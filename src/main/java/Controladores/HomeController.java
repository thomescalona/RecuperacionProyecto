package Controladores;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import MYSQL.ContactoDAO;
import POJOS.Contacto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


public class HomeController {

    @FXML
    public void initialize() {
        // 1. Configurar columnas
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellido1.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        columnaApellido2.setCellValueFactory(new PropertyValueFactory<>("apellido2"));

        // 2. Configurar qué pasa al seleccionar un contacto
        tablaContacto.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Contacto>() {
                    @Override
                    public void changed(ObservableValue<? extends Contacto> observable, Contacto oldValue, Contacto newValue) {
                        if (newValue != null) {
                            labelNombre.setText(newValue.getNombre());
                            labelApellido1.setText(newValue.getApellido1());
                            labelApellido2.setText(newValue.getApellido2());
                            labelTelefono.setText(newValue.getTelefono());
                            labelEmail.setText(newValue.getEmail());
                        }
                    }
                }
        );

        // 3. Cargar contactos
        cargarContactos();
    }


    private void cargarContactos() {
        ContactoDAO dao = new ContactoDAO();
        List<Contacto> lista = dao.obtenerTodos();
        tablaContacto.getItems().setAll(lista);
    }

    @FXML
    void btnAgregarClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/crearContacto.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Crar un Contacto");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ventanaActual.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnModificarClick(ActionEvent event) {
        Contacto contactoSeleccionado = tablaContacto.getSelectionModel().getSelectedItem();

        if (contactoSeleccionado != null) {
            try {
                // Cargar el FXML de la ventana de edición (tu editarContacto.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/editarContacto.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la nueva ventana (EditarController)
                Controladores.EditarController controller = loader.getController();
                // PASAR EL CONTACTO SELECCIONADO al controlador de la ventana de edición
                controller.setContacto(contactoSeleccionado);

                // Configurar y mostrar la nueva ventana
                Stage stage = new Stage();
                stage.setTitle("Modificar Contacto");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // Hace que la nueva ventana bloquee la principal
                stage.showAndWait(); // Espera a que la ventana de edición se cierre

                // Cuando la ventana de edición se cierra, recargamos la tabla principal
                cargarContactos(); // Para ver los cambios si se guardaron
                tablaContacto.getSelectionModel().clearSelection(); // Deseleccionar en la tabla principal
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) { // Captura cualquier otra excepción
                e.printStackTrace();
            }
        }

    }




    // *** NUEVO MÉTODO para la búsqueda por teléfono ***
    @FXML
    void btnBuscarClick(ActionEvent event) {
        String textoBusqueda = textFieldNombre.getText(); // Obtenemos el texto del campo de búsqueda

        ContactoDAO dao = new ContactoDAO(); // Creamos una instancia de tu DAO

        List<Contacto> resultados;

        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            // Si el campo de búsqueda está vacío, cargamos todos los contactos
            resultados = dao.obtenerTodos();
        } else {
            // Si hay texto, usamos el método de búsqueda de tu DAO
            resultados = dao.buscarNombre(textoBusqueda.trim());
        }

        // Limpiamos la tabla y añadimos los resultados (sea la lista completa o los de la búsqueda)
        tablaContacto.getItems().setAll(resultados);


    }

    @FXML
    public void btnEliminarClick(ActionEvent actionEvent) {

        Contacto contactoSeleccionado = tablaContacto.getSelectionModel().getSelectedItem();

        ContactoDAO dao = new ContactoDAO();


        if (contactoSeleccionado != null) {
            // --- CÓDIGO CLAVE PARA LA ALERTA DE CONFIRMACIÓN ---

            // 2. Crear una alerta de confirmación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación"); // Título de la ventana de alerta
            confirmacion.setHeaderText("¿Estás seguro de que quieres eliminar este contacto?"); // Encabezado (texto más grande)
            // Contenido de la alerta, mostrando detalles del contacto
            confirmacion.setContentText("Contacto: " + contactoSeleccionado.getNombre() + " " + contactoSeleccionado.getApellido1() + " (" + contactoSeleccionado.getTelefono() + ")");

            // 3. Mostrar la alerta y esperar la respuesta del usuario
            Optional<ButtonType> resultado = confirmacion.showAndWait();

            // 4. Verificar la respuesta del usuario
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Si el usuario hizo clic en "OK" (o el botón de confirmación)
                try {
                    // Llamar al método eliminar de tu DAO para borrar de la base de datos
                    dao.eliminar(contactoSeleccionado.getId());

                    // 5. Actualizar la tabla para que el contacto eliminado desaparezca visualmente
                    cargarContactos(); // Esto recarga la tabla con los datos actualizados desde la DB
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setTextFieldNombre(ActionEvent actionEvent) {

    }

    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnModificar;
    @FXML
    private TableColumn<Contacto, String> columnaApellido1;
    @FXML
    private TableColumn<Contacto, String> columnaApellido2;
    @FXML
    private TableColumn<Contacto, String> columnaNombre;
    @FXML
    private TableView<Contacto> tablaContacto;
    @FXML
    private Label labelApellido2;
    @FXML
    private Label labelEmail;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelTelefono;
    @FXML
    private Label labelApellido1;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField textFieldNombre;
}

