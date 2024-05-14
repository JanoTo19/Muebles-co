package view;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	public VentanaPrincipal(String nombre) {
		inicializarComponentes(nombre);
		agregarAcciones();
	}

	private void inicializarComponentes(String nombre) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Janot\\Documents\\Mis cosas\\cosas privadas\\Cosas importantes\\Personalizacion\\Fondos, Fotos e Iconos\\Iconos\\PNG\\3144460.png"));
		setBounds(400, 213, 500, 313);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		agregarEtiquetas(nombre);
	}

	private void agregarEtiquetas(String nombre) {
		JLabel lblBienvenida = new JLabel("Bienvenido " + nombre);
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblBienvenida.setBounds(10, 10, 416, 23);
		contentPane.add(lblBienvenida);
	}

	private void agregarAcciones() {

	}

}
