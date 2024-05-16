package miApp;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel miPanel;
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JTable tablaProductos;
    private JButton btnNuevoProducto;
    private JButton btnNewButton;
    private JComboBox<String> comboBoxTiposListado;
    private JButton btnNewButton_1;
    private JTextField textField;
    private JLabel lblNewLabel_2;


    public MenuPrincipal(String nombreUsuario) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        miPanel = new JPanel();
        miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        miPanel.setLayout(new BorderLayout());
        setContentPane(miPanel);

        // Crear el panel del encabezado (header)
        headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createTitledBorder("Menu Usuario"));
        miPanel.add(headerPanel, BorderLayout.NORTH);

        // Etiqueta para mostrar el nombre de usuario
        JLabel lblNewLabel = new JLabel("Usuario: " + nombreUsuario);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        headerPanel.add(lblNewLabel);
        
        // Botón para volver a la pantalla de Login
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login frame = new Login();
                frame.setVisible(true);
                dispose();
            }
        });
        String usuarioActivo=nombreUsuario;
        btnNuevoProducto = new JButton("Alta Producto");
        btnNuevoProducto.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                AltaProducto ventanaAltaProducto = new AltaProducto(usuarioActivo);
                dispose(); //borra la ventan de Login
                ventanaAltaProducto.setVisible(true);
        	}
        });

        
        btnNewButton = new JButton("Ver Usuario");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mostrarUsuario(usuarioActivo);
        	}
        });
        


        // Crear el panel principal (main)
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Inventario de Muebles"));
        miPanel.add(mainPanel, BorderLayout.CENTER);

        // Crear la tabla de productos
        String[] columnas = {"Usuario", "Id", "Nombre", "Tipo", "Gama", "Cantidad", "Precio"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        mainPanel.setLayout(null);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBounds(10, 22, 452, 427);
        mainPanel.add(scrollPane);
        
        btnNewButton_1 = new JButton("Listado");
        btnNewButton_1.setBounds(680, 94, 86, 23);
        mainPanel.add(btnNewButton_1);
        
        comboBoxTiposListado = new JComboBox<>();
        comboBoxTiposListado.setBounds(482, 94, 89, 22);
        mainPanel.add(comboBoxTiposListado);
        
        textField = new JTextField();
        textField.setBounds(581, 94, 89, 23);
        mainPanel.add(textField);
        textField.setColumns(10);
        
        JLabel lblNewLabel_1 = new JLabel("Seleccione la columna a consultar: ");
        lblNewLabel_1.setBounds(472, 29, 280, 14);
        mainPanel.add(lblNewLabel_1);
        
        lblNewLabel_2 = new JLabel("Indique =,<,> Ejemplo: [>100] o [='mueble']");
        lblNewLabel_2.setBounds(472, 64, 280, 14);
        mainPanel.add(lblNewLabel_2);
        comboBoxTiposListado.addItem("ID");
        comboBoxTiposListado.addItem("Nombre");
        comboBoxTiposListado.addItem("Tipo");
        comboBoxTiposListado.addItem("Gama");
        comboBoxTiposListado.addItem("Cantidades");
        comboBoxTiposListado.addItem("Precios");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String textoIngresado = textField.getText();
                String tipoListadoSeleccionado = (String) comboBoxTiposListado.getSelectedItem();
                TipoListado tipoListado = obtenerTipoListadoDesdeString(tipoListadoSeleccionado);
                obtenerListadoProductos(modeloTabla, tipoListado, textoIngresado);
        	}
        });
        headerPanel.add(btnNewButton);
        headerPanel.add(btnNuevoProducto);
        headerPanel.add(btnSalir);
        
        // Obtener los datos de la tabla 'productos' de la base de datos
        obtenerProductos(modeloTabla,usuarioActivo);
    }

    // Método para obtener los productos de la base de datos
    private void obtenerProductos(DefaultTableModel modeloTabla, String usuarioActivo) {
        try {
            // Establecer la conexión con la base de datos
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "");

            // Consultar el id_usuario del usuario activo
            String id_user_sql = "SELECT id_usuario FROM usuarios WHERE username = '" + usuarioActivo + "'";
            Statement idStmt = conn.createStatement();
            ResultSet idRs = idStmt.executeQuery(id_user_sql);
            idRs.next(); // Mover el cursor al primer resultado (asumiendo que solo habrá uno)
            String id_usuario = idRs.getString("id_usuario");

            // Crear la consulta SQL para obtener los productos del usuario activo
            String productos_sql = "SELECT p.id_usuario, p.id, p.nombre, p.tipo, p.gama, p.cantidad, p.precio "
                    + "FROM productos p WHERE p.id_usuario = '" + id_usuario + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(productos_sql);

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

            // Cerrar recursos
            rs.close();
            stmt.close();
            idRs.close();
            idStmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de la base de datos.");
            e.printStackTrace();
        }
    }
    // Método para mostrar los usuarios en la tabla de productos
    private void mostrarUsuario(String usuarioActivo) {
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaProductos.getModel();
        modeloTabla.setRowCount(0); // Limpiar la tabla
        
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "")) {
            String sql = "SELECT id_usuario, username, telefono FROM usuarios";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id_usuario = rs.getInt("id_usuario");
                    String nombre = rs.getString("username");
                    int telefono = rs.getInt("telefono");
                    Object[] fila = {id_usuario, nombre, telefono}; // Vaciar las columnas innecesarias
                    modeloTabla.addRow(fila);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al obtener los datos de los usuarios.");
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.");
            ex.printStackTrace();
        }
        // Remover el panel principal actual y agregar uno nuevo con la tabla de usuarios
        miPanel.remove(mainPanel);
        
        // Crear el panel para mostrar los usuarios
        JPanel mainPanelUsuario = new JPanel();
        mainPanelUsuario.setBorder(BorderFactory.createTitledBorder("Datos del Usuario Activo"));
        miPanel.add(mainPanelUsuario, BorderLayout.CENTER);
        
        // Crear la tabla de usuarios
        String[] columnasUsuarios = {"ID Usuario", "Nombre", "Teléfono"};
        DefaultTableModel modeloTablaUsuarios = new DefaultTableModel(columnasUsuarios, 0);
        JTable tablaUsuarios = new JTable(modeloTablaUsuarios);
        JScrollPane scrollPaneUsuarios = new JScrollPane(tablaUsuarios);
        mainPanelUsuario.add(scrollPaneUsuarios);
        
        // Obtener y mostrar los usuarios
        obtenerUsuarios(modeloTablaUsuarios,usuarioActivo);

        // Refrescar la interfaz
        miPanel.revalidate();
        miPanel.repaint();
    }

    // Método para obtener y mostrar el usuario activo en la tabla
    private void obtenerUsuarios(DefaultTableModel modeloTablaUsuarios, String usuarioActivo) {
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "")) {
            String sql = "SELECT id_usuario, username, telefono FROM usuarios WHERE username = '"+usuarioActivo+"'";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id_usuario = rs.getInt("id_usuario");
                    String nombre = rs.getString("username");
                    int telefono = rs.getInt("telefono");
                    Object[] fila = {id_usuario, nombre, telefono};
                    modeloTablaUsuarios.addRow(fila);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al obtener los datos de los usuarios.");
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.");
            ex.printStackTrace();
        }
    }
    // Enumeración para los tipos de listados
    public enum TipoListado {
        ID, NOMBRE, TIPO, GAMA, CANTIDADES, PRECIOS
    }

    // Método para obtener listados de productos desde la base de datos
    private void obtenerListadoProductos(DefaultTableModel modeloTabla, TipoListado tipoListado,String textoIngresado) {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de llenarla nuevamente

        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "")) {
            String sql = construirConsultaSQL(tipoListado, textoIngresado);
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                	int id_usuario = rs.getInt("id_usuario");
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    String tipo = rs.getString("tipo");
                    String gama = rs.getString("gama");
                    int cantidad = rs.getInt("cantidad");
                    double precio = rs.getDouble("precio");
                    Object[] fila = {id_usuario, id, nombre, tipo, gama, cantidad, precio}; // Vaciar las columnas innecesarias
                    modeloTabla.addRow(fila);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al obtener los datos de los productos.");
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.");
            ex.printStackTrace();
        }
    }

    // Método para construir la consulta SQL según el tipo de listado seleccionado
    private String construirConsultaSQL(TipoListado tipoListado,String textoIngresado) {
        switch (tipoListado) {
            case ID:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos where id"+textoIngresado+
                		" ORDER BY id";
            case NOMBRE:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos where nombre"+textoIngresado+
                		" ORDER BY nombre";
            case TIPO:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos where tipo"+textoIngresado+
                		" ORDER BY tipo";
            case GAMA:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos where gama"+textoIngresado+
                		" ORDER BY gama";
            case CANTIDADES:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos where cantidad"+textoIngresado+
                		" ORDER BY cantidad";
            case PRECIOS:
                return "SELECT id_usuario, id, nombre, tipo, gama, cantidad, precio FROM productos where precio"+textoIngresado+
                		" ORDER BY precio";
            default:
                throw new IllegalArgumentException("Tipo de listado no válido.");
        }
    }
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
            case "Cantidades":
                return TipoListado.CANTIDADES;
            case "Precios":
                return TipoListado.PRECIOS;
            default:
                throw new IllegalArgumentException("Tipo de listado no válido: " + tipoListadoSeleccionado);
        }
    }
}
