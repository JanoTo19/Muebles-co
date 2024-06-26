package miApp;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Clase AltaUsuario que representa la ventana de registro de nuevos usuarios.
 */
public class AltaUsuario extends JFrame {

	/**
	 * Atributo para el serial id
	 */
    private static final long serialVersionUID = 1L;
    /**
     * Atributos para crear los paneles
     */
    private JPanel miPanel,mainPanel;
    /**
     * Atributo para el campo de texto
     */
    private JTextField textField_username;
    /**
     * Atributos para los campos de texto de las contraseñas
     */
    private JPasswordField textField_password,passwordField;
    /**
     * Atributo para la fuente
     */
    private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);
    /**
     * Atributo para los botones
     */
    private JButton btnLogin;

    /**
     * Constructor de la clase AltaUsuario.
     * @param nombreUsuario Nombre del usuario
     */
    public AltaUsuario(String nombreUsuario) {
        // Contenedor del frame
        miPanel = new JPanel();
        miPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // Esto no es necesario
        setContentPane(miPanel);
        miPanel.setLayout(null);

        // Crear el panel principal (main)
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Alta de Usuario"));
        mainPanel.setBounds(10, 11, 416, 241);
        miPanel.add(mainPanel);
        mainPanel.setLayout(null);

        // Propiedades del marco
        setTitle("AltaUsuario APP");
        setBounds(100, 100, 450, 300);

        // Carga y establece el ícono de la aplicación
        try {
            Image img = ImageIO.read(new File("./src/files/Icono-App.png"));
            setIconImage(img);
        } catch (IOException e) {
            e.getMessage();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblPassword_1 = new JLabel("Repetir Contraseña:");
        lblPassword_1.setBounds(20, 119, 184, 30);
        mainPanel.add(lblPassword_1);
        lblPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 20));

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(88, 78, 116, 30);
        mainPanel.add(lblPassword);
        lblPassword.setFont(miFont);

        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setBounds(88, 37, 116, 30);
        mainPanel.add(lblUsername);
        lblUsername.setFont(miFont);

        textField_username = new JTextField();
        textField_username.setBounds(200, 37, 200, 30);
        mainPanel.add(textField_username);
        textField_username.setFont(miFont);
        textField_username.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 119, 200, 30);
        mainPanel.add(passwordField);
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        passwordField.setColumns(10);

        textField_password = new JPasswordField();
        textField_password.setBounds(200, 78, 200, 30);
        mainPanel.add(textField_password);
        textField_password.setFont(miFont);
        textField_password.setColumns(10);

        // Botón de registro
        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.setBounds(250, 176, 150, 33);
        mainPanel.add(btnRegistro);
        btnRegistro.setFont(miFont);

        // Botón de login
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnLogin.setBounds(73, 176, 131, 33);
        mainPanel.add(btnLogin);

        // Añade oyente para el botón de registro
        btnRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altaUsuario();
            }
        });

        // Añade oyente para el botón de login
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login ventanaLogin = new Login();
                dispose(); // Cierra la ventana de AltaUsuario
                ventanaLogin.setVisible(true);
            }
        });
    }

    /**
     * Método que maneja el registro de un nuevo usuario.
     */
    private void altaUsuario() {
        String confirmPassword = new String(passwordField.getPassword());
        String password = new String(textField_password.getPassword());
        String username = textField_username.getText();

        try {
            if (!(username.isBlank() || password.isBlank() || confirmPassword.isBlank())) {
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "Error!!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    if (existeUsuario(username)) {
                        JOptionPane.showMessageDialog(null, "Usuario ya registrado. Por favor, elija otro nombre de usuario.", "Error!!", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    BaseDatos bbdd = new BaseDatos();
                    String contraseniaCifrada = Login.cifrarContrasenia(password);
                    if (!bbdd.loginDB(textField_username.getText(), contraseniaCifrada)) {
                        // Inserta el nuevo usuario en la base de datos
                        try (Connection conn = BaseDatos.getConnection()) {
                            String sql = "INSERT INTO usuarios (id_usuario, username, password) VALUES (?, ?, ?)";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setInt(1, obtenerSiguienteIdUsuario());
                                pstmt.setString(2, textField_username.getText());
                                pstmt.setString(3, contraseniaCifrada);
                                pstmt.executeUpdate();
                                MenuPrincipal ventanaPrincipal = new MenuPrincipal(textField_username.getText());
                                this.setVisible(false);
                                ventanaPrincipal.setVisible(true);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario ya existente. Vaya al Login.", "Error!!", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(null, ex instanceof NoSuchAlgorithmException ? "El algoritmo solicitado no está disponible. " + ex.getMessage() : "Error en la base de datos. " + ex.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para obtener el siguiente ID de usuario.
     * @return El siguiente ID de usuario disponible
     */
    private int obtenerSiguienteIdUsuario() {
        int siguienteId = 1; // Valor predeterminado si la tabla está vacía
        try (Connection conn = BaseDatos.getConnection()) {
            String sql = "SELECT MAX(id_usuario) AS max_id FROM usuarios";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    int maxId = rs.getInt("max_id");
                    if (!rs.wasNull()) {
                        siguienteId = maxId + 1;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener el siguiente ID del usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return siguienteId;
    }

    /**
     * Método para verificar si un usuario ya existe en la base de datos.
     * @param username El nombre de usuario a verificar
     * @return true si el usuario existe, false en caso contrario
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */
    private boolean existeUsuario(String username) throws SQLException {
        boolean existe = false;

        try {
            String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
            try (Connection conn = BaseDatos.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        existe = count > 0;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos. " + ex.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
        }
        return existe;
    }
}

