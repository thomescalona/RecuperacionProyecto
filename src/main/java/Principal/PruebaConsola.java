package Principal;
import MYSQL.ContactoDAO;
import POJOS.Contacto;
import java.util.List;

public class PruebaConsola {

    public static void main(String[] args) {
        ContactoDAO dao = new ContactoDAO();
        List<Contacto> contactos = dao.obtenerTodos();

        System.out.println("Lista de contactos en la base de datos:");

        for (Contacto c : contactos) {
            System.out.println("ID: " + c.getId());
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Apellido 1: " + c.getApellido1());
            System.out.println("Apellido 2: " + c.getApellido2());
            System.out.println("Teléfono: " + c.getTelefono());
            System.out.println("Email: " + c.getEmail());
            System.out.println("---------------------------");
        }
    }

}
