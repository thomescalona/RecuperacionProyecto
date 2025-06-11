package MYSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/agenda_contactos";
    private static final String USUARIO = "root"; // cambia si usas otro usuario
    private static final String CONTRASENYA = "123456789";  // pon tu contraseña real

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENYA);
    }
}

