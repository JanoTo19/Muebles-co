package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.BaseDatos;

public class RecuperarContraseña extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JButton btnRecuperar;
	
	public RecuperarContraseña() {
		inicializarComponentes();
		agregarAcciones();
	}

	private void inicializarComponentes() {
		setTitle("Recuperar Contraseña");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		agregarEtiquetas();
		agregarCamposTexto();
		agregarBotones();
	}

	private void agregarEtiquetas() {
		JLabel lblNUsuario = new JLabel("Introduce el nombre de usuario");
		lblNUsuario.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNUsuario.setHorizontalAlignment(SwingConstants.CENTER);
		lblNUsuario.setBounds(10, 10, 416, 20);
		contentPane.add(lblNUsuario);
	}

	private void agregarCamposTexto() {
		txtUsuario = new JTextField();
		txtUsuario.setBounds(142, 77, 165, 30);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
	}

	private void agregarBotones() {
		btnRecuperar = new JButton("Recuperar");
		btnRecuperar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnRecuperar.setBounds(161, 129, 130, 21);
		contentPane.add(btnRecuperar);
	}

	private void agregarAcciones() {
		btnRecuperar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BaseDatos bbdd = new BaseDatos();
				Login log = new Login();
				bbdd.recuperarUser(txtUsuario.getText());
				log.setVisible(true);
				dispose();
			}
		});
	}
	
}
