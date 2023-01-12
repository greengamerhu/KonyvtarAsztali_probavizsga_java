module hu.petrik.konyvtarasztali {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.petrik.konyvtarasztali to javafx.fxml;
    exports hu.petrik.konyvtarasztali;
}