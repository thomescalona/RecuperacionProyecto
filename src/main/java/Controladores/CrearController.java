package Controladores;

import MYSQL.ContactoDAO;
import POJOS.Contacto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class CrearController {

    @FXML
    void setBtnAgregar(ActionEvent event) {

            String nombre = textFieldNombre.getText();
            String apellido1 = textFieldApellido1.getText();
            String apellido2 = textFieldApellido2.getText();
            String telefono = textFieldTelefono.getText();
            String email = textFieldEmail.getText();

        if (nombre.isEmpty() || apellido1.isEmpty() || telefono.isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Rellena todos los campos obligatorios.");
            return;
        }

        if (!telefono.matches("\\d{9}")) {
            mostrarAlerta("Teléfono inválido", "El teléfono debe tener 9 dígitos.");
            return;
        }

        if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mostrarAlerta("Email inválido", "Introduce un email válido o deja el campo vacío.");
            return;
        }


        Contacto contacto = new Contacto();
                contacto.setNombre(nombre);
                contacto.setApellido1(apellido1);
                contacto.setApellido2(apellido2);
                contacto.setTelefono(telefono);
                contacto.setEmail(email);

            ContactoDAO dao = new ContactoDAO();
            dao.insertar(contacto);

            labelEstado.setText("Contacto agregado correctamente.");

        textFieldNombre.clear();
        textFieldApellido1.clear();
        textFieldApellido2.clear();
        textFieldTelefono.clear();
        textFieldEmail.clear();
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    public void setBtnSalir(ActionEvent actionEvent) {
        try {
            // Cargar nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/home.fxml"));
            Parent root = loader.load();

            // Crear nueva ventana
            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Home");
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.show();

            // Cerrar la ventana actual
            Stage ventanaActual = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTextFieldNombre(ActionEvent actionEvent) {}

    public void setTextFieldApellido1(ActionEvent actionEvent) {}

    public void setTextFieldApellido2(ActionEvent actionEvent) {}

    public void setTextFieldTelefono(ActionEvent actionEvent) {}

    public void setTextFieldEmail(ActionEvent actionEvent) {}
    @FXML
    private Label labelEstado;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnSalir;
    @FXML
    private TextField textFieldApellido1;
    @FXML
    private TextField textFieldApellido2;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    void SetTextFieldApellido1(ActionEvent event) {}

    @FXML
    void SetTextFieldApellido2(ActionEvent event) {}

    @FXML
    void SetTextFieldEmail(ActionEvent event) {}

    @FXML
    void SetTextFieldNombre(ActionEvent event) {}

    @FXML
    void SetTextFieldTelefono(ActionEvent event) {}

}

