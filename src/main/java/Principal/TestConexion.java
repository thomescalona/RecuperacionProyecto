package Principal;

import MYSQL.DBUtil;

import java.sql.Connection;

public class  TestConexion {
    public static void main(String[] args) {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.println("✅ Conexión correcta a la base de datos.");
        } catch (Exception e) {
            System.out.println("❌ Error de conexión:");
            e.printStackTrace();
        }
    }
}
