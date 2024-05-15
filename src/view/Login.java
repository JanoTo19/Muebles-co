package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.BaseDatos;
import controller.Cifrado;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel miPanel;
	private JTextField textField_username;
	private JPasswordField textField_password;
	private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);
	private JButton btnLogin;
	private JLabel lblRecuperarPassword, lblRegistrarse;

	public Login() {
		inicializarComponentes();
		agregarAcciones();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> { // usando una expresión lambda en el método main, a partir Java 8
			try {
				Login frame = new Login();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void inicializarComponentes() {
		// Contenedor del frame
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // esto no es necesario
		
		// Indicamos el contenedor del frame
		setContentPane(miPanel);
		miPanel.setLayout(null);
		
		// Añadimos componentes al panel
		setTitle("LOGIN APP");
		setBounds(500, 180, 500, 500);
		
		// pROPIEDADES DEL MARCO
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // es un método de la clase
		
		// Botón de acceso con la contraseña y usuario - ver apuntes de actionlistener y
		// demás opciones

		agregarEtiquetas();
		agregarCamposTexto();
		agregarBotones();
	}

	private void agregarEtiquetas() {
		
		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setBounds(88, 90, 116, 30);
		lblUsername.setFont(miFont);
		miPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(88, 152, 116, 30);
		lblPassword.setFont(miFont);
		miPanel.add(lblPassword);
		
		lblRecuperarPassword = new JLabel("¿Perdiste tu contraseña?");
		lblRecuperarPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblRecuperarPassword.setBounds(166, 253, 174, 23);
		miPanel.add(lblRecuperarPassword);
		
		lblRegistrarse = new JLabel("¿No tienes cuenta? Registrate");
		lblRegistrarse.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblRegistrarse.setBounds(148, 288, 234, 23);
		miPanel.add(lblRegistrarse);

	}

	private void agregarCamposTexto() {
		
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

	}

	private void agregarBotones() {
		
		btnLogin = new JButton("LOGIN");
		btnLogin.setFont(miFont);
		btnLogin.setBounds(214, 211, 100, 23);
		miPanel.add(btnLogin);

	}

	private void agregarAcciones() {
		
		lblRecuperarPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Aquí puedes llamar al método que quieras ejecutar al hacer clic en el texto
				RecuperarContraseña rContra = new RecuperarContraseña();
				rContra.setVisible(true);
				dispose();
			}
		});
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Acción al pulsar el botón
				accederApp();
			}
		});
		
		lblRegistrarse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Aquí puedes llamar al método que quieras ejecutar al hacer clic en el texto
				System.out.println("Ventana de registro");
				// Llamar a la clase altaUsuario aquí
				AltaUsuario ventanaRegistro = new AltaUsuario();
				dispose(); // borra la ventan de Login
				ventanaRegistro.setVisible(true);
				// abrirRecuperarPassword();
			}
		});

	}

	private void accederApp() {

		String password = new String(textField_password.getPassword());
		if (!(textField_username.getText().isBlank() || password.isBlank())) {
			BaseDatos bbdd = new BaseDatos(); // textField_username.getText(),textField_password.getPassword()
			try {
				String contraseñaCifrada = Cifrado.encriptar(password);
				if (bbdd.loginDB(textField_username.getText(), contraseñaCifrada)) {
					MenuPrincipal ventanaPrincipal = new MenuPrincipal(textField_username.getText());
					// Mostrar la ventana principal
					ventanaPrincipal.setVisible(true);
					// ejecutar la clase menuprincipal.java
					this.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Su nombre de usuario o contraseña es incorrecto");
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
}