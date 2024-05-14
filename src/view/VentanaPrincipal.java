package view;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaPrincipal extends JFrame {

	private Font fuente = new Font("Tahoma", Font.PLAIN, 20);
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnSalir;
	
	public VentanaPrincipal(String nombre) {
		inicializarComponentes(nombre);
		agregarAcciones();
	}

	private void inicializarComponentes(String nombre) {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Muebles&Co");
		setBounds(500, 200, 427, 313);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Janot\\Documents\\Mis cosas\\cosas privadas\\Cosas importantes\\Personalizacion\\Fondos, Fotos e Iconos\\Iconos\\PNG\\3144460.png"));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		agregarEtiquetas(nombre);
		agregarBotones();
	}

	private void agregarEtiquetas(String nombre) {
		JLabel lblBienvenida = new JLabel("Bienvenido " + nombre);
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setFont(fuente);
		lblBienvenida.setBounds(10, 10, 416, 23);
		contentPane.add(lblBienvenida);
	}
	
	private void agregarBotones() {
		btnSalir = new JButton("X");
		btnSalir.setFont(fuente);
		btnSalir.setBounds(358, 15, 47, 47);
		contentPane.add(btnSalir);
	}

	private void agregarAcciones() {
		btnSalir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaLogin vLogin = new VentanaLogin();
				vLogin.setVisible(true);
				dispose();
			}
		});
	}

}
