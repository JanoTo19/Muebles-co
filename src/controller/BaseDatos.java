package controller;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import view.MenuPrincipal;

public class BaseDatos {
    private static Connection conn;

    private static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "");
        }
        return conn;
    }

    public boolean loginDB(String username, String contraseñaCifrada) throws NoSuchAlgorithmException, SQLException {
        String sql = "SELECT password FROM usuarios WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
                Object[] fila = {nombre, tipo, gama, cantidad, precio};
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
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, tipo);
            pstmt.setString(3, gama);
            pstmt.setInt(4, cantidad);
            pstmt.setDouble(5, precio);
            pstmt.executeUpdate();
            System.out.println("Producto registrado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar el producto en la base de datos.");
            e.printStackTrace();
        }
    }

    public void recuperarUser(String user) {
        String sql = "SELECT password FROM usuarios WHERE username=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
