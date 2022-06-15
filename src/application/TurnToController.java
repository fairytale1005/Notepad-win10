package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class TurnToController implements Initializable {
	Stage stage;
	int turnToNum;
	
	public NotepadController controller;
    public Global global;
    
	@FXML
	private Button turnToButton;
	@FXML 
	private Button cancelButton;
	@FXML
	private TextField textField;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//加载全局变量
        global = (Global) Main.globals.get("global");
        controller = (NotepadController) Main.controllers.get(NotepadController.class.getSimpleName());
	}
	
	public void turnToStage(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	void turnToButtonPressed(ActionEvent event) {
		try {
			turnToNum = Integer.parseInt(textField.getText());
		}
		catch(Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR); // 创建一个消息对话框
			alert.setTitle("转到");
			alert.setHeaderText("不能接受的字符");
			alert.setContentText("你只能在次数输入数字");
			alert.showAndWait(); // 模态显示对话框
			return;
		}
		global.turnTo = turnToNum;
        controller.turnTo();
	}
	
	@FXML 
	void cancelButtonPressed(ActionEvent event) {
		stage.close();
	}

	
}
