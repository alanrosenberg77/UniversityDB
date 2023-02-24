module cs330.arosenberg {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires c3p0;
	requires javafx.graphics;

    opens cs330.arosenberg to javafx.fxml;
    exports cs330.arosenberg;
}
