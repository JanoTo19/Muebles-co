package mypkt;

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

public class VentanaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton btnIniciar,btnRegistro,btnSalir;
	private JPanel miPanel;
	
	public VentanaLogin() {
		inicializarComponentes();
		agregarAcciones();
	}
	
	public static void main(String[] args) {
		VentanaLogin frame = new VentanaLogin();
		frame.setVisible(true);
	}

	private void inicializarComponentes() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Janot\\Documents\\Mis cosas\\cosas privadas\\Cosas importantes\\Personalizacion\\Fondos, Fotos e Iconos\\Iconos\\PNG\\3144460.png"));
		setTitle("Muebles&Co");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 485, 361);
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(miPanel);
		miPanel.setLayout(null);
		
		agregarEtiquetas();
		agregarBotones();
	}

	private void agregarEtiquetas() {
		JLabel lblTitulo = new JLabel("Muebles&Co");
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(111, 10, 243, 21);
		miPanel.add(lblTitulo);
	}

	private void agregarBotones() {
		btnIniciar = new JButton("Iniciar Sesion");
		btnIniciar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnIniciar.setBounds(151, 65, 173, 28);
		miPanel.add(btnIniciar);
		
		btnRegistro = new JButton("Registro");
		btnRegistro.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnRegistro.setBounds(151, 120, 173, 28);
		miPanel.add(btnRegistro);
		
		btnSalir = new JButton("Salir");
		btnSalir.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSalir.setBounds(151, 174, 173, 28);
		miPanel.add(btnSalir);
	}

	private void agregarAcciones() {
		
		btnSalir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

	}
}
