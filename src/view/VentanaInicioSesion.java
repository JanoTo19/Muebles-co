package view;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import model.Usuario;

public class VentanaInicioSesion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtPassword;
	private JButton btnIniciar;

	public VentanaInicioSesion() {
		inicializarComponentes();
		agregarAcciones();
	}

	private void inicializarComponentes() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Inicio de Sesion");
		setBounds(500, 200, 427, 313);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Janot\\Documents\\Mis cosas\\cosas privadas\\Cosas importantes\\Personalizacion\\Fondos, Fotos e Iconos\\Iconos\\PNG\\3144460.png"));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		agregarEtiquetas();
		agregarCamposTexto();
		agregarBotones();
	}

	private void agregarEtiquetas() {

		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUsuario.setBounds(82, 80, 74, 25);
		contentPane.add(lblUsuario);

		JLabel lblContraseña = new JLabel("Contraseña");
		lblContraseña.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblContraseña.setBounds(48, 133, 108, 25);
		contentPane.add(lblContraseña);

	}

	private void agregarCamposTexto() {

		txtUsuario = new JTextField();
		txtUsuario.setBounds(196, 87, 96, 19);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(196, 140, 96, 19);
		contentPane.add(txtPassword);

	}

	private void agregarBotones() {

		btnIniciar = new JButton("Iniciar Sesion");
		btnIniciar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnIniciar.setBounds(111, 182, 158, 33);
		contentPane.add(btnIniciar);

	}

	private void agregarAcciones() {

		btnIniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String password = new String(txtPassword.getPassword());
				Usuario user = new Usuario(txtUsuario.getText(),password);
				BaseDatos bbdd = new BaseDatos();
				try {
					if (!(user.getNombre().isBlank() || user.getContraseña().isBlank())) {
						if (bbdd.iniciarSesion(txtUsuario.getText(), password)) {
							JOptionPane.showMessageDialog(null, "Login Exitoso", "Mensaje",
									JOptionPane.INFORMATION_MESSAGE);
							VentanaPrincipal vInicio = new VentanaPrincipal(user.getNombre());
							vInicio.setVisible(true);
							dispose();
							
						} else {
							JOptionPane.showMessageDialog(null, "Login Fallido", "Mensaje",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Error", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Error en la base de datos. " + ex.getMessage(), "Mensaje",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

	}
}
