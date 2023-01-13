package hu.petrik.konyvtarasztali;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.security.AllPermission;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class KonyvController {
    private KonyvDB db;
    @FXML
    private Button torlesButton;
    @FXML
    private TableView<Konyv> bookTable;
    @FXML
    private TableColumn<Konyv, String> titleCol;
    @FXML
    private TableColumn<Konyv, String> authorCol;
    @FXML
    private TableColumn<Konyv, Integer> pyearCol;
    @FXML
    private TableColumn<Konyv, Integer> pagesCol;


    public void initialize() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        pyearCol.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        pagesCol.setCellValueFactory(new PropertyValueFactory<>("page_count"));
        Platform.runLater(() -> {
            try {
                db = new KonyvDB();
                konyvDisplay();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Adatbázis Hiba");
                alert.setContentText(e.toString());
                alert.showAndWait();

                Platform.exit();
            }
        });
    }

    private void konyvDisplay() throws SQLException {
        List<Konyv> konyvek = db.Konyvlistaz();
        bookTable.getItems().clear();
        bookTable.getItems().addAll(konyvek);
    }

    @FXML
    public void deleteButtonClick(ActionEvent actionEvent) {
        Konyv kivalsztottKonyv = bookTable.getSelectionModel().getSelectedItem();
        if (kivalsztottKonyv == null) {
            new Alert(Alert.AlertType.INFORMATION, "Törléshez előbb válasszon ki könyvet”").showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Figyelmeztetés");
            alert.setContentText("Biztos szeretné törölni a kiválasztott könyvet? " + kivalsztottKonyv.getTitle());
            Optional<ButtonType> userChoice = alert.showAndWait();
            if (userChoice.isEmpty() || (!userChoice.get().equals(ButtonType.OK) &&
                    !userChoice.get().equals(ButtonType.YES))) {
                return;
            }

            try {
                if (db.konyvTorlese(kivalsztottKonyv)) {
                    new Alert(Alert.AlertType.WARNING, "Sikeres Törlés").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Sikertelen Törlés").showAndWait();
                }
                konyvDisplay();
            } catch (SQLException e) {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Adatbázis Hiba");
                alert.setContentText(e.toString());
                alert.showAndWait();
                Platform.exit();
            }
        }
        }
    }
