import java.util.ArrayList;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class MetropolisModel extends AbstractTableModel {

    public static final String[] columnNames = {"metropolis", "continent", "population"};

    private final MetropolisDatabase metropolisDatabase;

    private ArrayList<ArrayList<String>> entries;

    public MetropolisModel() throws ClassNotFoundException, SQLException {
        super();
        entries = new ArrayList<>();
        metropolisDatabase = new MetropolisDatabase("metropolis", "metropolises");
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return entries.get(rowIndex).get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void addEntry(String metropolis, String continent, String population) throws SQLException {
        int stringToInt;
        try {
            stringToInt = Integer.parseInt(population);
        } catch (Exception e){
            stringToInt = -1;
        }

        if (!metropolis.isEmpty() && !continent.isEmpty() && stringToInt > 0) {
            metropolisDatabase.saveEntry(metropolis, continent, population);

            ArrayList<String> entry = new ArrayList<>();
            entry.add(metropolis);
            entry.add(continent);
            entry.add(population);

            entries.clear();
            entries.add(entry);

            fireTableDataChanged();
        }
    }

    public void searchEntries(String metropolis, String continent, String population, String populationFilter, String matchTypeFilter) throws SQLException {
        entries = metropolisDatabase.fetchEntries(metropolis, continent, population, populationFilter, matchTypeFilter);
        fireTableDataChanged();
    }

    public void closeConnection() throws SQLException {
        metropolisDatabase.closeConnection();
        entries.clear();

        fireTableDataChanged();
    }

}
