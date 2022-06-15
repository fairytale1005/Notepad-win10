package application;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	// 全局容器
	public static Map<String, Object> controllers = new HashMap<String, Object>();
	public static Map<String, Object> globals = new HashMap<String, Object>();
	// 全局变量实例化
	Global global = new Global();
	public NotepadController controller;

	@Override
	public void start(Stage stage) throws Exception {
		globals.put("global", global);

		FXMLLoader fxmlLoeader = new FXMLLoader();
		fxmlLoeader.setLocation(getClass().getResource("Notepad.fxml"));
		Parent root = fxmlLoeader.load();
		Scene scene = new Scene(root);
		stage.setTitle("-记事本");
		stage.setScene(scene);
		stage.getIcons().add(new Image("icon.png"));
		stage.show();

		// 获取NotepadController的实例对象
		NotepadController notepadController = fxmlLoeader.getController();
		notepadController.mainStage(stage);

		global = (Global) Main.globals.get("global");
		controller = (NotepadController) Main.controllers.get(NotepadController.class.getSimpleName());

		// 监听窗口关闭事件
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("记事本");
				alert.setHeaderText(null);
				alert.setContentText("你想将更改保存到 " + stage.getTitle() + " 吗？");

				Optional<ButtonType> result = alert.showAndWait();

				if (result.get() == ButtonType.OK) {
					if (global.path == null) {
						controller.saveAs();
					} else
						controller.save();
				} else
					stage.close();
			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}
}
