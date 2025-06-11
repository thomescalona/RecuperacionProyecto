package MYSQL;

import POJOS.Contacto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactoDAO {

    public List<Contacto> obtenerTodos() {
        List<Contacto> lista = new ArrayList<>();
        String sql = "SELECT * FROM contacto";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Contacto c = new Contacto();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido1(rs.getString("apellido1"));
                c.setApellido2(rs.getString("apellido2"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Contacto> obtenercolumnas() {
        List<Contacto> lista = new ArrayList<>();
        String sql = "SELECT c.nombre,c.apellido1,c.apellido2 FROM contacto";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Contacto c = new Contacto();
                c.setNombre(rs.getString("nombre"));
                c.setApellido1(rs.getString("apellido1"));
                c.setApellido2(rs.getString("apellido2"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void insertar(Contacto contacto) {
        String sql = "INSERT INTO contacto (nombre, apellido1, apellido2, telefono, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contacto.getNombre());
            stmt.setString(2, contacto.getApellido1());
            stmt.setString(3, contacto.getApellido2());
            stmt.setString(4, contacto.getTelefono());
            stmt.setString(5, contacto.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // --- NUEVA FUNCIÓN: Buscar Contactos por Teléfono ---
    public List<Contacto> buscarNombre(String nombreBuscado) {
        List<Contacto> contactosEncontrados = new ArrayList<>();
        // Usamos LIKE para buscar coincidencias parciales del número de teléfono
        // y concatenamos '%' para que busque el texto en cualquier parte del campo.
        String sql = "SELECT id, nombre, apellido1, apellido2, telefono, email FROM contacto WHERE nombre LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Establecemos el parámetro para la consulta SQL.
            // Los '%' permiten que la búsqueda sea "contiene" en lugar de "es exactamente igual".
            pstmt.setString(1, "%" + nombreBuscado + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Contacto contacto = new Contacto();
                    contacto.setId(rs.getInt("id"));
                    contacto.setNombre(rs.getString("nombre"));
                    contacto.setApellido1(rs.getString("apellido1"));
                    contacto.setApellido2(rs.getString("apellido2"));
                    contacto.setTelefono(rs.getString("telefono"));
                    contacto.setEmail(rs.getString("email"));
                    contactosEncontrados.add(contacto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contactosEncontrados;
    }

    public void eliminar(int idContacto) {

        String sql = "DELETE FROM contacto WHERE id = ?";

        try (
                Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)   )
        {
            pstmt.setInt(1, idContacto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void actualizar(Contacto contacto) throws SQLException { // Añadimos 'throws SQLException'
        String sql = "UPDATE contacto SET nombre = ?, apellido1 = ?, apellido2 = ?, telefono = ?, email = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); // Usamos tu DBUtil
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, contacto.getNombre());
            pstmt.setString(2, contacto.getApellido1());
            pstmt.setString(3, contacto.getApellido2());
            pstmt.setString(4, contacto.getTelefono());
            pstmt.setString(5, contacto.getEmail());
            pstmt.setInt(6, contacto.getId()); // ¡El ID es crucial para saber qué fila actualizar!

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Contacto con ID " + contacto.getId() + " actualizado correctamente.");
            } else {
                System.out.println("No se encontró contacto con ID " + contacto.getId() + " para actualizar.");
            }
        }
    }
}
