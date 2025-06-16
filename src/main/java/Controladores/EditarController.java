package Controladores;

import MYSQL.ContactoDAO;
import POJOS.Contacto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

public class EditarController {

        //Método para recibir el contacto desde HomeController ---
        private Contacto contactoAEditar; // Para guardar el contacto que vamos a modificar
        private ContactoDAO contactoDAO; // Instancia del DAO

        public void setContacto(POJOS.Contacto contacto) {
                this.contactoAEditar = contacto; // Guardamos el contacto recibido (a editar)
        }

        private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
                Alert alerta = new Alert(tipo); // Creamos la alerta
                alerta.setTitle(titulo);
                alerta.setHeaderText(encabezado);
                alerta.setContentText(contenido);
                alerta.showAndWait(); // La mostramos y esperamos respuesta
        }

        @FXML
        public void cargarDatosEnPantalla(){
                // Si no es null, rellenamos los campos del formulario con los datos actuales
                if (contactoAEditar != null) {
                        textFieldNombre.setText(contactoAEditar.getNombre());
                        textFieldApellido1.setText(contactoAEditar.getApellido1());

                        // Para evitar que se muestre "null", se comprueba si los campos opcionales están vacíos
                        textFieldApellido2.setText(contactoAEditar.getApellido2() != null ? contactoAEditar.getApellido2() : "");
                        textFieldTelefono.setText(contactoAEditar.getTelefono());
                        textFieldEmail.setText(contactoAEditar.getEmail() != null ? contactoAEditar.getEmail() : "");
                }

        }

        @FXML
        public void initialize() {
                contactoDAO = new MYSQL.ContactoDAO(); // Creamos el DAO para trabajar con la base de datos
                // Los datos del contacto se reciben después, por eso no se hace nada más aquí
        }

        @FXML
        void setBtnSalir(ActionEvent event) {
                cerrarVentana(); // Cierra la ventana sin guardar nada
        }





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
                                // Si no había contacto, lo estamos creando desde cero
                                Contacto nuevoContacto = new Contacto(
                                        nuevoNombre,
                                        nuevoApellido1,
                                        nuevoApellido2.isEmpty() ? null : nuevoApellido2,
                                        nuevoTelefono,
                                        nuevoEmail.isEmpty() ? null : nuevoEmail
                                );
                                contactoDAO.insertar(nuevoContacto); // INSERT en base de datos
                                mostrarAlerta(Alert.AlertType.INFORMATION, "Contacto añadido", "Se ha añadido correctamente el nuevo contacto.", null);
                        } else {
                                // Ya había contacto, así que lo actualizamos
                                contactoAEditar.setNombre(nuevoNombre);
                                contactoAEditar.setApellido1(nuevoApellido1);
                                contactoAEditar.setApellido2(nuevoApellido2.isEmpty() ? null : nuevoApellido2);
                                contactoAEditar.setTelefono(nuevoTelefono);
                                contactoAEditar.setEmail(nuevoEmail.isEmpty() ? null : nuevoEmail);

                                contactoDAO.actualizar(contactoAEditar); // UPDATE en base de datos
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

