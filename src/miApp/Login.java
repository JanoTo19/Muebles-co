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
		//Contenedor del frame
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); //esto no es necesario
		//Indicamos el contenedor del frame
		setContentPane(miPanel);
		miPanel.setLayout(null);
		
		//pROPIEDADES DEL MARCO
		setTitle("LOGIN APP");
		setBounds(465, 200, 500, 500);
		
		try {
			Image img = ImageIO.read(new File("./src/files/Icono-App.png"));
			setIconImage(img);
		} catch (IOException e) {
			e.getMessage();
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //es un método de la clase
		
		//Añadimos componentes al panel
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
		
		//Botón de acceso con la contraseña y usuario - ver apuntes de actionlistener y demás opciones
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(miFont);
		btnLogin.setBounds(214, 211, 100, 23);
		miPanel.add(btnLogin);
		
		lblRecuperarPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Aquí puedes llamar al método que quieras ejecutar al hacer clic en el texto
            	BaseDatos.RecuperarContraseña();
            }
            public void mouseEntered(MouseEvent e) {
            	lblRecuperarPassword.setForeground(Color.BLUE); // Cambiar color al pasar el ratón
            }
            public void mouseExited(MouseEvent e) {
            	lblRecuperarPassword.setForeground(Color.BLACK); // Restaurar color al salir del texto
            }
        });
		
		lblRegistrarse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Acción al pulsar el botón
				accederApp();
			}
		});
	}
	
	private void accederApp() {
		
		String password = new String(textField_password.getPassword());
		if (!(textField_username.getText().isBlank() || password.isBlank())) {
			BaseDatos bbdd= new BaseDatos(); //textField_username.getText(),textField_password.getPassword()
			try {
				String contraseñaCifrada=cifrarContraseña(password);

				if (bbdd.loginDB(textField_username.getText(),contraseñaCifrada)) {
					MenuPrincipal ventanaPrincipal = new MenuPrincipal(textField_username.getText());
					// Mostrar la ventana principal
                    ventanaPrincipal.setVisible(true);
					//ejecutar la clase menuprincipal.java
					this.setVisible(false);
				}else {
		            JOptionPane.showMessageDialog(null, "Su nombre de usuario o contraseña es incorrecto.","Error!!",JOptionPane.INFORMATION_MESSAGE);
		            return;
				}
			} catch (NoSuchAlgorithmException | SQLException ex) {
				JOptionPane.showMessageDialog(null, ex instanceof NoSuchAlgorithmException ? "El algoritmo solicitado no está disponible. " + ex.getMessage() : "Error en la base de datos. " + ex.getMessage(),"Error!!",JOptionPane.ERROR_MESSAGE);
			} 
		}
	}
	
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
	
	private static SecretKeySpec generadorClave(String llave) {
		
		try {
			byte [] cadena = llave.getBytes("UTF-8");
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
