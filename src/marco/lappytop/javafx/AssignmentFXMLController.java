/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marco.lappytop.javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;

/**
 * FXML Controller class
 * This controller is a result of an FXML conversion attempt
 * after the base code had been written.
 * The program still works as intended even though the event handlers
 * are located here rather than in the .java file where the base code
 * is located.
 * @author marco
 */
public class AssignmentFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

/**
 * This class serves as the event handler for the "Delete" button.
 * The "Delete" button removes all traces of a selected file (or student information)
 * saved in the program. This includes removing the selected name from the 
 * dropdown selection menu and removing the memory reference for the stored
 * object of the student information.
 * @author marco
 */
class DeleteHandler implements EventHandler<ActionEvent> {

    Stage stage;
    ComboBox<String> idNameMenu;    
    ArrayList<String> idNamesList;
    HashMap<String, Student> map;
 
    public DeleteHandler(Stage stage, ComboBox idNameMenu, 
            ArrayList idNamesList, HashMap map) {
        this.stage = stage;
        this.idNameMenu = idNameMenu;
        this.idNamesList = idNamesList;
        this.map = map;
    }
    
    public void handle(ActionEvent e) {
        System.out.println("Action confirmed.");
        String deleteName = idNameMenu.getSelectionModel().getSelectedItem();
        
        idNamesList.remove(deleteName);
        idNameMenu.getItems().remove(deleteName);
        map.remove(deleteName); 
        
    }
    
}

/**
 * This class serves as the event handler for the "Save" button.
 * The "Save" button writes all inputted data into the program Hash Map 
 * where the entered information is stored as an object with a dedicated fragment
 * of memory. The dropdown selection menu is also updated with the new name
 * entered by the user.
 * @author marco
 */
class SaveHandler implements EventHandler<ActionEvent> {

    Stage stage;
    ComboBox<String> idNameMenu;
    TextField idtext;
    TextField gpatext;
    ArrayList<String> idNamesList;
    HashMap<String, Student> map;

    public SaveHandler(Stage stage, ComboBox idNameMenu, TextField idtext, 
            TextField gpatext, ArrayList idNamesList, HashMap map) {
        this.stage = stage;
        this.idNameMenu = idNameMenu;
        this.idtext = idtext;
        this.gpatext = gpatext;
        this.idNamesList = idNamesList;
        this.map = map;
    }
    
    public void handle(ActionEvent e) {
        System.out.println("Action confirmed.");
        String newName = idNameMenu.getSelectionModel().getSelectedItem();
        String newId = idtext.getText();
        String newGpa = gpatext.getText();
        
        idNamesList.add(newName);
        Student newStudent = new Student(newName, newId, newGpa);
        idNameMenu.getItems().add(newName);
       
        map.put(newName, newStudent);
        
        //Writing file to save
        try {
            FileOutputStream savefile = new FileOutputStream(new File
            ("C:\\Users\\Marco\\Documents\\MyGitProjects\\MyFirstJavaFXApp\\AssignmentHashmap.ser"));
            ObjectOutputStream writefile = new ObjectOutputStream(savefile);
            writefile.writeObject(map);
            writefile.close();
            savefile.close();
        } catch(IOException ee) {
            ee.printStackTrace();
        }

    }
    
}

/**
 * This class serves as the event handler that is triggered every time a name from the dropdown menu is clicked.
 * The program retrieve the corresponding hash values for whatever name entry is loaded in the menu
 * and prints the corresponding ID and GPA into the text fields based on the program hash map.
 * @author marco
 */
class InfoHandler implements EventHandler<ActionEvent> {
    
    Stage stage;
    ComboBox<String> idNameMenu;
    TextField idtext;
    TextField gpatext;
    HashMap<String, Student> map;
    
    public InfoHandler(Stage stage, ComboBox idNameMenu, TextField idtext, 
            TextField gpatext, HashMap map) {
        this.stage = stage;
        this.idNameMenu = idNameMenu;
        this.idtext = idtext;
        this.gpatext = gpatext;
        this.map = map;
    }
    
    public void handle(ActionEvent e) {
        String selectName = idNameMenu.getSelectionModel().getSelectedItem();
        idtext.setText(map.get(selectName).id);
        gpatext.setText(map.get(selectName).gpa);
    }  
}

/*
/**
 * This class serves as the event handler for the program startup. 
 * The idea is to add whatever previously instated students 
 * are logged in the program's save file
 * and add them onto the data array that contains "Allen Bukowski" and "Jenna Morrison"
 * as default students logged in the system.
 * @author marco
 */
class StartHandler {
    
    Stage stage;
    ComboBox<String> idNameMenu;
    TextField idtext;
    TextField gpatext;
    ArrayList<String> idNamesList;
    HashMap<String, Student> map;
    
    public StartHandler(Stage stage, ComboBox idNameMenu, TextField idtext, 
            TextField gpatext, ArrayList idNamesList, HashMap map) {
        
        this.stage = stage;
        this.idNameMenu = idNameMenu;
        this.idtext = idtext;
        this.gpatext = gpatext;
        this.idNamesList = idNamesList;
        this.map = map;
    }
    
    public void handle() {
           
        //Reading saved file
        try {
            FileInputStream readDefaultFile = new FileInputStream
            ("C:\\Users\\Marco\\Documents\\MyGitProjects\\MyFirstJavaFXApp\\AssignmentHashmap.ser");
            ObjectInputStream viewDefaultFile = new ObjectInputStream(readDefaultFile);
            
            /*
            idNameMenu.getItems().removeAll(idNamesList);
            idNamesList.clear();
            map.clear();
            */
            
            map = (HashMap) viewDefaultFile.readObject();
            
            for(String name : map.keySet()) {
                idNamesList.add(name);
            }
            
            //idNameMenu.getItems().addAll(map.keySet());
            
            viewDefaultFile.close();
            readDefaultFile.close(); 

        } catch(IOException eee) {
            eee.printStackTrace();
        } catch (ClassNotFoundException eee) {
            eee.printStackTrace();
        }
        
        InfoHandler saveInfo = new InfoHandler(stage, idNameMenu, idtext, 
                gpatext, map);
        idNameMenu.setOnAction(saveInfo);
       
    }    
}


/**
 * This class serves as a set of stored information for each student.
 * The class be called and instantiated as a Student object.
 * The object is intended to be used as an object reference in a HashMap.
 * @author marco
 */
class Student implements Serializable {
    
    String name;
    String id;
    String gpa;
    private final long serialVersionUID = 4020970605441555052L;
    
    public Student(String name, String id, String gpa) {
        
        this.name = name;
        this.id = id;
        this.gpa = gpa;
    }
    
    public Student() {
        
        this.name = name;
        this.id = id;
        this.gpa = gpa;
    }
}


