package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import view.MenuPrincipal;

public class BaseDatos {
	private static Connection conn;
	private static Properties properties = new Properties();

	private static Connection getConnection() throws SQLException {

		try (FileInputStream fis = new FileInputStream("./src/files/config.properties")) {
			properties.load(fis);

			if (conn == null || conn.isClosed()) {
				conn = DriverManager.getConnection(properties.getProperty("jdbc") + properties.getProperty("BBDD"), properties.getProperty("USER"), properties.getProperty("PASS"));
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error no se puede leer el archivo", "Error!!", JOptionPane.ERROR_MESSAGE);
		}
		return conn;
	}

	public boolean loginDB(String username, String contraseñaCifrada) throws NoSuchAlgorithmException, SQLException {
		String sql = "SELECT password FROM usuarios WHERE username = ?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String contraseñaCifradaBD = rs.getString("password");
					return contraseñaCifradaBD.equals(contraseñaCifrada);
				}
			}
		}
		return false; // El usuario no existe en la base de datos
	}

	public static void obtenerProductos(DefaultTableModel modeloTabla) {
		String sql = "SELECT nombre, tipo, gama, cantidad, precio FROM productos";
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				String nombre = rs.getString("nombre");
				String tipo = rs.getString("tipo");
				String gama = rs.getString("gama");
				int cantidad = rs.getInt("cantidad");
				double precio = rs.getDouble("precio");
				Object[] fila = { nombre, tipo, gama, cantidad, precio };
				modeloTabla.addRow(fila);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al obtener los datos de la base de datos.");
			e.printStackTrace();
		}
	}

	public static void altaUsuario(String username, String password, String confirmar) {
		if (username.isBlank() || password.isBlank() || confirmar.isBlank()) {
			JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
			return;
		}
		if (!password.equals(confirmar)) {
			JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
			return;
		}

		try {
			BaseDatos bbdd = new BaseDatos();
			String contraseñaCifrada = Cifrado.encriptar(password);
			if (!bbdd.loginDB(username, contraseñaCifrada)) {
				String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
				try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
					pstmt.setString(1, username);
					pstmt.setString(2, contraseñaCifrada);
					pstmt.executeUpdate();
					System.out.println("Usuario registrado correctamente.");
					MenuPrincipal ventanaPrincipal = new MenuPrincipal(username);
					ventanaPrincipal.setVisible(true);
				}
			} else {
				JOptionPane.showMessageDialog(null, "El usuario ya existe.");
			}
		} catch (NoSuchAlgorithmException | SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al registrar el usuario.");
		}
	}

	public static void altaProducto(String nombre, String tipo, String gama, int cantidad, double precio) {
		String sql = "INSERT INTO productos (nombre, tipo, gama, cantidad, precio) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, nombre);
			pstmt.setString(2, tipo);
			pstmt.setString(3, gama);
			pstmt.setInt(4, cantidad);
			pstmt.setDouble(5, precio);
			pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "Producto registrado correctamente.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar el producto en la base de datos.");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Error al parsear un numero.");
		}
	}

	public void recuperarUser(String user) {
		String sql = "SELECT password FROM usuarios WHERE username=?";
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String passwordCifrada = rs.getString("password");
					String passwordDesencriptada = Cifrado.desencriptar(passwordCifrada);
					JOptionPane.showMessageDialog(null, "La contraseña es " + passwordDesencriptada);
				} else {
					JOptionPane.showMessageDialog(null, "Usuario no encontrado.");
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al recuperar la contraseña: " + e.getMessage());
		}
	}
}
