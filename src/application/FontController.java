package application;

import java.awt.GraphicsEnvironment;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class FontController implements Initializable {
	Stage stage;

	public NotepadController controller;
	public Global global;
	
	@FXML
	private ListView<String> sizeListView;// 大小
	@FXML
	private ListView<String> fontListView;// 字体
	@FXML
	private ListView<String> typefaceListView;// 字形
	@FXML
	private Button confirmButton;
	@FXML
	private Button cancelButton;
	@FXML
	private TextArea textField;// 示例
	@FXML
	private TextField sizeTextField;
	@FXML
	private TextField fontTextField;
	@FXML
	private TextField typefaceTextField;

	//中文预览的字符串
    private static final String CH_STRING = "预览字体";
    //英文预览的字符串
    private static final String EN_STRING = "AaBbYyZz";
    //数字预览的字符串
    private static final String NUMBER_STRING = "0123456789";
	// 预设字体，也是将来要返回的字体
	private Font font = null;
	// 所有字体
	private ObservableList<String> fontArray = null;
	// 所有字形
	private ObservableList<String> typefaceArray = FXCollections.observableArrayList("常规", "粗体", "斜体");
	// 所有预设字体大小
	private ObservableList<String> sizeArray = FXCollections.observableArrayList("8", "9", "10", "11", "12", "15", "14",
			"16", "18", "20", "22", "24", "26", "28", "36", "48", "72", "初号", "小初", "一号", "小一", "二号", "小二", "三号", "小三",
			"四号", "小四", "五号", "小五", "六号", "小六", "七号", "八号");
	// 上面数组中对应的字体大小
	private int[] sizeIntArray = { 8, 9, 10, 11, 12, 14, 15, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72, 42, 36, 26, 24, 22,
			18, 16, 15, 14, 12, 10, 9, 8, 7, 6, 5 };

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// 加载全局变量
		global = (Global) Main.globals.get("global");
		controller = (NotepadController) Main.controllers.get(NotepadController.class.getSimpleName());

		// 读取系统字体
		GraphicsEnvironment graphicsEEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fontArray = FXCollections.observableArrayList(graphicsEEnvironment.getAvailableFontFamilyNames());
		// 初始化listView
		fontListView.setItems(fontArray);
		typefaceListView.setItems(typefaceArray);
		sizeListView.setItems(sizeArray);

		// textField和listView绑定
		fontTextField.textProperty().bind(fontListView.getSelectionModel().selectedItemProperty());
		typefaceTextField.textProperty().bind(typefaceListView.getSelectionModel().selectedItemProperty());
		sizeTextField.textProperty().bind(sizeListView.getSelectionModel().selectedItemProperty());
		// listView默认值
		fontListView.getSelectionModel().select("微软雅黑");
		typefaceListView.getSelectionModel().select(0);
		sizeListView.getSelectionModel().select(5);
		
		initFont();
		
		//listView监听
		fontListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

            	initFont();
            }
        });
		typefaceListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	initFont();
            }
        });
		sizeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	initFont();
            }
        });
        

	}

	public void fontStage(Stage stage) {
		this.stage = stage;
	}

	// 字体设置
	private void initFont() {
		int size = 15;
		for (int i = 0; i < sizeIntArray.length; i++) {
			// 字号
			if (sizeListView.getSelectionModel().getSelectedItem().equals(sizeArray.get(i))) {
				size = Integer.parseInt(String.valueOf(sizeIntArray[i]));
				break;
			}
		}
		if (typefaceListView.getSelectionModel().getSelectedItem().equals("常规")) {
			font = Font.font(fontListView.getSelectionModel().getSelectedItem(), FontWeight.NORMAL, FontPosture.REGULAR,
					size);
			global.fontWeight = FontWeight.NORMAL;
			global.fontPosture = FontPosture.REGULAR;
		} else if (typefaceListView.getSelectionModel().getSelectedItem().equals("粗体")) {
			font = Font.font(fontListView.getSelectionModel().getSelectedItem(), FontWeight.BOLD, FontPosture.REGULAR,
					size);
			global.fontWeight = FontWeight.BOLD;
			global.fontPosture = FontPosture.REGULAR;
		} else if (typefaceListView.getSelectionModel().getSelectedItem().equals("斜体")) {
			font = Font.font(fontListView.getSelectionModel().getSelectedItem(), FontWeight.NORMAL, FontPosture.ITALIC,
					size);
			global.fontWeight = FontWeight.NORMAL;
			global.fontPosture = FontPosture.ITALIC;
		}

		global.font = font;
		global.fontFamily = fontListView.getSelectionModel().getSelectedItem();
		global.size = size;
		textField.setFont(font);
		textField.setText(CH_STRING + "\n" + EN_STRING + "\n" + NUMBER_STRING);
		
	}

	@FXML
	void confirmButtonPressed(ActionEvent event) {
		controller.textFont();
		stage.close();
	}

	@FXML
	void cancelButtonPressed(ActionEvent event) {
		stage.close();
	}
}
