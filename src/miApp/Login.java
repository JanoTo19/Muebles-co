package miApp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
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
            	System.out.println("Contraseña recuperada");
         //       abrirRecuperarPassword();
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
		
//		textField_password.getPassword();
		
		
		JLabel lblRegistrarse = new JLabel("¿No tienes cuenta? Registrate");
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
         //       abrirRecuperarPassword();
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
				System.out.println("Contraseña login cifrada: "+contraseñaCifrada);
				System.out.println("contrasea cifrada en login: "+contraseñaCifrada);
				if (bbdd.loginDB(textField_username.getText(),contraseñaCifrada)) {
					System.out.println("Todo ok");
					MenuPrincipal ventanaPrincipal = new MenuPrincipal(textField_username.getText());
	                
					//ejecutar la clase menuprincipal.java
					this.setVisible(false);
				}else {
					System.out.println("Su nombre de usuario o contraseña es incorrecto");
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			bbdd.cerrarBD();
		}
	}
	public static String cifrarContraseña(String contraseña) throws NoSuchAlgorithmException {
	    // Definir una sal fija para que el cifrado sea determinista
	    byte[] salt = "MiSalFijaParaCifrado".getBytes();

	    // Crear un arreglo que contenga la concatenación de la contraseña y la sal
	    byte[] contraseñaConSal = new byte[contraseña.length() + salt.length];
	    System.arraycopy(contraseña.getBytes(), 0, contraseñaConSal, 0, contraseña.length());
	    System.arraycopy(salt, 0, contraseñaConSal, contraseña.length(), salt.length);

	    // Crear instancia de MessageDigest para SHA-256
	    MessageDigest digest = MessageDigest.getInstance("SHA-256");
	    byte[] hash = digest.digest(contraseñaConSal);

	    // Convertir el hash y la sal a base64 para almacenarlos en la base de datos
	    String contraseñaCifrada = Base64.getEncoder().encodeToString(hash);
	    String salBase64 = Base64.getEncoder().encodeToString(salt);

	    // Formato para almacenar en la base de datos: hash:sal
	    return contraseñaCifrada + ":" + salBase64;
	}


}
