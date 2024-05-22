package miApp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que representa el menú principal de la aplicación.
 * Extiende JFrame para proporcionar una interfaz gráfica de usuario.
 * Permite al usuario interactuar con varias funcionalidades del sistema de inventario.
 */
public class MenuPrincipal extends JFrame {

	/**
	 * Atributos para los botones
	 */
    private JButton btnNuevoProducto, btnVerUsuario, btnListado, btnSalir, btnEliminar, btnVolver;
    /**
     * Atributo para el objeto connection
     */
    private Connection conn = BaseDatos.getConnection();
    /**
     * Serial ID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Atributos para los panel de la interfaz grafica
     */
    private JPanel miPanel, headerPanel, mainPanel, userPanel, cardPanel;
    /**
     * Atributos para os menus desplegables
     */
    private JComboBox<String> comboBoxTiposListado, comboBoxTiposBusqueda;
    /**
     * Atributos para las tablas
     */
    private JTable tablaProductos, tablaUsuarios;
    /**
     * Atributos para el campo de texto
     */
    private JTextField textField;
    /**
     * Atributo para una etiqueta
     */
    private JLabel lblInstruccion;
    /**
     * Atributo para cambiar de paneles
     */
    private CardLayout cardLayout;

    /**
     * Constructor de la clase MenuPrincipal.
     * @param nombreUsuario Nombre del usuario activo.
     */
    public MenuPrincipal(String nombreUsuario) {

        String usuarioActivo = nombreUsuario;

        miPanel = new JPanel();
        miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        miPanel.setLayout(new BorderLayout());
        setContentPane(miPanel);

        // Crear el CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        miPanel.add(cardPanel, BorderLayout.CENTER);

        // Crear el panel del encabezado (header)
        headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createTitledBorder("Menu Usuario"));
        miPanel.add(headerPanel, BorderLayout.NORTH);

        // Crear el panel principal (main)
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Inventario de Muebles"));
        mainPanel.setLayout(null);

