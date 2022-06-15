package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AboutController implements Initializable {
	Stage stage;

	public NotepadController controller;
	public Global global;
	
	@FXML
	private Button confirmButton;
	@FXML
	private ImageView iconImage;
	@FXML
	private ImageView logoImage;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//加载全局变量
        global = (Global) Main.globals.get("global");
        controller = (NotepadController) Main.controllers.get(NotepadController.class.getSimpleName());
	}
	
	public void aboutStage(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	void confirmButtonPressed(ActionEvent event) {	
		confirmButton.requestFocus();
		stage.close();
	}

}
