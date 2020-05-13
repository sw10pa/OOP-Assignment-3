import java.awt.*;
import javax.swing.*;
import java.sql.SQLException;
import javax.swing.border.TitledBorder;

public class MetropolisViewer extends JFrame {

    private static final int TEXT_FIELDS_SIZE = 10;
    public static final String[] POPULATION_SEARCH_OPTIONS = {"Population larger than", "Population smaller than"};
    public static final String[] MATCH_TYPE_SEARCH_OPTIONS = {"Exact match", "Partial match"};

    private JTextField metropolisField;
    private JTextField continentField;
    private JTextField populationField;

    private JButton addButton;
    private JButton searchButton;

    private JComboBox<String> populationPulldown;
    private JComboBox<String> matchTypePulldown;

    private MetropolisModel metropolisModel;

    public MetropolisViewer() throws ClassNotFoundException, SQLException {
        super("Metropolis Viewer");

        setLayout(new BorderLayout(4, 4));

        createTopMenu();
        createSideMenu();
        createCenterDataTable();

        pack();
        addActionListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createTopMenu() {
        JPanel topMenuPanel = new JPanel();
        add(topMenuPanel, BorderLayout.NORTH);

        topMenuPanel.add(new JLabel("Metropolis:"));
        metropolisField = new JTextField(TEXT_FIELDS_SIZE);
        topMenuPanel.add(metropolisField);

        topMenuPanel.add(new JLabel("Continent:"));
        continentField = new JTextField(TEXT_FIELDS_SIZE);
        topMenuPanel.add(continentField);

        topMenuPanel.add(new JLabel("Population:"));
        populationField = new JTextField(TEXT_FIELDS_SIZE);
        topMenuPanel.add(populationField);
    }

    private void createSideMenu() {
        Box sideMenuBox = new Box(BoxLayout.Y_AXIS);
        add(sideMenuBox, BorderLayout.EAST);

        addButton = new JButton("Add");
        addButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        sideMenuBox.add(addButton);

        searchButton = new JButton("Search");
        searchButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        sideMenuBox.add(searchButton);

        Dimension buttonDimension = new Dimension(searchButton.getMaximumSize());
        addButton.setMaximumSize(buttonDimension);
        searchButton.setMaximumSize(buttonDimension);

        Box sideMenuInnerBox = new Box(BoxLayout.Y_AXIS);
        sideMenuInnerBox.setBorder(new TitledBorder("Search Options"));
        sideMenuBox.add(sideMenuInnerBox);

        populationPulldown = new JComboBox<>(POPULATION_SEARCH_OPTIONS);
        populationPulldown.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        sideMenuInnerBox.add(populationPulldown);

        matchTypePulldown = new JComboBox<>(MATCH_TYPE_SEARCH_OPTIONS);
        matchTypePulldown.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        sideMenuInnerBox.add(matchTypePulldown);

        Dimension comboBoxDimension = new Dimension(populationPulldown.getPreferredSize().width, populationPulldown.getPreferredSize().height);
        populationPulldown.setMaximumSize(comboBoxDimension);
        matchTypePulldown.setMaximumSize(comboBoxDimension);
    }

    private void createCenterDataTable() throws ClassNotFoundException, SQLException {
        metropolisModel = new MetropolisModel();
        JTable dataTable = new JTable(metropolisModel);

        JScrollPane scrollPane = new JScrollPane(dataTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addActionListeners() {
        addButton.addActionListener(e ->
        {
            try {
                metropolisModel.addEntry(metropolisField.getText(),
                                         continentField.getText(),
                                         populationField.getText());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        searchButton.addActionListener(e ->
        {
            try {
                metropolisModel.searchEntries(metropolisField.getText(),
                                              continentField.getText(),
                                              populationField.getText(),
                                              (String) populationPulldown.getSelectedItem(),
                                              (String) matchTypePulldown.getSelectedItem());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignored) { }

        MetropolisViewer metropolisViewer = new MetropolisViewer();
        metropolisViewer.setVisible(true);
    }

}
