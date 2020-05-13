import java.sql.*;
import java.util.ArrayList;

public class MetropolisDatabase {

    private final String databaseName;
    private final String tableName;

    private Connection connection;

    public MetropolisDatabase(String databaseName, String tableName) throws ClassNotFoundException, SQLException {
        this.databaseName = databaseName;
        this.tableName = tableName;

        openConnection();

        createDatabase();
        createTable();
    }

    private void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "Stephane27");
    }

    private void createDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP DATABASE IF EXISTS " + databaseName +";");
        statement.execute("CREATE DATABASE " + databaseName + ";");
        statement.execute("USE " + databaseName + ";");
    }

    private void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS " + tableName + ";");
        statement.execute("CREATE TABLE " + tableName + "(metropolis CHAR(64)," +
                                                            " continent CHAR(64)," +
                                                            " population BIGINT);");
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void saveEntry(String metropolis, String continent, String population) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " " +
                                                                      "VALUES (?, ?, ?);");

        statement.setString(1, metropolis);
        statement.setString(2, continent);
        statement.setString(3, population);

        statement.execute();
        statement.close();
    }

    private String buildQuery(String metropolis, String continent, String population, String populationFilter, String matchTypeFilter) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM ");
        query.append(tableName);

        if (!population.isEmpty()) {
            query.append(" WHERE population ");
            if (populationFilter.equals(MetropolisViewer.POPULATION_SEARCH_OPTIONS[0])) {
                query.append("> ");
            } else {
                query.append("< ");
            }
            query.append(population);
        }

        if (!metropolis.isEmpty()) {
            if (population.isEmpty()) {
                query.append(" WHERE metropolis ");
            } else {
                query.append(" AND metropolis ");
            }

            if (matchTypeFilter.equals(MetropolisViewer.MATCH_TYPE_SEARCH_OPTIONS[0])) {
                query.append("= '").append(metropolis).append("'");
            } else {
                query.append("LIKE '%").append(metropolis).append("%'");
            }
        }

        if (!continent.isEmpty()) {
            if (population.isEmpty() && metropolis.isEmpty()) {
                query.append(" WHERE continent ");
            } else {
                query.append(" AND continent ");
            }

            if (matchTypeFilter.equals(MetropolisViewer.MATCH_TYPE_SEARCH_OPTIONS[0])) {
                query.append("= '").append(continent).append("'");
            } else {
                query.append("LIKE '%").append(continent).append("%'");
            }
        }

        query.append(";");

        return query.toString();
    }

    public ArrayList<ArrayList<String>> fetchEntries(String metropolis, String continent, String population, String populationFilter, String matchTypeFilter) throws SQLException {
        String query = buildQuery(metropolis, continent, population, populationFilter, matchTypeFilter);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ArrayList<ArrayList<String>> result = new ArrayList<>();

        while (resultSet.next()) {
            ArrayList<String> entry = new ArrayList<>();

            entry.add(resultSet.getString(1));
            entry.add(resultSet.getString(2));
            entry.add(resultSet.getString(3));

            result.add(entry);
        }

        return result;
    }

}
