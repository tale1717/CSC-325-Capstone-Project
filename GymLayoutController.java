import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GymLayoutController {

    @FXML
    private Button myButton;

    @FXML
    private void showMachineStatus() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Machine Status");
        alert.setHeaderText(null);
        alert.setContextText("The machine is currently free!");
        alert.showAndWait();
    }
}
