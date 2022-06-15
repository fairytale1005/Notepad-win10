package application;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NotepadController {
	boolean flagChange = false;// 记录文本是否有更改
	private String path = null;// 打开的文件的路径
	public Global global;
	Stage stage;

	// 字体
	private Font font;
	// 字体尺寸
	private double textSize;

	@FXML
	private TextArea textArea;
	@FXML
	private HBox statusBar;// 状态栏
	@FXML
	private Label RCLable;// 光标位置
	@FXML
	private Label zoomLable;// 缩放倍数

	// 文件(F)
	@FXML
	private MenuItem newMenultem;// 新建
	@FXML
	private MenuItem newWindowMenultem;// 新窗口
	@FXML
	private MenuItem openMenultem;// 打开
	@FXML
	private MenuItem saveMenultem;// 保存
	@FXML
	private MenuItem saveAsMenultem;// 另存为
	@FXML
	private MenuItem pageformatMenultem;// 页面设置
	@FXML
	private MenuItem exitMenultem;// 退出
	// 编辑(E)
	@FXML
	private MenuItem undoMenultem;// 撤销
	@FXML
	private MenuItem trimMenultem;// 剪切
	@FXML
	private MenuItem copyMenultem;// 复制
	@FXML
	private MenuItem pasteMenultem;// 粘贴
	@FXML
	private MenuItem delMenultem;// 删除
	@FXML
	private MenuItem findMenultem;// 查找
	@FXML
	private MenuItem findDownMenultm;// 查找下一个
	@FXML
	private MenuItem findUpMenultm;// 查找上一个
	@FXML
	private MenuItem replaceMenultm;// 替换
	@FXML
	private MenuItem turnToMenultem;// 转到
	@FXML
	private MenuItem selectAllMenultem;// 全选
	@FXML
	private MenuItem timeDateMenultem;// 时间日期
	// 格式(O)
	@FXML
	private MenuItem textFontMenultem;// 字体
	@FXML
	private CheckMenuItem autoWrapMenultem;// 自动换行
	// 查看(V)
	@FXML
	private MenuItem largeZoomMenultem;// 放大
	@FXML
	private MenuItem smallZoomMenultem;// 放小
	@FXML
	private MenuItem restoreDefaultZoomMenultem;// 重置
	@FXML
	private CheckMenuItem statusBarCheckMenuItem;// 状态栏
	// 帮助(H)
	@FXML
	private MenuItem helpMenuItem;// 查看帮助
	@FXML
	private MenuItem aboutMenultem;// 帮助

	public void initialize() {
		// 全局变量激活
		global = (Global) Main.globals.get("global");
		Main.controllers.put(this.getClass().getSimpleName(), this);
		textSize = global.size;
		path = null;
		global.path = null;
		// 初始状态下不可使用撤销、剪切、复制、删除、查找、转到功能
		// 灰度控制
		undoMenultem.setDisable(true);
		trimMenultem.setDisable(true);
		copyMenultem.setDisable(true);
		delMenultem.setDisable(true);
		findMenultem.setDisable(true);
		findDownMenultm.setDisable(true);
		findUpMenultm.setDisable(true);
		replaceMenultm.setDisable(true);
		turnToMenultem.setDisable(true);

		// 对textarea的内容是否改变进行监听
		textArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// 如果testarea中内容部位空,则可以使用查找与替换
				renewRCLable();
				if (textArea.getLength() > 0) {
					findMenultem.setDisable(false);
					undoMenultem.setDisable(false);
					trimMenultem.setDisable(false);
					copyMenultem.setDisable(false);
					delMenultem.setDisable(false);
					findMenultem.setDisable(false);
					findDownMenultm.setDisable(false);
					findUpMenultm.setDisable(false);
					replaceMenultm.setDisable(false);
					flagChange = true;// 文本更改

				} // 否则禁用查找与替换
				else {
					undoMenultem.setDisable(true);
					trimMenultem.setDisable(true);
					copyMenultem.setDisable(true);
					delMenultem.setDisable(true);
					findMenultem.setDisable(true);
					replaceMenultm.setDisable(true);
				} // else
			}
		});

		// 监听鼠标点击
		textArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				renewRCLable();
			}
		});
	}

	// 获取main中的stage
	public void mainStage(Stage stage) {
		this.stage = stage;
	}

	// 更新状态栏信息
	private void renewRCLable() {

		try {
			int line, row, FindStartPos;
			String strA, strB;
			String[] strings;
			strA = textArea.getText();
			FindStartPos = textArea.getCaretPosition();
			strB = strA.substring(0, FindStartPos);
			strB = strB + "0";
			strings = strB.split("\n");
			line = strings.length;
			// 有连续按下回车的情况
			if (line == 0) {
				strA = strA + "*";
				strB = strA.substring(0, FindStartPos + 1);
				strings = strB.split("\n");
				line = strings.length;
				row = strings[line - 1].length() - 1;
			} else {
				row = strings[line - 1].length();
			}
			RCLable.setText("第" + line + "行, 第" + row + "列" + " 共" + strA.length() + "字");
		} catch (Exception e) {

		}
	}

	// 文件-新建
	@FXML
	void newMenultemPressed(ActionEvent event) {
		if (flagChange) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("记事本");
			alert.setHeaderText(null);
			alert.setContentText("你想将更改保存到 " + stage.getTitle() + " 吗？");
			alert.getButtonTypes().add(ButtonType.CLOSE);

			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.CANCEL) {
				textArea.clear();
				stage.setTitle("无标题-记事本");
				path = null;
				global.path = null;
				flagChange = false;
			} else if (result.get() == ButtonType.OK) {
				if (path == null) saveAs();
				else save();
				
			}
			else return;
		} else {// 文件未被更改过
			textArea.clear();
			stage.setTitle("无标题-记事本");
			path = null;
			global.path = null;
			flagChange = false;
		}

	}

	// 文件-新窗口
	@FXML
	void newWindowMenultemPressed(ActionEvent event) {
		// 未实现
	}

	// 文件-打开
	@FXML
	void openMenultemPressed(ActionEvent event) {
		FileChooser chooser = new FileChooser(); // 创建一个文件对话框
		chooser.setTitle("打开"); // 设置文件对话框的标题
		chooser.setInitialDirectory(new File("C:\\")); // 设置文件对话框的初始目录
		// 给文件对话框添加文件类型的过滤器
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("所有文件", "*.*"));
		File file = chooser.showOpenDialog(stage); // 显示文件打开对话框
		if (file == null) { // 文件对象为空，表示没有选择任何文件
			Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建一个消息对话框
			alert.setTitle("打开文件");
			alert.setHeaderText("未选择任何文件");
			alert.setContentText("未打开新文件");
			alert.showAndWait(); // 模态显示对话框
		} else { // 文件对象非空，表示选择了某个文件

			path = file.getAbsolutePath();
			global.path = path;
			stage.setTitle(file.getName());
			BufferedReader br = null;
			try {
				// 建立文件流[字符流]
				InputStreamReader stream = new InputStreamReader(new FileInputStream(file), "UTF-8");
				br = new BufferedReader(stream);// 动态绑定
				// 读取内容
				StringBuffer sb = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line).append(System.getProperty("line.separator"));
					// 换行符
				}
				// 显示在文本框[多框]
				textArea.setText(sb.toString());
			} catch (IOException e) {
				// e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	// 文件-保存
	void save() {
		flagChange = false;
		if (path == null) {
			saveAs();
		} else {
			FileWriter fw = null;
			// 保存
			try {
				fw = new FileWriter(new File(path));
				fw.write(textArea.getText());
				fw.flush();
				stage.setTitle(path);
			} catch (IOException e) {
				// e.printStackTrace();
			} finally {
				try {
					if (fw != null)
						fw.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	@FXML
	void saveMenultemPressed(ActionEvent event) {
		save();
	}

	// 文件-另存为
	void saveAs() {
		// 打开保存框
		FileChooser chooser = new FileChooser(); // 创建一个文件对话框
		chooser.setTitle("保存文件"); // 设置文件对话框的标题
		chooser.setInitialDirectory(new File("C:\\")); // 设置文件对话框的初始目录
		// 创建一个文件类型过滤器
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("文本文件(*.txt)", "*.txt");
		// 给文件对话框添加文件类型过滤器
		chooser.getExtensionFilters().add(filter);
		File file = chooser.showSaveDialog(stage); // 显示文件保存对话框
		if (file == null) { // 文件对象为空，表示没有选择任何文件
			Alert alert = new Alert(Alert.AlertType.WARNING); // 创建一个消息对话框
			alert.setTitle("保存");
			alert.setHeaderText(null);
			alert.setContentText("未保存文件");
			alert.showAndWait(); // 模态显示对话框
		} else { // 文件对象非空，表示选择了某个文件

			FileWriter fw = null;
			// 保存
			try {
				fw = new FileWriter(file);
				fw.write(textArea.getText());
				path = file.getAbsolutePath();
				global.path = path;
				fw.flush();
				stage.setTitle(path);
			} catch (IOException e) {
				// e1.printStackTrace();
			} finally {
				try {
					if (fw != null)
						fw.close();
				} catch (IOException e) {
					// e1.printStackTrace();
				}
			}
		}
	}

	@FXML
	void saveAsMenultemPressed(ActionEvent event) {
		saveAs();
	}

	// 文件-页面设置
	@FXML
	private void pageformatMenultemPressed() {
		PageFormat pf = new PageFormat();
		PrinterJob.getPrinterJob().pageDialog(pf);
	}

	// 文件-打印
	@FXML
	void printMenultemPressed(ActionEvent event) {
		// 未实现

	}

	// 文件-退出
	@FXML
	void exitMenultemPressed(ActionEvent event) {
		if (flagChange) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("记事本");
			alert.setHeaderText(null);
			alert.setContentText("你想将更改保存到 " + stage.getTitle() + " 吗？");

			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				if (path == null) {
					saveAs();
				} else save();
			}
			else stage.close();
		}
		else stage.close();
		
	}

	// 编辑-撤销
	@FXML
	void undoMenultemPressed(ActionEvent event) {
		textArea.undo();
	}

	// 编辑-剪切
	@FXML
	void trimMenultemPressed(ActionEvent event) {
		textArea.cut();
	}

	// 编辑-复制
	@FXML
	void copyMenultemPressed(ActionEvent event) {
		textArea.copy();
	}

	// 编辑-粘贴
	@FXML
	void pasteMenultemPressed(ActionEvent event) {
		textArea.paste();
	}

	// 编辑-删除
	@FXML
	void delMenultemPressed(ActionEvent event) {
		textArea.replaceSelection("");
	}

	// 编辑-查找
	@FXML
	void findMenultemPressed(ActionEvent event) {
		Parent searchRoot = null;
		FXMLLoader fxmlLoeader = new FXMLLoader();
		try {

			fxmlLoeader.setLocation(getClass().getResource("Search.fxml"));
			searchRoot = fxmlLoeader.load();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		Scene searchScene = new Scene(searchRoot);
		Stage searchStage = new Stage();
		searchStage.setScene(searchScene);
		searchStage.setTitle("查找");
		searchStage.show(); // 显示窗口；
		searchStage.setResizable(false);

		SearchController searchController = fxmlLoeader.getController();
		searchController.searchStage(searchStage);

	}

	void searchDown() {

		int a = 0, b = 0;
		int FindStartPos = textArea.getCaretPosition();// 光标位置
		String strA, strB;
		strA = textArea.getText();// 全部文本
		strB = global.searchText;// 查找文本

		if (!global.caseSensitive) {
			strA = strA.toLowerCase();
			strB = strB.toLowerCase();
		}
		if (FindStartPos >= strA.length()) {
			FindStartPos = 0;
		}
		if (textArea.getSelectedText() == null) {
			a = strA.indexOf(strB, FindStartPos);
		} else {
			a = strA.indexOf(strB, FindStartPos - strB.length() + 1);
		}

		if (a > -1) {
			textArea.positionCaret(a);
			b = strB.length();
			textArea.selectRange(a, a + b);
		} else if ((a == -1 && !global.loop) || (!global.loop && FindStartPos == strA.length() - 1)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建一个消息对话框
			alert.setTitle("记事本");
			alert.setHeaderText(null);
			alert.setContentText("找不到\"" + strB + "\"");
			alert.showAndWait(); // 模态显示对话框
		} else {
			textArea.positionCaret(0);
		}
		findDownMenultm.setDisable(false);
	}

	void searchUp() {
		// 未实现
		findUpMenultm.setDisable(false);
	}

	// 编辑-查找下一个
	@FXML
	void findDownMenultmPressed(ActionEvent event) {
		searchDown();
	}

	// 编辑-查找上一个
	@FXML
	void findUpMenultmPressed(ActionEvent event) {
		// 未实现
		searchUp();
	}

	// 编辑-替换
	@FXML
	void replaceMenultmPressed(ActionEvent event) {
		Parent replaceRoot = null;
		FXMLLoader fxmlLoeader = new FXMLLoader();
		try {

			fxmlLoeader.setLocation(getClass().getResource("Replace.fxml"));
			replaceRoot = fxmlLoeader.load();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		Scene replaceScene = new Scene(replaceRoot);
		Stage replaceStage = new Stage();
		replaceStage.setScene(replaceScene);
		replaceStage.setTitle("替换");
		replaceStage.show(); // 显示窗口；
		replaceStage.setResizable(false);

		ReplaceController replaceController = fxmlLoeader.getController();
		replaceController.replaceStage(replaceStage);

	}

	void replace() {
		String replaceText = global.replaceText;
		searchDown();
		if (replaceText.isEmpty()) {
			textArea.replaceSelection("");
		} else
			textArea.replaceSelection(replaceText);

	}

	void replaceALL() {
		textArea.positionCaret(0); // 将光标放到编辑区开头
		int a = 0, b = 0, replaceCount = 0;

		while (a > -1) {
			int FindStartPos = textArea.getCaretPosition();
			String strA, strB, strC;
			strA = textArea.getText();
			strB = global.searchText;
			strC = global.replaceText;
			if (!global.caseSensitive) {
				strA = strA.toLowerCase();
				strB = strB.toLowerCase();
			}
			if (strB.length() == 0) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建一个消息对话框
				alert.setTitle("查询与替换");
				alert.setHeaderText("替换结果");
				alert.setContentText("请填写查找内容");
				alert.showAndWait(); // 模态显示对话框
				return;
			}
			if (textArea.getSelectedText() == null) {
				a = strA.indexOf(strB, FindStartPos);
			} else {
				a = strA.indexOf(strB, FindStartPos - strB.length() + 1);
			}
			if (a > -1) {
				textArea.positionCaret(a);
				b = strB.length();
				textArea.selectRange(a, a + b);
			} else {
				if (replaceCount == 0) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建一个消息对话框
					alert.setTitle("查询与替换");
					alert.setHeaderText("替换结果");
					alert.setContentText("找不到您查找的内容");
					alert.showAndWait(); // 模态显示对话框
				} else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建一个消息对话框
					alert.setTitle("查询与替换");
					alert.setHeaderText("替换结果");
					alert.setContentText("成功替换 " + replaceCount + "个匹配内容!");
					alert.showAndWait(); // 模态显示对话框
				}
			}
			if (strC.length() == 0 && textArea.getSelectedText() != null) {
				textArea.replaceSelection("");
				replaceCount++;
			}
			if (strC.length() > 0 && textArea.getSelectedText() != null) {
				textArea.replaceSelection(strC);
				replaceCount++;
			}
		}
	}

	// 编辑-转到
	@FXML
	void turnToMenultemPressed(ActionEvent event) {
		Parent turnToRoot = null;
		FXMLLoader fxmlLoeader = new FXMLLoader();
		try {

			fxmlLoeader.setLocation(getClass().getResource("TurnTo.fxml"));
			turnToRoot = fxmlLoeader.load();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		Scene turnToScene = new Scene(turnToRoot);
		Stage turnToStage = new Stage();
		turnToStage.setScene(turnToScene);
		turnToStage.setTitle("转到");
		turnToStage.show(); // 显示窗口；
		turnToStage.setResizable(false);

		TurnToController turnToController = fxmlLoeader.getController();
		turnToController.turnToStage(turnToStage);
	}

	void turnTo() {
		int turnTo = global.turnTo;
		int lineRow;
		int caretPosition = 0;
		String strA;
		String[] lines;
		strA = textArea.getText();
		lines = strA.split("\n");
		lineRow = lines.length;

		if (turnTo > lineRow) {
			Alert alert = new Alert(Alert.AlertType.ERROR); // 创建一个消息对话框
			alert.setTitle("转到");
			alert.setHeaderText(null);
			alert.setContentText("超出最大行数");
			alert.showAndWait(); // 模态显示对话框
			return;
		}
		for (int i = 0; i < turnTo - 1; i++)
			caretPosition += lines[i].length();
		textArea.positionCaret(caretPosition + turnTo - 1);
	}

	// 编辑-全选
	@FXML
	void selectAllMenultemPressed(ActionEvent event) {
		textArea.selectAll();
	}

	// 编辑-时间日期
	@FXML
	void timeDateMenultemPressed(ActionEvent event) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String date = df.format(new Date());
		textArea.appendText(date);
	}

	// 格式-自动换行
	@FXML
	void autoWrapMenultemPressed(ActionEvent event) {
		if (textArea.isWrapText()) {
			textArea.setWrapText(false);
			autoWrapMenultem.setSelected(false);
			turnToMenultem.setDisable(false);// 当自动换行开启时，转到功能不可用
		} else {
			autoWrapMenultem.setSelected(true);
			textArea.setWrapText(true);
			turnToMenultem.setDisable(true);
		}
	}

	// 格式-字体
	@FXML
	void textFontMenultemPressed(ActionEvent event) {
		Parent fontRoot = null;
		FXMLLoader fxmlLoeader = new FXMLLoader();
		try {

			fxmlLoeader.setLocation(getClass().getResource("Font.fxml"));
			fontRoot = fxmlLoeader.load();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		Scene fontScene = new Scene(fontRoot);
		Stage fontStage = new Stage();
		fontStage.setScene(fontScene);
		fontStage.setTitle("字体");
		fontStage.show(); // 显示窗口；
		fontStage.setResizable(false);

		FontController fontController = fxmlLoeader.getController();
		fontController.fontStage(fontStage);
	}

	void textFont() {
		font = global.font;
		textSize = global.size;
		// 设置字体
		textArea.setFont(font);
		zoomLable.setText("100%");
	}

	// 查看-缩放-放大
	@FXML
	void largeZoomMenultemPressed(ActionEvent event) {
		if (textSize < 72)
			textSize++;
		font = Font.font(global.fontFamily, global.fontWeight, global.fontPosture, textSize);
		textArea.setFont(font);
		zoomLable.setText(Math.round(textSize / global.size * 100) + "%");
	}

	// 查看-缩放-放小
	@FXML
	void smallZoomMenultemPressed(ActionEvent event) {
		if (textSize > 1)
			textSize--;
		font = Font.font(global.fontFamily, global.fontWeight, global.fontPosture, textSize);
		textArea.setFont(font);
		zoomLable.setText(Math.round(textSize / global.size * 100) + "%");
	}

	// 查看-缩放-恢复默认
	@FXML
	void restoreDefaultZoomMenultemPressed(ActionEvent event) {
		font = Font.font(global.fontFamily, global.fontWeight, global.fontPosture, global.size);
		textArea.setFont(font);
		zoomLable.setText("100%");
	}

	// 查看-状态栏
	@FXML
	void statusBarCheckMenuItemPressed(ActionEvent event) {
		if (statusBar.isVisible()) {
			statusBarCheckMenuItem.setSelected(false);
			statusBar.setVisible(false);

		} else {
			statusBarCheckMenuItem.setSelected(true);
			statusBar.setVisible(true);
		}
	}

	@FXML
	void helpMenuItemPressed(ActionEvent event) {
		java.net.URI uri = java.net.URI.create(
				"https://www.bing.com/search?q=%e8%8e%b7%e5%8f%96%e6%9c%89%e5%85%b3+windows&%e4%b8%ad%e7%9a%84%e8%ae%b0%e4%ba%8b%e6%9c%ac%e7%9a%84%e5%b8%ae%e5%8a%a9&filters=guid:%224466414-zh-hans-dia%22%20lang:%22zh-hans%22&form=T00032&ocid=HelpPane-BingIA");
		// 获取当前系统桌面扩展
		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
		// 判断系统桌面是否支持要执行的功能
		if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
			// 获取系统默认浏览器打开链接
			try {
				desktop.browse(uri);
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	@FXML
	void aboutMenuItemPressed(ActionEvent event) {
		Parent aboutRoot = null;
		FXMLLoader fxmlLoeader = new FXMLLoader();
		try {

			fxmlLoeader.setLocation(getClass().getResource("About.fxml"));
			aboutRoot = fxmlLoeader.load();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		Scene aboutScene = new Scene(aboutRoot);
		Stage aboutStage = new Stage();
		aboutStage.setScene(aboutScene);
		aboutStage.setTitle("关于\"记事本\"");
		aboutStage.show(); // 显示窗口；
		aboutStage.setResizable(false);

		AboutController aboutController = fxmlLoeader.getController();
		aboutController.aboutStage(aboutStage);
	}

}
