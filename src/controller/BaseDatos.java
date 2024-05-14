package controller;

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
	private Encriptacion en = new Encriptacion();
	private Connection conn;

	public BaseDatos() {
		try (FileInputStream fis = new FileInputStream("./src/files/config.properties")) {

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

	public boolean iniciarSesion(String user, String pass) throws SQLException {

		boolean respuesta = false;
		String contraseña = en.encriptar(pass);
		String sql = "SELECT * FROM usuarios WHERE User='" + user + "' AND Pass='" + contraseña + "'";

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				respuesta = true;
			}

		}
		return respuesta;
	}

	public void registrarse(String user, String pass) throws SQLException {

		Scanner ent = new Scanner(System.in);
		String contraseña = en.encriptar(pass);

		String sql = "INSERT INTO usuarios VALUES('" + user + "','" + contraseña + "')";
		

		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "Usuario " + user + " añadido exitosamente", "Mensaje",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

}
