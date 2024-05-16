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

public class BaseDatos {

	private static Properties properties = new Properties();
	private static Connection conn;

	public static Connection getConnection() {
		try (FileInputStream fis = new FileInputStream("./src/files/config.properties")) {
			properties.load(fis);
			conn = DriverManager.getConnection(properties.getProperty("jdbc") + properties.getProperty("BBDD"),properties.getProperty("USER"),properties.getProperty("PASS"));
		} catch (SQLException | IOException ex) {
			JOptionPane.showMessageDialog(null, ex instanceof SQLException
					? "Error de acceso a base de datos: " + ex.getMessage()
					: ex instanceof FileNotFoundException ? "Error no se encuentra el archivo " + ex.getMessage()
							: "Error no se puede leer el archivo " + ex.getMessage(),
					"Error!!", JOptionPane.ERROR_MESSAGE);
		}
		return conn;
	}

	public static void RecuperarContraseña() {
		
		String usuario = JOptionPane.showInputDialog(null, "Introduce el nombre de usuario", "Recuperar Contraseña", JOptionPane.INFORMATION_MESSAGE);
		
		try {
			String sql = "SELECT password FROM usuarios WHERE username=?";
			try(Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, usuario);
				try(ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						String passwordCifrada = rs.getString("password");
						String passwordDesencriptada = Login.desencriptar(passwordCifrada);
						JOptionPane.showMessageDialog(null, "La contraseña es " + passwordDesencriptada, "Recuperacion de contraseña", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Usuario no encontrado.", "Error!!", JOptionPane.INFORMATION_MESSAGE);
					}
				} 
				
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al recuperar la contraseña: " + ex.getMessage());
		}
	}

	public boolean loginDB(String username, String contraseñaCifrada) throws NoSuchAlgorithmException, SQLException {

		// Establecer conexión a la base de datos
		try (Connection conn = getConnection()) {
			// Preparar la consulta SQL para obtener la contraseña cifrada del usuario
			String sql = "SELECT password FROM usuarios WHERE username = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, username);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						// Obtener la contraseña cifrada de la base de datos
						String contraseñaCifradaBD = rs.getString("password");
						// Comparar la contraseña introducida con el hashalmacenado en la BD
						return contraseñaCifradaBD.equals(contraseñaCifrada);
					}
				}
			}
		}
		return false; // El usuario no existe en la base de datos
	}
}
