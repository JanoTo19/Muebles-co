package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.BaseDatos;

public class AltaUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel miPanel;
	private JTextField textField_username;
	private JPasswordField textField_password;
	private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);
	private JPasswordField passwordField;
    private JPanel mainPanel;
    private JButton btnRegistro;
    
	public AltaUsuario() {
		inicializarComponentes();
		agregarAcciones();
	}

	private void inicializarComponentes() {
		//Contenedor del frame
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); //esto no es necesario
		setContentPane(miPanel);
		miPanel.setLayout(null);
		
		// Crear el panel principal (main)
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createTitledBorder("Alta de Usuario"));
		mainPanel.setBounds(10, 11, 416, 241);
		miPanel.add(mainPanel);
		mainPanel.setLayout(null);
		
		//pROPIEDADES DEL MARCO
		setTitle("Alta Usuario APP");
		setBounds(500, 180, 450, 300);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		agregarEtiquetas();
		agregarCamposTexto();
		agregarBotones();
	}

	private void agregarEtiquetas() {
		JLabel lblPassword_1 = new JLabel("Repetir Contraseña:");
		lblPassword_1.setBounds(20, 119, 184, 30);
		mainPanel.add(lblPassword_1);
		lblPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(88, 78, 116, 30);
		mainPanel.add(lblPassword);
		lblPassword.setFont(miFont);
		
		//Añadimos componentes al panel
		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setBounds(88, 37, 116, 30);
		mainPanel.add(lblUsername);
		lblUsername.setFont(miFont);
	}

	private void agregarCamposTexto() {
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
		
		textField_username = new JTextField();
		textField_username.setBounds(200, 37, 200, 30);
		mainPanel.add(textField_username);
		textField_username.setFont(miFont);
		textField_username.setColumns(10);
	}

	private void agregarBotones() {
		//Botón de registro con la contraseña y usuario - ver apuntes de actionlistener y demás opciones
		btnRegistro = new JButton("Registrarse");
		btnRegistro.setBounds(269, 176, 131, 33);
		mainPanel.add(btnRegistro);
		btnRegistro.setFont(miFont);
	}

	private void agregarAcciones() {
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Acción al pulsar el botón
				String password = new String(textField_password.getPassword());
				String confirmar = new String(passwordField.getPassword());
				BaseDatos.altaUsuario(textField_username.getText(),password,confirmar);
				setVisible(false);
			}
		});
	}
}