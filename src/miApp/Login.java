package miApp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Clase Login que representa la ventana de inicio de sesión.
 */
public class Login extends JFrame { 

    private static final long serialVersionUID = 1L;
    private JPanel miPanel;
    private JTextField textField_username;
    private JPasswordField textField_password;
    private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);

    /**
     * Método principal que inicia la aplicación.
     * @param args Argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructor de la clase Login.
     */
    public Login() {
        miPanel = new JPanel();
        miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(miPanel);
        miPanel.setLayout(null);

        setTitle("LOGIN APP");
        setBounds(465, 200, 500, 500);

        // Carga y establece el ícono de la aplicación
        try {
            Image img = ImageIO.read(new File("./src/files/Icono-App.png"));
            setIconImage(img);
        } catch (IOException e) {
            e.getMessage();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setBounds(88, 90, 116, 30);
        lblUsername.setFont(miFont);
        miPanel.add(lblUsername);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(88, 152, 116, 30);
        lblPassword.setFont(miFont);
        miPanel.add(lblPassword);

        JLabel lblRecuperarPassword = new JLabel("¿Perdiste tu contraseña?");
        lblRecuperarPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblRecuperarPassword.setBounds(166, 253, 174, 23);
        miPanel.add(lblRecuperarPassword);

        JLabel lblRegistrarse = new JLabel("¿No tienes cuenta? Regístrate.");
        lblRegistrarse.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblRegistrarse.setBounds(148, 288, 234, 23);
        miPanel.add(lblRegistrarse);

        textField_username = new JTextField();
        textField_username.setBounds(214, 90, 200, 30);
        textField_username.setFont(miFont);
        miPanel.add(textField_username);
        textField_username.setColumns(10);

        textField_password = new JPasswordField();
        textField_password.setBounds(214, 152, 200, 30);
        textField_password.setFont(miFont);
        miPanel.add(textField_password);
        textField_password.setColumns(10);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(miFont);
        btnLogin.setBounds(214, 211, 100, 23);
        miPanel.add(btnLogin);

        // Añade oyente para recuperar contraseña
        lblRecuperarPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BaseDatos.RecuperarContraseña();
            }
            public void mouseEntered(MouseEvent e) {
                lblRecuperarPassword.setForeground(Color.BLUE); 
            }
            public void mouseExited(MouseEvent e) {
                lblRecuperarPassword.setForeground(Color.BLACK); 
            }
        });

        // Añade oyente para registrarse
        lblRegistrarse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AltaUsuario ventanaRegistro = new AltaUsuario(textField_username.getText());
                dispose(); 
                ventanaRegistro.setVisible(true);
            }
            public void mouseEntered(MouseEvent e) {
                lblRegistrarse.setForeground(Color.BLUE); 
            }
            public void mouseExited(MouseEvent e) {
                lblRegistrarse.setForeground(Color.BLACK);
            }
        });

        // Añade oyente para el botón de login
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accederApp();
            }
        });
    }

    /**
     * Método que maneja el acceso a la aplicación.
     */
    private void accederApp() {
        String password = new String(textField_password.getPassword());
        if (!(textField_username.getText().isBlank() || password.isBlank())) {
            BaseDatos bbdd= new BaseDatos();
            try {
                String contraseñaCifrada = cifrarContraseña(password);

                if (bbdd.loginDB(textField_username.getText(), contraseñaCifrada)) {
                    MenuPrincipal ventanaPrincipal = new MenuPrincipal(textField_username.getText());
                    ventanaPrincipal.setVisible(true);
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Su nombre de usuario o contraseña es incorrecto.", "Error!!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            } catch (NoSuchAlgorithmException | SQLException ex) {
                JOptionPane.showMessageDialog(null, ex instanceof NoSuchAlgorithmException ? "El algoritmo solicitado no está disponible. " + ex.getMessage() : "Error en la base de datos. " + ex.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
            } 
        }
    }

    /**
     * Método para cifrar una contraseña.
     * @param contraseña La contraseña en texto plano
     * @return La contraseña cifrada en formato Base64
     */
    public static String cifrarContraseña(String contraseña) {
        try {
            SecretKeySpec se = generadorClave("Muebles&co");
            Cipher c = Cipher.getInstance("AES");

            c.init(Cipher.ENCRYPT_MODE, se);

            byte[] cadena = contraseña.getBytes("UTF-8");
            byte[] encriptada = c.doFinal(cadena);
            String cadenaEncriptada = Base64.getEncoder().encodeToString(encriptada);
            return cadenaEncriptada;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Método para desencriptar una contraseña cifrada.
     * @param desencriptar La contraseña cifrada en Base64
     * @return La contraseña desencriptada en texto plano
     */
    public static String desencriptar(String desencriptar) {
        try {
            SecretKeySpec se = generadorClave("Muebles&co");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, se);

            byte[] cadena = Base64.getDecoder().decode(desencriptar);
            byte[] desencriptacion = c.doFinal(cadena);
            String cadenaDesencriptada = new String(desencriptacion);
            return cadenaDesencriptada;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Genera una clave secreta a partir de una cadena de texto.
     * @param llave La llave en texto plano
     * @return La clave secreta generada
     */
    private static SecretKeySpec generadorClave(String llave) {
        try {
            byte[] cadena = llave.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            cadena = md.digest(cadena);
            cadena = Arrays.copyOf(cadena, 16);

            SecretKeySpec se = new SecretKeySpec(cadena, "AES");
            return se;
        } catch (Exception e) {
            return null;
        }
    }
}

