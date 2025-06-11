package Controladores;

import POJOS.Contacto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

public class EditarController {
        // --- Métodos utilitarios ---
        // Método para cerrar la ventana actual

        public void setContacto(POJOS.Contacto contacto) {
                this.contactoAEditar = contacto; // Guardamos el contacto recibido
                // Cargamos los datos del contacto en los TextFields de la ventana
                if (contactoAEditar != null) {
                        textFieldNombre.setText(contactoAEditar.getNombre());
                        textFieldApellido1.setText(contactoAEditar.getApellido1());
                        // Para los campos opcionales, como apellido2 y email, asegurarnos de que no pongan "null"
                        textFieldApellido2.setText(contactoAEditar.getApellido2() != null ? contactoAEditar.getApellido2() : "");
                        textFieldTelefono.setText(contactoAEditar.getTelefono());
                        textFieldEmail.setText(contactoAEditar.getEmail() != null ? contactoAEditar.getEmail() : "");
                }
        }

        // Método auxiliar para mostrar alertas al usuario
        private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
                Alert alerta = new Alert(tipo);
                alerta.setTitle(titulo);
                alerta.setHeaderText(encabezado);
                alerta.setContentText(contenido);
                alerta.showAndWait();
        }

        private MYSQL.ContactoDAO contactoDAO; // Instancia de tu DAO
        @FXML
        public void initialize() {
                contactoDAO = new MYSQL.ContactoDAO(); // Inicializamos el DAO
                // Aquí no cargamos los datos aún, HomeController nos los pasará.
        }

        @FXML
        void setBtnSalir(ActionEvent event) { // Este método es tu onAction para el botón "Cancelar"
                cerrarVentana(); // Simplemente cierra la ventana sin guardar cambios
        }


        // --- Método para recibir el contacto desde HomeController ---
        private POJOS.Contacto contactoAEditar; // Para guardar el contacto que vamos a modificar

        @FXML
        void setBtnAgregar(ActionEvent event) {
                // 1. Recoger los datos de los TextFields
                String nuevoNombre = textFieldNombre.getText().trim();
                String nuevoApellido1 = textFieldApellido1.getText().trim();
                String nuevoApellido2 = textFieldApellido2.getText().trim();
                String nuevoTelefono = textFieldTelefono.getText().trim();
                String nuevoEmail = textFieldEmail.getText().trim();

                // 2. Validaciones
                if (nuevoNombre.isEmpty() || nuevoApellido1.isEmpty() || nuevoTelefono.isEmpty()) {
                        mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Datos Requeridos", "Nombre, Primer Apellido y Teléfono no pueden estar vacíos.");
                        return;
                }

                if (!nuevoTelefono.matches("\\d{9}")) {
                        mostrarAlerta(Alert.AlertType.WARNING, "Teléfono inválido", "Debe tener 9 dígitos.", null);
                        return;
                }

                if (!nuevoEmail.isEmpty() && !nuevoEmail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                        mostrarAlerta(Alert.AlertType.WARNING, "Email inválido", "Introduce un email válido o deja el campo vacío.", null);
                        return;
                }

                try {
                        if (contactoAEditar == null) {
                                // Caso: Añadir nuevo contacto
                                Contacto nuevoContacto = new Contacto(
                                        nuevoNombre,
                                        nuevoApellido1,
                                        nuevoApellido2.isEmpty() ? null : nuevoApellido2,
                                        nuevoTelefono,
                                        nuevoEmail.isEmpty() ? null : nuevoEmail
                                );
                                contactoDAO.insertar(nuevoContacto);
                                mostrarAlerta(Alert.AlertType.INFORMATION, "Contacto añadido", "Se ha añadido correctamente el nuevo contacto.", null);
                        } else {
                                // Caso: Editar contacto existente
                                contactoAEditar.setNombre(nuevoNombre);
                                contactoAEditar.setApellido1(nuevoApellido1);
                                contactoAEditar.setApellido2(nuevoApellido2.isEmpty() ? null : nuevoApellido2);
                                contactoAEditar.setTelefono(nuevoTelefono);
                                contactoAEditar.setEmail(nuevoEmail.isEmpty() ? null : nuevoEmail);

                                contactoDAO.actualizar(contactoAEditar);
                                mostrarAlerta(Alert.AlertType.INFORMATION, "Contacto actualizado", "Los cambios se han guardado correctamente.", null);
                        }

                        cerrarVentana();

                } catch (SQLException e) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar", "No se pudo guardar el contacto en la base de datos.", e.getMessage());
                        e.printStackTrace();
                }
        }

        private void cerrarVentana() {
                Stage stage = (Stage) textFieldNombre.getScene().getWindow(); // Obtiene la ventana
                stage.close(); // Cierra la ventana
        }

        @FXML
        void setTextFieldApellido1(ActionEvent event) {}
        @FXML
        void setTextFieldApellido2(ActionEvent event) {}
        @FXML
        void setTextFieldEmail(ActionEvent event) {}
        @FXML
        void setTextFieldNombre(ActionEvent event) {}
        @FXML
        void setTextFieldTelefono(ActionEvent event) {}

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
}

