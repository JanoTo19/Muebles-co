package view;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;

public class BaseDatos extends JFrame {

	private Connection conn;

    public BaseDatos() {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda",
                    "root",
                    "");
        } catch (SQLException e) {
            System.out.println("Error de acceso a base de datos: " + e.getMessage());
        }
    }

//    public boolean loginDB(String user, String pass) {
//        boolean loginOK = false;
//
//
//        try {
//            // Consulta SQL para verificar el usuario y la contraseña en la base de datos
//            String query = "SELECT * FROM usuarios WHERE username = '"+user+"' AND password ='"+pass+"'";
//	        Statement stmt = (Statement) conn.createStatement();
//	        ResultSet rs = stmt.executeQuery(query);
//            if (rs.next()) {
//                // El usuario y la contraseña son válidos
//                loginOK = true;
//            }
//        } catch (SQLException e) {
//            System.out.println("Error al verificar el usuario y contraseña: " + e.getMessage());
//        } 
//
//        return loginOK;
//    }
//    public void cerrarBD() {
//    	
//    }
    public boolean loginDB(String username, String contraseñaCifrada) throws NoSuchAlgorithmException, SQLException {
        // Parámetros de conexión a la base de datos
        String bbdd = "proyectotienda"; // nombre de la base de datos
        String url = "jdbc:mariadb://localhost:3306/" + bbdd; // URL de la base de datos
        String usuarioBBDD = "root";
        String contraseñaBBDD = "";

        // Establecer conexión a la base de datos
        try (Connection conn = DriverManager.getConnection(url, usuarioBBDD, contraseñaBBDD)) {
            // Preparar la consulta SQL para obtener la contraseña cifrada del usuario
            String sql = "SELECT password FROM usuarios WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Obtener la contraseña cifrada de la base de datos
                        String contraseñaCifradaBD = rs.getString("password");
                        System.out.println("Contraseña cifrada en BD: "+contraseñaCifradaBD);
                        System.out.println("Contraseña cifrada entrante: "+contraseñaCifrada);
                        // Comparar la contraseña introducida con el hashalmacenado en la BD
                        return contraseñaCifradaBD.equals(contraseñaCifrada);
                    }
                }
            }
        }
        return false; // El usuario no existe en la base de datos
    }
}