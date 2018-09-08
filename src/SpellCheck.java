// Noah Drew - September 7, 2018
// Program accepts a dictionary file and a file to check against that dictionary file and finds unrecognized words

// Imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class SpellCheck extends Application{
	
	// Global Variables
	File dictionaryFile;
	File toBeCheckedFile;
	ArrayList<String> dictionaryList;
	ArrayList<String> toBeCheckedList;

	// Main method to launch JavaFX application
	public static void main(String[] args) {	
		launch(args);
	}

	// Start method used to set up the stage
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Vbox to hold all items
		VBox root = new VBox(5);	
		// Hbox to hold buttons
		HBox fileButtonsBox = new HBox(10);
		
		// Button to open a dictionary file
		Button buttonOpenDictionaryFile = new Button("Open Dictionary File");
		buttonOpenDictionaryFile.setPrefSize(245, 30);
		buttonOpenDictionaryFile.setFont(new Font(20));
		
		// Button to open a file to check against the dictionary
		Button buttonOpenToBeCheckedFile = new Button("Open File to Check");
		buttonOpenToBeCheckedFile.setPrefSize(245, 30);
		buttonOpenToBeCheckedFile.setFont(new Font(20));
		
		fileButtonsBox.getChildren().add(buttonOpenDictionaryFile);
		fileButtonsBox.getChildren().add(buttonOpenToBeCheckedFile);
		
		// Button to compare the files
		Button buttonFindUnrecognizedWords = new Button("Find Unrecognized Words");
		buttonFindUnrecognizedWords.setPrefSize(500, 30);
		buttonFindUnrecognizedWords.setFont(new Font(20));
		
		// ListView to display the words unrecognized
		ListView<String> listView = new ListView<>();		
		
		// Allows files to be chosen
		FileChooser fileChooser = new FileChooser();	
		
		// When pressed, allow the user to choose a file for the dictionary. If a file is chosen, highlight green. If a file is not choosen, highlight red.
		buttonOpenDictionaryFile.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				try {
					dictionaryFile = getFile(fileChooser, "Select the Dictionary File", primaryStage);	
					dictionaryList = getFileContents(dictionaryFile);
					
					if (dictionaryList.isEmpty()) {
						buttonOpenDictionaryFile.setTextFill(Color.ORANGERED);
					} else {
						buttonOpenDictionaryFile.setTextFill(Color.LIMEGREEN);
					}
					
				} catch (Exception e) {
					System.out.println("No file selected");
				}
			}
		});
		
		// When pressed, allow the user to choose a file to be checked against the dictionary. If a file is chosen, highlight green. If a file is not choosen, highlight red.
		buttonOpenToBeCheckedFile.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					toBeCheckedFile = getFile(fileChooser, "Select File to Check", primaryStage);
					toBeCheckedList = getFileContents(toBeCheckedFile);
					
					if (toBeCheckedList.isEmpty()) {
						buttonOpenToBeCheckedFile.setTextFill(Color.ORANGERED);
					} else {
						buttonOpenToBeCheckedFile.setTextFill(Color.LIMEGREEN);
					}
					
				} catch (Exception e) {
					System.out.println("No file selected");
				}
			}
		});
		
		// When pressed, take the unrecognized words list and add it to an observable list. Clear anything previously displayed and redisplay the contents.
		buttonFindUnrecognizedWords.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				ArrayList<String> unrecognizedWordsList = getUnrecognizedWords(dictionaryList, toBeCheckedList);
				ObservableList<String> observableList = FXCollections.observableArrayList();
				observableList.clear();
				listView.setItems(null);
				observableList.setAll(unrecognizedWordsList);
				listView.setItems(observableList);
			}
		});
		
		// Configure scene and stage
		root.getChildren().add(fileButtonsBox);
		root.getChildren().add(buttonFindUnrecognizedWords);
		root.getChildren().add(listView);
		
		Scene scene = new Scene(root,500,480);
		primaryStage.setTitle("Spell Checker");		
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	// Open the file chooser dialog and return selected file
	private File getFile(FileChooser fileChooser, String chooserTitle, Stage stage) {
		
		fileChooser.setTitle(chooserTitle);
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));

		return fileChooser.showOpenDialog(stage);
	}
	
	// Loop through each line in the given file and return an array list of strings
	private ArrayList<String> getFileContents(File file) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				list.add(line);
			}
			
			bufferedReader.close();
			reader.close();
			
		} catch (Exception e) {
			System.out.println("No File Selected");
		}
		
		return list;
	}
	
	// Compare words from the file to be checked against the dictionary
	private ArrayList<String> getUnrecognizedWords(ArrayList<String> dictionaryList, ArrayList<String> toBeCheckedList) {
		
		ArrayList<String> unrecognizedWordsList = new ArrayList<>();
		boolean matchFound;
		
		try {	
			// Loop through both lists checking if there is a match
			for (int i = 0; i < toBeCheckedList.size(); i++) {
				matchFound = false;
				for (int j = 0; j < dictionaryList.size(); j++) {
					if (toBeCheckedList.get(i).equals(dictionaryList.get(j))) {
						matchFound = true;
					}
				}
				
				// If there is no match found, add it to the list of unrecognized words
				if (!matchFound) {
					unrecognizedWordsList.add(toBeCheckedList.get(i));
					System.out.println(toBeCheckedList.get(i));
				}
			}
			// Return list of unrecognized words
			return unrecognizedWordsList;
			
		} catch (Exception e) {
			// If both files are not found, return empty list
			System.out.println("Both files not selected.");
			return unrecognizedWordsList;
		}
	}
	

}
