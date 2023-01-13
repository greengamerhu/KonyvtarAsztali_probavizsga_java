module hu.petrik.konyvtarasztali {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens hu.petrik.konyvtarasztali to javafx.fxml;
    exports hu.petrik.konyvtarasztali;
}