        // Crear el panel de usuario
        userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder("Datos del Usuario Activo"));

        // Agregar los paneles al CardLayout
        cardPanel.add(mainPanel, "mainPanel");
        cardPanel.add(userPanel, "userPanel");

        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setTitle("Muebles&Co");        
        try {
            Image img = ImageIO.read(new File("./src/files/Icono-App.png"));
            setIconImage(img);
        } catch (IOException e) {
            e.getMessage();
        }

        // Etiqueta para mostrar el nombre de usuario
        JLabel lblUsuario = new JLabel("Usuario: " + nombreUsuario);
        lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 20));
        headerPanel.add(lblUsuario);

        JLabel lblColumna = new JLabel("Seleccione la columna a consultar: ");
        lblColumna.setBounds(472, 29, 280, 14);
        mainPanel.add(lblColumna);

        lblInstruccion = new JLabel("Indique =,<,> Ejemplo: [>100] o [='mueble']");
        lblInstruccion.setBounds(472, 64, 280, 14);
        mainPanel.add(lblInstruccion);

        textField = new JTextField();
        textField.setBounds(581, 94, 89, 23);
        mainPanel.add(textField);
        textField.setColumns(10);

        // Botón para volver a la pantalla de Login
        btnSalir = new JButton("Salir");
        headerPanel.add(btnSalir);

        btnNuevoProducto = new JButton("Alta Producto");
        headerPanel.add(btnNuevoProducto);

        btnVerUsuario = new JButton("Ver Usuario");
        headerPanel.add(btnVerUsuario);

        btnListado = new JButton("Listado");
        btnListado.setBounds(680, 94, 86, 23);
        mainPanel.add(btnListado);

        btnEliminar = new JButton("Eliminar Producto");
        headerPanel.add(btnEliminar);

        // Crear la tabla de productos
        String[] columnas = {"Usuario", "Id", "Nombre", "Tipo", "Gama", "Cantidad", "Precio"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBounds(10, 22, 452, 427);
        mainPanel.add(scrollPane);

        comboBoxTiposListado = new JComboBox<>();
        comboBoxTiposListado.setBounds(463, 94, 63, 22);
        mainPanel.add(comboBoxTiposListado);

        comboBoxTiposListado.addItem("ID");
        comboBoxTiposListado.addItem("Nombre");
        comboBoxTiposListado.addItem("Tipo");
        comboBoxTiposListado.addItem("Gama");
        comboBoxTiposListado.addItem("Cantidad");
        comboBoxTiposListado.addItem("Precio");
        
        comboBoxTiposBusqueda = new JComboBox<>();
        comboBoxTiposBusqueda.setBounds(533, 94, 40, 22);
        mainPanel.add(comboBoxTiposBusqueda);
        
        comboBoxTiposBusqueda.addItem("=");
        comboBoxTiposBusqueda.addItem(">");
        comboBoxTiposBusqueda.addItem("<");

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login frame = new Login();
                frame.setVisible(true);
                dispose();
            }
        });
        
        btnNuevoProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AltaProducto ventanaAltaProducto = new AltaProducto(usuarioActivo);
                dispose(); // Cierra la ventana actual
                ventanaAltaProducto.setVisible(true);
            }
        });

        btnVerUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarUsuario(usuarioActivo);
            }
        });

        btnListado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textoIngresado = textField.getText();
                String tipoListadoSeleccionado = (String) comboBoxTiposListado.getSelectedItem();
                TipoListado tipoListado = obtenerTipoListadoDesdeString(tipoListadoSeleccionado);
                obtenerListadoProductos(modeloTabla, tipoListado, textoIngresado);
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaProductos.getSelectedRow();
                if (filaSeleccionada != -1) {
                    // Obtiene el ID del producto de la fila seleccionada
                    String idProducto = tablaProductos.getValueAt(filaSeleccionada, 1).toString();

                    // Elimina el producto de la base de datos
                    eliminarProducto(idProducto);
                    
                    // Elimina la fila de la tabla
                    ((DefaultTableModel) tablaProductos.getModel()).removeRow(filaSeleccionada);
                    JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Ningún producto seleccionado", "Error!!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Obtener los datos de la tabla 'productos' de la base de datos
        obtenerProductos(modeloTabla, usuarioActivo);
    }

    /**
     * Método para obtener los productos de la base de datos.
     * @param modeloTabla El modelo de la tabla donde se mostrarán los productos.
     * @param usuarioActivo El nombre del usuario activo.
     */
    private void obtenerProductos(DefaultTableModel modeloTabla, String usuarioActivo) {
        try {
            String id_user_sql = "SELECT id_usuario FROM usuarios WHERE username = '" + usuarioActivo + "'";
            try (Statement idStmt = conn.createStatement();
                 ResultSet idRs = idStmt.executeQuery(id_user_sql)) {
                idRs.next(); // Mover el cursor al primer resultado (asumiendo que solo habrá uno)
                String id_usuario = idRs.getString("id_usuario");

                // Crear la consulta SQL para obtener los productos del usuario activo
                String productos_sql = "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio "
                        + "FROM productos WHERE id_usuario = '" + id_usuario + "'";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(productos_sql)) {
                    // Llenar la tabla con los datos de los productos
                    while (rs.next()) {
                        String id = rs.getString("id");
                        String nombre = rs.getString("nombre");
                        String tipo = rs.getString("tipo");
                        String gama = rs.getString("gama");
                        int cantidad = rs.getInt("cantidad");
                        double precio = rs.getDouble("precio");
                        Object[] fila = {id_usuario, id, nombre, tipo, gama, cantidad, precio};
                        modeloTabla.addRow(fila);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de la base de datos. " + ex.getMessage(),
                    "Error!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para mostrar los datos del usuario activo en la interfaz.
     * @param usuarioActivo El nombre del usuario activo.
     */
    private void mostrarUsuario(String usuarioActivo) {
        // Limpiar la tabla de productos
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaProductos.getModel();
        modeloTabla.setRowCount(0);

        // Crear la tabla de usuarios
        String[] columnasUsuarios = {"ID Usuario", "Nombre", "Teléfono"};
        DefaultTableModel modeloTablaUsuarios = new DefaultTableModel(columnasUsuarios, 0);
        tablaUsuarios = new JTable(modeloTablaUsuarios);
        JScrollPane scrollPaneUsuarios = new JScrollPane(tablaUsuarios);
        userPanel.add(scrollPaneUsuarios, BorderLayout.CENTER);

        // Botón para volver al panel principal
        btnVolver = new JButton("Volver");
        userPanel.add(btnVolver, BorderLayout.SOUTH);
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obtenerProductos(modeloTabla, usuarioActivo);
                cardLayout.show(cardPanel, "mainPanel");
            }
        });

        // Obtener y mostrar los usuarios
        obtenerUsuarios(modeloTablaUsuarios, usuarioActivo);

        // Cambiar al panel de usuario
        cardLayout.show(cardPanel, "userPanel");
    }

    /**
     * Método para obtener y mostrar los datos del usuario activo en la tabla.
     * @param modeloTablaUsuarios El modelo de la tabla donde se mostrarán los datos del usuario.
     * @param usuarioActivo El nombre del usuario activo.
     */
    private void obtenerUsuarios(DefaultTableModel modeloTablaUsuarios, String usuarioActivo) {
        try {
            String sql = "SELECT id_usuario, username, telefono FROM usuarios WHERE username = '" + usuarioActivo + "'";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id_usuario = rs.getInt("id_usuario");
                    String nombre = rs.getString("username");
                    int telefono = rs.getInt("telefono");
                    Object[] fila = {id_usuario, nombre, telefono};
                    modeloTablaUsuarios.addRow(fila);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de los usuarios. " + ex.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Enumeración para los tipos de listados disponibles.
     */
    public enum TipoListado {
    	/**
    	 * Atributo id del enum tipolistado
    	 */
        ID, 
    	/**
    	 * Atributo nombre del enum tipolistado
    	 */
        NOMBRE, 
    	/**
    	 * Atributo tipo del enum tipolistado
    	 */
        TIPO, 
    	/**
    	 * Atributo gama del enum tipolistado
    	 */
        GAMA, 
    	/**
    	 * Atributo cantidad del enum tipolistado
    	 */
        CANTIDAD, 
    	/**
    	 * Atributo precio del enum tipolistado
    	 */
        PRECIO
    }

    /**
     * Método para obtener listados de productos desde la base de datos según el tipo de listado seleccionado.
     * @param modeloTabla El modelo de la tabla donde se mostrarán los productos.
     * @param tipoListado El tipo de listado seleccionado.
     * @param textoIngresado El texto ingresado para la consulta.
     */
    private void obtenerListadoProductos(DefaultTableModel modeloTabla, TipoListado tipoListado, String textoIngresado) {
        try {
            modeloTabla.setRowCount(0); // Limpiar la tabla antes de llenarla nuevamente
            String sql = construirConsultaSQL(tipoListado, textoIngresado);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id_usuario = rs.getInt("id_usuario");
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    String tipo = rs.getString("tipo");
                    String gama = rs.getString("gama");
                    int cantidad = rs.getInt("cantidad");
                    double precio = rs.getDouble("precio");
                    Object[] fila = {id_usuario, id, nombre, tipo, gama, cantidad, precio};
                    modeloTabla.addRow(fila);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de los productos. " + ex.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para eliminar un producto de la base de datos.
     * @param idProducto El ID del producto a eliminar.
     */
    private void eliminarProducto(String idProducto) {
        try (Connection con = BaseDatos.getConnection()) {
            String sql = "DELETE FROM productos WHERE id = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, idProducto);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos. " + ex.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para construir la consulta SQL según el tipo de listado seleccionado y el texto ingresado.
     * @param tipoListado El tipo de listado seleccionado.
     * @param textoIngresado El texto ingresado para la consulta.
     * @return La consulta SQL construida.
     */
    private String construirConsultaSQL(TipoListado tipoListado, String textoIngresado) {
        String simbolo = (String) comboBoxTiposBusqueda.getSelectedItem();
        if (textoIngresado.isBlank()) {
			return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos";
		}
        switch (tipoListado) {
            case ID:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos WHERE id" + simbolo + textoIngresado + " ORDER BY id";
            case NOMBRE:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos WHERE nombre='" + textoIngresado + "' ORDER BY nombre";
            case TIPO:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos WHERE tipo='" + textoIngresado + "' ORDER BY tipo";
            case GAMA:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos WHERE gama='" + textoIngresado + "' ORDER BY gama";
            case CANTIDAD:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos WHERE cantidad" + simbolo + textoIngresado + " ORDER BY cantidad";
            case PRECIO:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos WHERE precio" + simbolo + textoIngresado + " ORDER BY precio";
            default:
                throw new IllegalArgumentException("Tipo de listado no válido.");
        }
    }

    /**
     * Método para obtener el tipo de listado desde el string seleccionado en el comboBox.
     * @param tipoListadoSeleccionado El string del tipo de listado seleccionado.
     * @return El tipo de listado correspondiente.
     */
    private TipoListado obtenerTipoListadoDesdeString(String tipoListadoSeleccionado) {
        switch (tipoListadoSeleccionado) {
            case "ID":
                return TipoListado.ID;
            case "Nombre":
                return TipoListado.NOMBRE;
            case "Tipo":
                return TipoListado.TIPO;
            case "Gama":
                return TipoListado.GAMA;
            case "Cantidad":
                return TipoListado.CANTIDAD;
            case "Precio":
                return TipoListado.PRECIO;
            default:
                throw new IllegalArgumentException("Tipo de listado no válido: " + tipoListadoSeleccionado);
        }
    }
}

