package miApp;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.awt.event.ActionEvent;

public class Login extends JFrame { //los métodos están heredados de la clase JFrame

	private static final long serialVersionUID = 1L;
	//Atributos de mi ventana
	private JPanel miPanel;
	private JTextField textField_username;
	private JPasswordField textField_password;
	private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);


	public static void main(String[] args) {
	    EventQueue.invokeLater(() -> { //usando una expresión lambda en el método main, a partir Java 8
	        try {
	            Login frame = new Login();
	            frame.setVisible(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });
	}

	public Login() {
		//pROPIEDADES DEL MARCO
		setTitle("LOGIN APP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //es un método de la clase
		setBounds(100, 100, 500, 500);
		//Contenedor del frame
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); //esto no es necesario
		//Indicamos el contenedor del frame
		setContentPane(miPanel);
		miPanel.setLayout(null);
		//Añadimos componentes al panel
		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setBounds(88, 90, 116, 30);
		lblUsername.setFont(miFont);
		miPanel.add(lblUsername);
		
		textField_username = new JTextField();
		textField_username.setBounds(214, 90, 200, 30);
		textField_username.setFont(miFont);
		miPanel.add(textField_username);
		textField_username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(88, 152, 116, 30);
		lblPassword.setFont(miFont);
		miPanel.add(lblPassword);
		
		textField_password = new JPasswordField();
		textField_password.setBounds(214, 152, 200, 30);
		textField_password.setFont(miFont);
		miPanel.add(textField_password);
		textField_password.setColumns(10);
		
		JLabel lblRecuperarPassword = new JLabel("¿Perdiste tu contraseña?");
		lblRecuperarPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblRecuperarPassword.setBounds(166, 253, 174, 23);
		miPanel.add(lblRecuperarPassword);
        lblRecuperarPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Aquí puedes llamar al método que quieras ejecutar al hacer clic en el texto
                String mensaje = "No se ha implementado la recuperación de contraseña.\nRegístrese de nuevo.";
                JOptionPane.showMessageDialog(lblRecuperarPassword, mensaje, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
            public void mouseEntered(MouseEvent e) {
            	lblRecuperarPassword.setForeground(Color.BLUE); // Cambiar color al pasar el ratón
            }
            public void mouseExited(MouseEvent e) {
            	lblRecuperarPassword.setForeground(Color.BLACK); // Restaurar color al salir del texto
            }
        });
		
		//Botón de acceso con la contraseña y usuario - ver apuntes de actionlistener y demás opciones
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(miFont);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Acción al pulsar el botón
				accederApp();
			}
		});
		btnLogin.setBounds(214, 211, 100, 23);
		miPanel.add(btnLogin);
		
		
		JLabel lblRegistrarse = new JLabel("¿No tienes cuenta? Regístrate.");
		lblRegistrarse.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblRegistrarse.setBounds(148, 288, 234, 23);
		miPanel.add(lblRegistrarse);
		lblRegistrarse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Aquí puedes llamar al método que quieras ejecutar al hacer clic en el texto
            	System.out.println("Ventana de registro");
                // Llamar a la clase altaUsuario aquí
                AltaUsuario ventanaRegistro = new AltaUsuario(textField_username.getText());
                dispose(); //borra la ventan de Login
                ventanaRegistro.setVisible(true);
            }
            public void mouseEntered(MouseEvent e) {
            	lblRegistrarse.setForeground(Color.BLUE); // Cambiar color al pasar el ratón
            }
            public void mouseExited(MouseEvent e) {
            	lblRegistrarse.setForeground(Color.BLACK); // Restaurar color al salir del texto
            }
        });
	}
	private void accederApp() {
		
		String password= new String (textField_password.getPassword());
		System.out.println("Password: "+password);
		if (!(textField_username.getText().isBlank()||password.isBlank())) {
			BaseDatos bbdd= new BaseDatos(); //textField_username.getText(),textField_password.getPassword()
			try {
				String contraseñaCifrada=cifrarContraseña(password);
				if (bbdd.loginDB(textField_username.getText(),contraseñaCifrada)) {
					System.out.println("Todo ok");
					MenuPrincipal ventanaPrincipal = new MenuPrincipal(textField_username.getText());
					// Mostrar la ventana principal
                    ventanaPrincipal.setVisible(true);
					//ejecutar la clase menuprincipal.java
					this.setVisible(false);
				}else {
					System.out.println("Su nombre de usuario o contraseña es incorrecto");
		            JOptionPane.showMessageDialog(this, "Su nombre de usuario o contraseña es incorrecto.");
		            return;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
    public static String cifrarContraseña(String contraseña) throws NoSuchAlgorithmException {

            // Cadena fija para la sal
            byte[] salt = "EstoEsUnaSalFija".getBytes();

            // Agregar la sal a la contraseña
            String contraseñaConSal = contraseña + new String(salt);

            // Crear instancia de MessageDigest para SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseñaConSal.getBytes());

            // Convertir el hash y la sal a base64 para almacenarlos en la base de datos
            String contraseñaCifrada = Base64.getEncoder().encodeToString(hash);

            // Formato para almacenar en la base de datos: hash:sal
            return contraseñaCifrada;
        }
}
