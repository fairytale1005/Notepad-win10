module BigProjectFX {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires java.desktop;
	requires javafx.graphics;
	requires java.logging;
	
	opens application to javafx.graphics, javafx.fxml;
	
}
