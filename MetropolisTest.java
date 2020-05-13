import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.*;
import java.sql.SQLException;

public class MetropolisTest {

    MetropolisModel testModel;

    @BeforeEach
    public void SetUp() throws ClassNotFoundException, SQLException {
        testModel = new MetropolisModel();
    }

    @Test
    public void emptyDatabaseTest() {
        assertEquals(0, testModel.getRowCount());
        assertEquals(3, testModel.getColumnCount());

        assertEquals("metropolis", testModel.getColumnName(0));
        assertEquals("continent", testModel.getColumnName(1));
        assertEquals("population", testModel.getColumnName(2));
    }

    @Test
    public void addLegalEntryTest() throws SQLException {
        testModel.addEntry("Kutaisi", "Europe", "147500");

        assertEquals(1, testModel.getRowCount());
        assertEquals(3, testModel.getColumnCount());

        assertEquals("Kutaisi", testModel.getValueAt(0, 0));
        assertEquals("Europe", testModel.getValueAt(0, 1));
        assertEquals("147500", testModel.getValueAt(0, 2));
    }

    @Test
    public void addIllegalEntryTest() throws SQLException {
        testModel.addEntry("", "Europe", "147500");
        testModel.addEntry("Kutaisi", "", "147500");
        testModel.addEntry("Kutaisi", "Europe", "-147500");
        testModel.addEntry("Kutaisi", "Europe", "Tbilisi");

        assertEquals(0, testModel.getRowCount());
    }

    @Test
    public void addLegalEntriesTest() throws SQLException {
        testModel.addEntry("Kutaisi", "Europe", "147500");
        testModel.addEntry("Mumbai", "Asia", "20400000");
        testModel.addEntry("New York", "North America", "21295000");
        testModel.addEntry("San Francisco", "North America", "5780000");
        testModel.addEntry("London", "Europe", "8580000");
        testModel.addEntry("Rome", "Europe", "2715000");
        testModel.addEntry("Melbourne", "Australia", "3900000");
        testModel.addEntry("San Jose", "North America", "7354555");
        testModel.addEntry("Rostov-on-Don", "Europe", "1052000");

        testModel.searchEntries("", "", "21295000", "Population larger than", "Exact match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("", "", "147500", "Population larger than", "Exact match");
        assertEquals(8, testModel.getRowCount());
        testModel.searchEntries("", "", "147500", "Population smaller than", "Exact match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("", "", "21295000", "Population smaller than", "Exact match");
        assertEquals(8, testModel.getRowCount());

        testModel.searchEntries("Kutaisi", "Europe", "", "Population smaller than", "Exact match");
        assertEquals(1, testModel.getRowCount());
        testModel.searchEntries("Kutaisi", "Asia", "", "Population smaller than", "Exact match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("", "Europe", "", "Population smaller than", "Exact match");
        assertEquals(4, testModel.getRowCount());
        testModel.searchEntries("", "Asia", "", "Population smaller than", "Exact match");
        assertEquals(1, testModel.getRowCount());

        testModel.searchEntries("", "", "", "Population smaller than", "Partial match");
        assertEquals(9, testModel.getRowCount());
        testModel.searchEntries(" ", " ", "", "Population smaller than", "Partial match");
        assertEquals(3, testModel.getRowCount());
        testModel.searchEntries("San", "North America", "", "Population smaller than", "Partial match");
        assertEquals(2, testModel.getRowCount());
        testModel.searchEntries("-", "", "", "Population smaller than", "Partial match");
        assertEquals(1, testModel.getRowCount());
    }

    @Test
    public void searchIllegalEntriesTest() throws SQLException {
        testModel.searchEntries("Kutaisi", "Europe", "147500", "Population larger than", "Exact match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("Kutaisi", "Europe", "147500", "Population larger than", "Partial match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("Kutaisi", "Europe", "147500", "Population smaller than", "Exact match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("Kutaisi", "Europe", "147500", "Population smaller than", "Partial match");
        assertEquals(0, testModel.getRowCount());

        testModel.searchEntries("", "Europe", "147500", "Population larger than", "Exact match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("Kutaisi", "", "147500", "Population larger than", "Partial match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("Kutaisi", "Europe", "", "Population smaller than", "Exact match");
        assertEquals(0, testModel.getRowCount());
        testModel.searchEntries("", "Europe", "", "Population smaller than", "Partial match");
        assertEquals(0, testModel.getRowCount());
    }

    @Test
    public void closeConnectionTest() throws SQLException {
        testModel.closeConnection();
        assertEquals(0, testModel.getRowCount());
    }

}
