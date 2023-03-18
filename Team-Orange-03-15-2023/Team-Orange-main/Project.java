import javax.swing.*;
import java.util.*;

/**
 * @author JD Sawyer, Michael Tuskan, Ian Liston
 */
public class Project {
    private LinkedList<Task> toDoList;
    private LinkedList<Material> materials;
    private LinkedList<Receipt> receipts;
    private LinkedList<Plan> plans;
    private LinkedList<Client> clients;
    private final ArrayList<JTextArea> taskTextArr;
    private final ArrayList<JPanel> taskPanelArr;
    private Project current;
    public int projectID = 0;

    Project(ArrayList<JPanel> taskPanelArr, ArrayList<JTextArea> taskTextArr){
        this.toDoList  = new LinkedList<>();
        this.materials  = new LinkedList<>();
        this.receipts  = new LinkedList<>();
        this.plans  = new LinkedList<>();
        this.clients = new LinkedList<>();
        this.taskTextArr = taskTextArr;
        this.taskPanelArr = taskPanelArr;
    }

    /** @author Ian Liston */
    public void deleteTask(String taskText){
        for(int n = 0; n < toDoList.size();n++){
            if(toDoList.get(n).getTask().equals(taskText)){
                toDoList.remove(n);
                break;
            }
        }
        rePopulateTasks();
    }

    /**
     * @author Ian Liston
    */
    public void rePopulateTasks(){
        if(!toDoList.isEmpty())
            for(int n = 0; n < toDoList.size(); n++){
                if(taskTextArr.size() > n) {
                    taskTextArr.get(n).setText(toDoList.get(n).getTask());
                    taskPanelArr.get(n).setVisible(true);
                }
            }
    }

    public void addReceipt(Receipt receipt){
    receipts.add(receipt);
    }

    public void addPlan(Plan plan){
        plans.add(plan);
    }

    /** @author Michael Tuskan */
    public void addMaterials(Material material){
        materials.add(material);
    }

    /** @author Michael Tuskan */
    public void addClient(Client client) {
        clients.add(client);
    }

    /** @author Michael Tuskan */
    public Material[] getMaterials() {
        Material[] materialList = new Material[materials.size()];
        for (int idx = 0; idx < materials.size(); idx++) {
            materialList[idx] = materials.get(idx);
        }
        return materialList;
    }

    /** @author Michael Tuskan */
    public Receipt[] getReceipts() {
        Receipt[] receiptList = new Receipt[receipts.size()];
        for (int idx = 0; idx < receipts.size(); idx++) {
            receiptList[idx] = receipts.get(idx);
        }
        return receiptList;
    }

    /** @author Michael Tuskan */
    public Plan[] getPlans() {
        Plan[] planList = new Plan[plans.size()];
        for (int idx = 0; idx < plans.size(); idx++) {
            planList[idx] = plans.get(idx);
        }
        return planList;
    }

    /** @author Michael Tuskan */
    public Client[] getClients() {
        Client[] clientList = new Client[clients.size()];
        for (int idx = 0; idx < clients.size(); idx++) {
            clientList[idx] = clients.get(idx);
        }
        return clientList;
    }

    /** @author Michael Tuskan */
    public void removeReceipt(int compareNameIndex) {
        for (int idx = 0; idx < receipts.size(); idx++) {
            if (idx == compareNameIndex) {
                receipts.remove(idx);
            }
        }
    }

    /** @author Michael Tuskan */
    public void removeMaterial(int compareNameIndex) {
        for (int idx = 0; idx < materials.size(); idx++) {
            if (idx == compareNameIndex) {
                materials.remove(idx);
            }
        }
    }

    /** @author Michael Tuskan */
    public void removePlan(int compareNameIndex) {
        for (int idx = 0; idx < plans.size(); idx++) {
            if (idx == compareNameIndex) {
                plans.remove(idx);
            }
        }
    }

    /** @author Michael Tuskan */
    public void removeClient(int compareNameIndex) {
        for (int idx = 0; idx < clients.size(); idx++) {
            if (idx == compareNameIndex) {
                clients.remove(idx);
            }
        }
    }

    public void addTask(Task task){
        toDoList.add(task);
        toDoList.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.compareTo(o2);
            }
        });
        Collections.reverse(toDoList);
    }

    public String printList(){
        StringBuilder str = new StringBuilder();
        for (Task task : toDoList) {
            str.append(task.toString() + "\n");
        }
        return str.toString();
    }

    //TODO FOR CONSOLE TESTING
    /** @author Michael Tuskan */
    public String printResources() {
        StringBuilder str = new StringBuilder();
        str.append("Receipt(s): ");
        str.append("\n");
        for (Receipt receipt : receipts) {
            str.append(receipt.toString() + "\n");
        }
        str.append("\n");
        str.append("Material(s): ");
        str.append("\n");
        for (Material material : materials) {
            str.append(material.toString() + "\n");
        }
        str.append("\n");
        str.append("Plan(s): ");
        str.append("\n");
        for (Plan plan : plans) {
            str.append(plan.toString() + "\n");
        }
        str.append("\n");
        str.append("Client(s): ");
        str.append("\n");
        for (Client client : clients) {
            str.append(client.toString() + "\n");
        }
        return str.toString();
    }

    public void setCurrent(Project current) {
        this.current = current;
    }

    public Project getCurrent() {
        return current;
    }

    public void setProjectID(int ID){
        this.projectID = ID;
    }

    public int getProjectID(){
        return projectID;
    }

}