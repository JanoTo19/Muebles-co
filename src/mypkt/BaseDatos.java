package mypkt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class BaseDatos {

	private Properties properties = new Properties();
	private Connection conn;

	public BaseDatos() {
		try (FileInputStream fis = new FileInputStream("./src/archivos/config.properties")) {

			properties.load(fis);

			conn = DriverManager.getConnection(properties.getProperty("jdbc") + properties.getProperty("BBDD"),
					properties.getProperty("USER"), properties.getProperty("PASS"));
		} catch (SQLException | IOException ex) {
			JOptionPane.showMessageDialog(null,
					ex instanceof SQLException ? "Error al conectar la base de datos. " + ex.getMessage()
							: ex instanceof FileNotFoundException
									? "Error no se encuentra el archivo. " + ex.getMessage()
									: "Error al leer el archivo. " + ex.getMessage(),
					"Mensaje", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void iniciarSesion() throws SQLException {
		
		String usuario = "", contraseña = "", sql = "";
		Scanner ent = new Scanner(System.in);

		try (Statement stmt = conn.createStatement()) {

			System.out.println("Introduce el usuario");
			usuario = ent.nextLine();

			System.out.println("Introduce la contraseña");
			contraseña = ent.nextLine();
			
			sql = "SELECT * FROM clientes WHERE Usuario='" + usuario + "' AND Contraseña='" + contraseña + "'";

			try (ResultSet rs = stmt.executeQuery(sql)) {
				while (rs.next()) {
					System.out.println("Inicio de sesion exitoso");
				}
			}
		}

	}

	public void registrarse() throws SQLException {
		
		String nombre = "", apellido = "", usuario = "", email = "", contraseña = "", sql = "";
		Scanner ent = new Scanner(System.in);
		int telefono = 0;

		while (nombre.isBlank()) {
			System.out.println("Introduce tu nombre");
			nombre = ent.nextLine();
		}

		while (apellido.isBlank()) {
			System.out.println("Introduce tus apellidos");
			apellido = ent.nextLine();
		}

		while (usuario.isBlank()) {
			System.out.println("Introduce el nombre de usuario");
			usuario = ent.nextLine();
		}

		while (telefono <= 0) {
			System.out.println("Introduce el telefono");
			telefono = Integer.parseInt(ent.nextLine());
		}

		while (email.isBlank()) {
			System.out.println("Introduce el email");
			email = ent.nextLine();
			if (!email.contains("@")) {
				email = "";
			}
		}

		while (contraseña.isBlank()) {
			System.out.println("Introduce la contraseña");
			contraseña = ent.nextLine();
		}
		
		sql = "INSERT INTO clientes(Nombre,Apellidos,Usuario,Telefono,Email,Contraseña) VALUES('" + nombre + "','"
				+ apellido + "','" + usuario + "'," + telefono + ",'" + email + "','" + contraseña + "')";
		
		try(Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		}

	}

}
