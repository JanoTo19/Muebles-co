package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.BaseDatos;

public class MenuPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel miPanel;
	private JPanel headerPanel;
	private JPanel mainPanel;
	private JTable tablaProductos;
	private JButton btnSalir, btnAniadir, btnEliminar;
	private DefaultTableModel modeloTabla;

	public MenuPrincipal(String nombreUsuario) {
		inicializarComponentes(nombreUsuario);
		agregarAcciones();
	}

	private void inicializarComponentes(String nombreUsuario) {
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		miPanel.setLayout(new BorderLayout());
		setContentPane(miPanel);
		
		// Crear el panel del encabezado (header)
		headerPanel = new JPanel();
		headerPanel.setBorder(BorderFactory.createTitledBorder("Usuario"));
		miPanel.add(headerPanel, BorderLayout.NORTH);
		
		// Crear el panel principal (main)
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createTitledBorder("Resumen"));
		miPanel.add(mainPanel, BorderLayout.CENTER);
		
		setBounds(500, 180, 800, 600);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		agregarEtiquetas(nombreUsuario);
		agregarTablas();
		agregarBotones();
	}

	private void agregarEtiquetas(String nombreUsuario) {
		// Etiqueta para mostrar el nombre de usuario
		JLabel lblNewLabel = new JLabel("Usuario logeado: " + nombreUsuario);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		headerPanel.add(lblNewLabel);
	}

	private void agregarTablas() {
		// Crear la tabla de productos
		String[] columnas = { "Nombre", "Tipo", "Gama", "Cantidad", "Precio" };
		modeloTabla = new DefaultTableModel(columnas, 0);
		tablaProductos = new JTable(modeloTabla);
		JScrollPane scrollPane = new JScrollPane(tablaProductos);
		mainPanel.add(scrollPane);

		// Obtener los datos de la tabla 'productos' de la base de datos
		BaseDatos.obtenerProductos(modeloTabla);
	}

	private void agregarBotones() {
		// Botón para volver a la pantalla de Login
		btnSalir = new JButton("Salir");
		headerPanel.add(btnSalir);
		
		btnAniadir = new JButton("Añadir Producto");
		headerPanel.add(btnAniadir);
		
		btnEliminar = new JButton("Eliminar Producto");
		headerPanel.add(btnEliminar);
	}

	private void agregarAcciones() {
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login frame = new Login();
				frame.setVisible(true);
				dispose();
			}
		});
		
		btnAniadir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AltaProducto aProducto = new AltaProducto();
				aProducto.setVisible(true);
				dispose();
				
			}
		});
		
		btnEliminar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
						BaseDatos bbdd = new BaseDatos();
						bbdd.eliminarProducto(tablaProductos,modeloTabla);
			}
		});
	}
}