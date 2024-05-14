package miApp;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AltaUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel miPanel;
	private JTextField textField_username;
	private JPasswordField textField_password;
	private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);
	private JPasswordField passwordField;
	
	public AltaUsuario(String nombreUsuario) {
		//pROPIEDADES DEL MARCO
		setTitle("AltaUsuario APP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		//Contenedor del frame
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); //esto no es necesario
		//Indicamos el contenedor del frame
		setContentPane(miPanel);
		miPanel.setLayout(null);
		//Añadimos componentes al panel
		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setBounds(88, 26, 116, 30);
		lblUsername.setFont(miFont);
		miPanel.add(lblUsername);
		
		textField_username = new JTextField();
		textField_username.setBounds(214, 26, 200, 30);
		textField_username.setFont(miFont);
		miPanel.add(textField_username);
		textField_username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(88, 67, 116, 30);
		lblPassword.setFont(miFont);
		miPanel.add(lblPassword);
		
		textField_password = new JPasswordField();
		textField_password.setBounds(214, 67, 200, 30);
		textField_password.setFont(miFont);
		miPanel.add(textField_password);
		textField_password.setColumns(10);
		

		//Botón de registro con la contraseña y usuario - ver apuntes de actionlistener y demás opciones
		JButton btnRegistro = new JButton("Registrarse");
		btnRegistro.setFont(miFont);
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Acción al pulsar el botón
				altaUsuario();
			}
		});
		btnRegistro.setBounds(214, 149, 131, 23);
		miPanel.add(btnRegistro);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		passwordField.setColumns(10);
		passwordField.setBounds(214, 108, 200, 30);
		miPanel.add(passwordField);
		
		JLabel lblPassword_1 = new JLabel("Repetir Contraseña:");
		lblPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword_1.setBounds(20, 108, 184, 30);
		miPanel.add(lblPassword_1);
	}
	private void altaUsuario() {
		
		String password= new String (textField_password.getPassword());
		
		if (!(textField_username.getText().isBlank()||password.isBlank())) {
			BaseDatos bbdd= new BaseDatos(); //textField_username.getText(),textField_password.getPassword()
			try {
		        // Cifrar la contraseña
		        String contraseñaCifrada = Login.cifrarContraseña(password);
				if (!bbdd.loginDB(textField_username.getText(),contraseñaCifrada)) {
					System.out.println("Procedemos a registrar");
					//HAY QUE COMPARAR LAS DOS CONTRASEÑAS PARA QUE COINCIDAN Y VALIDARLAS
					 try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda",
							 "root", "")) {
				            // Preparar la consulta SQL para insertar el usuario
				            String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
				            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				                pstmt.setString(1, textField_username.getText());
				                pstmt.setString(2, contraseñaCifrada);
				                pstmt.executeUpdate();
				                System.out.println("Usuario registrado correctamente.");
				                MenuPrincipal ventanaPrincipal= new MenuPrincipal(textField_username.getText());
				                this.setVisible(false);
				                ventanaPrincipal.setVisible(true);
				            } catch (SQLException e) {
								System.out.println("Error de conexión a la base de datos");
							}
				        }
				}else {
					System.out.println("No se pude registrar");
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
