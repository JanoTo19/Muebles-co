package miApp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Clase que maneja la conexión a la base de datos y proporciona métodos para recuperar contraseñas y verificar login.
 */
public class BaseDatos {

    private static Properties properties = new Properties();
    private static Connection conn;

    /**
     * Obtiene una conexión a la base de datos utilizando los parámetros definidos en el archivo de configuración.
     * @return La conexión a la base de datos.
     */
    public static Connection getConnection() {
        try (FileInputStream fis = new FileInputStream("./src/files/config.properties")) {
            properties.load(fis);
            conn = DriverManager.getConnection(
                    properties.getProperty("jdbc") + properties.getProperty("BBDD"),
                    properties.getProperty("USER"),
                    properties.getProperty("PASS")
            );
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(null, ex instanceof SQLException
                            ? "Error de acceso a base de datos: " + ex.getMessage()
                            : ex instanceof FileNotFoundException ? "Error no se encuentra el archivo " + ex.getMessage()
                            : "Error no se puede leer el archivo " + ex.getMessage(),
                    "Error!!", JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    }

    /**
     * Método para recuperar la contraseña de un usuario.
     * Solicita el nombre de usuario y muestra la contraseña desencriptada en un mensaje de diálogo.
     */
    public static void RecuperarContraseña() {
        String usuario = JOptionPane.showInputDialog(null, "Introduce el nombre de usuario", "Recuperar Contraseña",
                JOptionPane.INFORMATION_MESSAGE);

        try {
            String sql = "SELECT username,password FROM usuarios WHERE username=?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, usuario);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        if (usuario.equals(rs.getString("username"))) {
                            String passwordCifrada = rs.getString("password");
                            String passwordDesencriptada = Login.desencriptar(passwordCifrada);
                            JOptionPane.showMessageDialog(null, "La contraseña es " + passwordDesencriptada,
                                    "Recuperación de contraseña", JOptionPane.INFORMATION_MESSAGE);
                            
                        } else {
                            JOptionPane.showMessageDialog(null, "Usuario mal escrito", "Error!!", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        if (usuario != null && !usuario.isBlank()) {
                            JOptionPane.showMessageDialog(null, "Usuario no encontrado.", "Error!!",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al recuperar la contraseña: " + ex.getMessage());
        }
    }

    /**
     * Método para verificar el login de un usuario.
     * Compara la contraseña cifrada proporcionada con la almacenada en la base de datos.
     * @param username El nombre de usuario.
     * @param contraseñaCifrada La contraseña cifrada proporcionada.
     * @return true si la contraseña coincide, false en caso contrario.
     * @throws NoSuchAlgorithmException Si hay un problema con el algoritmo de cifrado.
     * @throws SQLException Si hay un problema al acceder a la base de datos.
     */
    public boolean loginDB(String username, String contraseñaCifrada) throws NoSuchAlgorithmException, SQLException {
        try (Connection conn = getConnection()) {
            String sql = "SELECT password FROM usuarios WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String contraseñaCifradaBD = rs.getString("password");
                        return contraseñaCifradaBD.equals(contraseñaCifrada);
                    }
                }
            }
        }
        return false; // El usuario no existe en la base de datos
    }
}

