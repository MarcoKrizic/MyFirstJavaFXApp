
package marco.lappytop.javafx;

//Imported packages 
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import java.io.*;

/**
 * This program serves as a means to practice the basic elements of a JavaFX GUI.
 * The program includes a menu with an editable dropdown menu box containing names,
 * a couple of specified text fields on the right side for coinciding ID numbers
 * and GPAs for the names, buttons on the bottom for future delete and save
 * functions, and a checkbox that allows for editing permissions on all the fields.
 * @author marco
 */
public class Assignment extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {
        
        //Creating FXML loader
        FXMLLoader loader = new FXMLLoader();
        
        //Creating path to FXML file
        //This will allow for the event handlers to run through my FXML controller
        AssignmentFXMLController controller = loader.getController();
        
        //Creating window
        Pane pane = new Pane();
        pane.setPadding(new Insets(10, 10, 10, 10));

        //Creating stage graphics
        BackgroundFill grey = new BackgroundFill(Color.GREY, null, null);
        Background body = new Background(grey);
        pane.setBackground(body);
       
        //Creating button layout
        Button delete = new Button("  Delete  ");
        Button save = new Button("  Add  ");
        
        delete.setLayoutX(50); delete.setLayoutY(220); delete.setPrefWidth(150);
        save.setLayoutX(300); save.setLayoutY(220); save.setPrefWidth(150);

        pane.getChildren().setAll(delete, save);
        
        //Creating labels
        Text id = new Text ("ID");
        Text gpa = new Text ("GPA");
        
        id.setLayoutX(300);  id.setLayoutY(60);
        gpa.setLayoutX(300); gpa.setLayoutY(100);
        
        pane.getChildren().addAll(id, gpa);
        
        //Creating labelled textboxes
        TextField idtext = new TextField();
        TextField gpatext = new TextField();
        
        idtext.setPrefColumnCount(10);
        idtext.setLayoutX(330); idtext.setLayoutY(60);
        gpatext.setPrefColumnCount(5);
        gpatext.setLayoutX(330); gpatext.setLayoutY(100);
        
        pane.getChildren().addAll(idtext, gpatext);
        
        //Creating title
        Text titlecap = new Text ("Student Grade Database");
        
        titlecap.setLayoutX(125); titlecap.setLayoutY(25);
        titlecap.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 24));
        
        pane.getChildren().addAll(titlecap);
        
        //Creating name textbox (big textbox with combo box for list of saved IDs)
        ComboBox<String> idNameMenu = new ComboBox<>();
        String[] idNames = {"Allen Bukowski", "Jenna Morrison"};
        ArrayList<String> idNamesList = new ArrayList<>();
        HashMap<String, Student> map = new HashMap<>();
        
        Student allen = new Student("Allen Bukowski", "192348201", "3.5");
        Student jenna = new Student("Jenna Morrison", "128392102", "4.0");
        
        idNameMenu.setLayoutX(30); idNameMenu.setLayoutY(70);      
        for (String name: idNames) {
            idNamesList.add(name);
        }
        idNameMenu.getItems().addAll(idNamesList);
        
        map.put("Allen Bukowski", allen);
        map.put("Jenna Morrison", jenna);
        
        
        //Ensuring that Student objects "Allen Bukowski" and "Jenna Morrison"
        //Stay as default objects in the program on startup as new 
        //This will be done by saving and reading a dedicated "Default" file
        try {
            FileOutputStream infoDefault = new FileOutputStream(new File
            ("C:\\Users\\Marco\\Documents\\MyGitProjects\\MyFirstJavaFXApp\\AssignmentDefault.ser"));
            ObjectOutputStream infoWrite = new ObjectOutputStream(infoDefault);
            infoWrite.writeObject(map);
            infoWrite.close();
            infoDefault.close();
        } catch(IOException e){
            e.printStackTrace();
        }
        
        try {
            FileInputStream readDefault = new FileInputStream(
            "C:\\Users\\Marco\\Documents\\MyGitProjects\\MyFirstJavaFXApp\\AssignmentDefault.ser");
            ObjectInputStream infoRead = new ObjectInputStream(readDefault);
            
            idNameMenu.getItems().removeAll(idNamesList);
            idNamesList.clear();            
            map.clear();
            
            map = (HashMap) infoRead.readObject();
            
            for(String name : map.keySet()) {
                idNamesList.add(name);
            }
            idNameMenu.getItems().addAll(map.keySet());
            
            
            infoRead.close();
            readDefault.close();
            
        } catch(IOException ee) {
            ee.printStackTrace();
        } catch(ClassNotFoundException ee) {
            ee.printStackTrace();
        }
        
        //Launching program automatically with previous save file
        StartHandler defaultStart = new StartHandler(stage, idNameMenu, idtext, 
                gpatext, idNamesList, map);
        defaultStart.handle();

        pane.getChildren().addAll(idNameMenu);
        
        //Creating 'Edit' checkbox
        Text edit = new Text("Edit");
        CheckBox editbox = new CheckBox();
        edit.setLayoutX(30); edit.setLayoutY(170);
        editbox.setLayoutX(55); editbox.setLayoutY(155);
        
        //Managing checkbox behaviours
        delete.disableProperty().bind(editbox.selectedProperty().not());
        save.disableProperty().bind(editbox.selectedProperty().not());
        idtext.editableProperty().bind(editbox.selectedProperty());
        idtext.disableProperty().bind(editbox.selectedProperty().not());
        gpatext.editableProperty().bind(editbox.selectedProperty());
        gpatext.disableProperty().bind(editbox.selectedProperty().not());
        idNameMenu.editableProperty().bind(editbox.selectedProperty());
        
        pane.getChildren().addAll(edit, editbox);
        
        //Creating handlers for buttons
        //Delete Button
        DeleteHandler delbutton = new DeleteHandler(stage, idNameMenu, 
                idNamesList, map);
        delete.setOnAction(delbutton);
        
        //Save Button
        SaveHandler savbutton = new SaveHandler(stage, idNameMenu, idtext, 
                gpatext, idNamesList, map);
        save.setOnAction(savbutton);
        
        //Dropdown Menu Botton, clicking will return the pre-entered id and gpa values
        InfoHandler listbutton = new InfoHandler(stage, idNameMenu, idtext, 
                gpatext, map);
        idNameMenu.setOnAction(listbutton);
        
        //Rendering assignment window
        Scene scene = new Scene(pane, 500, 250);
        stage.setTitle("Student Grade Storage Menu");
        stage.setScene(scene);
        stage.show();
        
    }
}




