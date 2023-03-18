import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/** @author Michael Tuskan, JD Sawyer, Gregory Yi
 *
 * ProjectDirectory Class - Functions as Panel to create, remove or load projects into Dashboard
 *
 * */
public class ProjectDirectory extends JDialog{
    private JPanel mainCreateProjectPanel;
    private JPanel createProjectPanel;
    private JLabel ProjectSelectedLabel;
    private JTextArea projectSelectedTextArea;
    private JPanel createProjectOrangeTitleLabel;
    private JLabel projectTitleLabel;
    private JComboBox projectDirectoryComboBox;
    private JButton loadProjectButton;
    private JButton removeProjectButton;
    private JButton createProjectButton;
    private JLabel newProjectNameLabel;
    private JTextField newProjectNameTextField;
    private JTextArea createProjectTextArea;
    private LinkedList<Project> project;
    private int projectCount;
    private int projectSelectedKey;

    private HashMap<Integer, Project> projectHashMap;

    /**
     * Project Directory Constructor
     * @author Michael Tuskan, JD Sawyer, Gregory Yi
     * @param parent
     */
    public ProjectDirectory(JFrame parent) {
        super(parent);

        //Main Dashboard Panel Options
        setTitle("Projects");
        setContentPane(mainCreateProjectPanel);
        setMinimumSize(new Dimension(500, 300));
        setModal(true);
        setLocationRelativeTo(null);
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        projectHashMap = new HashMap<>();
        project = new LinkedList<>();
        projectCount = 0;
        projectSelectedKey = 0;

        /** @author Michael Tuskan */
        getCreateProjectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = false;
                if (projectCount >= 1) {
                    for (int idx = 0; idx < projectCount; idx++) {
                        if (getNewProjectNameTextField().getText().matches(projectHashMap.get(idx).getProjectName())) {
                            displayProjectMessage("Sorry, this name has been taken.");
                        }
                    }
                    if (!flag) {
                        String projectName = getNewProjectNameTextField().getText();
                        addProject();
                        addProjectToDB(projectName);
                        displayProjectMessage("Project Created.");
                    }
                } else {
                    addProject();
                    displayProjectMessage("Project Created.");
                }
            }
        });

        /** @author JD Sawyer  */
        getRemoveProjectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project tempDel = null;
                int removeKey = 0;
                if (projectCount > 0) {
                    for(Map.Entry<Integer,Project> curr : projectHashMap.entrySet()) {
                        int currKey = curr.getKey();
                        Project currProj = curr.getValue();
                        if (currProj.getProjectName().matches(getProjectSelectedTextArea().getText())) {
                            tempDel = projectHashMap.get(currKey);
                            removeKey = currKey;
                            break;
                        }
                    }
                    if(tempDel != null) {
                        project.removeFirstOccurrence(tempDel);
                        projectCount--;
                        projectHashMap.remove(removeKey);
                        projectDirectoryComboBox.removeItemAt(projectDirectoryComboBox.getSelectedIndex()); //Might be causing it
                    }
                }

            }



        });



        /** @author Michael Tuskan */
        getLoadProjectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(projectSelectedKey);
//                setVisible(false);
                projectHashMap.get(projectSelectedKey).getDashboard().setProject(project.get(projectSelectedKey));
                projectHashMap.get(projectSelectedKey).getDashboard().setVisible(true);
            }
        });




        /** @author Michael Tuskan */
        projectDirectoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cbProject = (JComboBox)e.getSource();
                String project = (String) cbProject.getSelectedItem();
                getProjectSelectedTextArea().setText(project);

                /** @author JD Sawyer */
                for(Map.Entry<Integer,Project> curr : projectHashMap.entrySet()) {
                    int currKey = curr.getKey();
                    Project currProj = curr.getValue();
                    if (currProj.getProjectName().matches(getProjectSelectedTextArea().getText())) {
                        projectSelectedKey = currKey;
                        break;
                    }
                }
            }
        });
    }
    /** @author Michael Tuskan */
    public void addProject() {
        project.add(new Project());
        projectHashMap.put(projectCount, project.get(projectCount));
        projectHashMap.get(projectCount).setProjectKey(projectCount);
        projectHashMap.get(projectCount).setProjectName(getNewProjectNameTextField().getText());
        setProjectDirectoryComboBox(projectHashMap.get(projectCount));
    }

    /** @author Michael Tuskan */
    public void displayProjectMessage(String message) {
        setCreateProjectTextArea(message);
        ActionListener CreatedTask = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setCreateProjectTextArea("");
            }
        };
        Timer timer = new Timer(3000 ,CreatedTask);
        timer.setRepeats(false);
        timer.start();
        projectCount++;
    }

    public void addProjectToDB(String projectName) {
        final String MYSQL_SERVER_URL = "jdbc:mysql://teamorange.mysql.database.azure.com /";
        final String DB_URL = "jdbc:mysql://teamorange.mysql.database.azure.com /login?serverTimezone=UTC";
        final String USERNAME = "orange";
        final String PASSWORD = "!Team360";

        try {
            //First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.close();
            conn.close();

            //Second, connect to the database and create the table "users" if cot created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + Dashboard.getCurrUserName() + projectName + "("
                    + "id VARCHAR( 20 ) NOT NULL PRIMARY KEY,"
                    + "receipts VARCHAR(200) NOT NULL,"
                    + "clients VARCHAR(200) NOT NULL UNIQUE,"
                    + "materials VARCHAR(200),"
                    + "plan VARCHAR(200),"
                    + "todo VARCHAR(200) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);
            System.out.println("Successfully added " + Dashboard.getCurrUserName() + projectName) ;

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    /** @author Michael Tuskan */
    public JTextArea getCreateProjectTextArea() {
        return createProjectTextArea;
    }

    /** @author Michael Tuskan */
    public JButton getCreateProjectButton() {
        return createProjectButton;
    }

    /** @author Michael Tuskan */
    public JButton getLoadProjectButton() {
        return loadProjectButton;
    }

    /** @author Michael Tuskan */
    public JButton getRemoveProjectButton() {
        return removeProjectButton;
    }

    /** @author Michael Tuskan */
    public JTextField getNewProjectNameTextField() {
        return newProjectNameTextField;
    }


    /** @author Michael Tuskan */
    public JTextArea getProjectSelectedTextArea() {
        return projectSelectedTextArea;
    }


    /** @author Michael Tuskan */
    public void setProjectDirectoryComboBox(Project project) {
        projectDirectoryComboBox.addItem(project.getProjectName());

    }

    public void setCreateProjectTextArea(String message) {
        getCreateProjectTextArea().setText(message);
    }



}
