package application;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ReplaceController implements Initializable {
	Stage stage;
	// 搜索文本
	private String searchStr;
	// 替换文本
	private String replaceStr;
	@FXML
	private TextField searchTextField;
	@FXML
	private TextField replaceTextField;
	@FXML
	private Button searchNextButton;
	@FXML
	private Button cancleButton;
	@FXML
	private Button replaceButton;
	@FXML
	private Button replaceAllButton;
	@FXML
	private CheckBox caseSensitiveCheckBox;
	@FXML
	private CheckBox loopCheckBox;

	public NotepadController controller;
	public Global global;

	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// 加载全局变量
		
		global = (Global) Main.globals.get("global");
		controller = (NotepadController) Main.controllers.get(NotepadController.class.getSimpleName());
		// 监听鼠标点击
		loopCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (loopCheckBox.isSelected())
					global.loop = true;
				else
					global.loop = false;
			}
		});
		// 监听鼠标点击
		caseSensitiveCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (caseSensitiveCheckBox.isSelected())
					global.caseSensitive = true;
				else
					global.caseSensitive = false;
			}
		});
	}

	public void replaceStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	void cancleButtonPressed(ActionEvent event) {
		stage.close();
	}

	@FXML
	void searchNextButtonPressed(ActionEvent event) {
		if (loopCheckBox.isSelected())
			global.loop = true;
		else
			global.loop = false;
		if (caseSensitiveCheckBox.isSelected())
			global.caseSensitive = true;
		else
			global.caseSensitive = false;
		searchStr = searchTextField.getText();
		replaceStr = replaceTextField.getText();
		global.searchText = searchStr;
		global.replaceText = replaceStr;
		controller.searchDown();
	}
	
	@FXML
	void replaceButtonPressed(ActionEvent event) {
		if (loopCheckBox.isSelected())
			global.loop = true;
		else
			global.loop = false;
		if (caseSensitiveCheckBox.isSelected())
			global.caseSensitive = true;
		else
			global.caseSensitive = false;
		searchStr = searchTextField.getText();
		replaceStr = replaceTextField.getText();
		global.searchText = searchStr;
		global.replaceText = replaceStr;
		controller.replace();
	}

	@FXML 
	void replaceAllButtonPressed(ActionEvent event) {
		if (loopCheckBox.isSelected())
			global.loop = true;
		else
			global.loop = false;
		if (caseSensitiveCheckBox.isSelected())
			global.caseSensitive = true;
		else
			global.caseSensitive = false;
		searchStr = searchTextField.getText();
		replaceStr = replaceTextField.getText();
		global.searchText = searchStr;
		global.replaceText = replaceStr;
		controller.replaceALL();
	}
}
