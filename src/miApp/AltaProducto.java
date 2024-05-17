package miApp;

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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AltaProducto extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel miPanel;
    private JTextField textField_nombre;
    private JTextField textField_tipo;
    private JTextField textField_cantidad;
    private JTextField textField_precio;
    private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);
    private JPanel mainPanel;
    private JComboBox<String> comboBoxTiposGama;
    
    public AltaProducto(String usuarioActivo) {
    	// Contenedor del frame
        miPanel = new JPanel();
        miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(miPanel);
        miPanel.setLayout(null);
    	
     // Crear el panel principal (main)
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Alta de Producto"));
        mainPanel.setBounds(10, 11, 566, 341);
        miPanel.add(mainPanel);
		mainPanel.setLayout(null);
    	
    	// Propiedades del marco
        setTitle("Alta Producto");
    	setBounds(100, 100, 600, 400);
    	
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	try {
			Image img = ImageIO.read(new File("./src/files/Icono-App.png"));
			setIconImage(img);
		} catch (IOException e) {
			e.getMessage();
		}
    	
    	// Etiquetas y campos de texto para ingresar datos del producto
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(39, 52, 150, 30);
        mainPanel.add(lblNombre);
        lblNombre.setFont(miFont);
    	
    	JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(39, 93, 150, 30);
        mainPanel.add(lblTipo);
        lblTipo.setFont(miFont);
    	
    	JLabel lblGama = new JLabel("Gama:");
        lblGama.setBounds(39, 134, 150, 30);
        mainPanel.add(lblGama);
        lblGama.setFont(miFont);
    	
    	JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(39, 175, 150, 30);
        mainPanel.add(lblCantidad);
        lblCantidad.setFont(miFont);
    	
    	JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(39, 216, 150, 30);
        mainPanel.add(lblPrecio);
        lblPrecio.setFont(miFont);
    	
    	textField_nombre = new JTextField();
        textField_nombre.setBounds(188, 52, 300, 30);
        mainPanel.add(textField_nombre);
        textField_nombre.setFont(miFont);
    	
    	textField_tipo = new JTextField();
        textField_tipo.setBounds(188, 93, 300, 30);
        mainPanel.add(textField_tipo);
        textField_tipo.setFont(miFont);
        
        comboBoxTiposGama = new JComboBox<>();
        comboBoxTiposGama.setBounds(188, 134, 300, 30);
        mainPanel.add(comboBoxTiposGama);
        comboBoxTiposGama.addItem("Alta");
        comboBoxTiposGama.addItem("Media");
        comboBoxTiposGama.addItem("Baja");
        
        textField_cantidad = new JTextField();
        textField_cantidad.setBounds(188, 175, 300, 30);
        mainPanel.add(textField_cantidad);
        textField_cantidad.setFont(miFont);

        textField_precio = new JTextField();
        textField_precio.setBounds(188, 216, 300, 30);
        mainPanel.add(textField_precio);
        textField_precio.setFont(miFont);
        
        // Botón para dar de alta el producto
        JButton btnAltaProducto = new JButton("Dar de Alta Producto");
        btnAltaProducto.setBounds(188, 273, 300, 30);
        mainPanel.add(btnAltaProducto);
        btnAltaProducto.setFont(miFont);
		
        //Botón para volver al menu principal
        JButton btnNewButton = new JButton("Volver");
        btnNewButton.setBounds(39, 273, 89, 30);
        mainPanel.add(btnNewButton);
        btnNewButton.setFont(miFont);
        
        btnAltaProducto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		altaProducto(usuarioActivo);
            	}catch (NumberFormatException ex) {
            		JOptionPane.showMessageDialog(null, "Error dato invalido. " + ex.getMessage(),"Error!!",JOptionPane.ERROR_MESSAGE);
				}
                AltaProducto ventanaAltaProducto = new AltaProducto(usuarioActivo);
                dispose(); //borra la ventan de Login
                ventanaAltaProducto.setVisible(true);
            }
        });

        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                MenuPrincipal ventanaMenuPrincipal = new MenuPrincipal(usuarioActivo);
                dispose(); //borra la ventan de Login
                ventanaMenuPrincipal.setVisible(true);
        	}
        });         
    }

    private void altaProducto(String usuarioActivo) throws NumberFormatException {
        // Obtener los datos del producto desde los campos de texto
        String nombre = textField_nombre.getText();
        String tipo = textField_tipo.getText();
        String gama = (String) comboBoxTiposGama.getSelectedItem();  // Obtener el valor seleccionado del JComboBox
        int cantidad = Integer.parseInt(textField_cantidad.getText());
        double precio = Double.parseDouble(textField_precio.getText());

        // Validar los datos del producto
        if (nombre.isEmpty() || tipo.isEmpty() || gama == null || cantidad <= 0 || precio <= 0) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos correctamente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try(Connection conn = BaseDatos.getConnection()) {
            // Obtener el id del usuario activo
            String idUsuarioSql = "SELECT id_usuario FROM usuarios WHERE username = ?";
            int idUsuario;
            try (PreparedStatement pstmt = conn.prepareStatement(idUsuarioSql)) {
                pstmt.setString(1, usuarioActivo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        idUsuario = rs.getInt("id_usuario");
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Obtener el siguiente ID del producto
            int siguienteIdProducto = obtenerSiguienteIdProducto();

            // Insertar el nuevo producto en la base de datos
            String sql = "INSERT INTO productos (id, id_usuario, nombre, tipo, gama, cantidad, precio) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, siguienteIdProducto);
                pstmt.setInt(2, idUsuario);
                pstmt.setString(3, nombre);
                pstmt.setString(4, tipo);
                pstmt.setString(5, gama);
                pstmt.setInt(6, cantidad);
                pstmt.setDouble(7, precio);
                pstmt.executeUpdate();
            }

            // Mostrar un mensaje de éxito
            JOptionPane.showMessageDialog(this, "Producto agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar los campos de texto después de la inserción
            textField_nombre.setText("");
            textField_tipo.setText("");
            comboBoxTiposGama.setSelectedIndex(0);
            textField_cantidad.setText("");
            textField_precio.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
 // Método para obtener el siguiente ID de producto
    private int obtenerSiguienteIdProducto() {
        int siguienteId = 1; // Valor predeterminado si la tabla está vacía
        try(Connection conn = BaseDatos.getConnection()) {
            String sql = "SELECT MAX(id) AS max_id FROM productos";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    int maxId = rs.getInt("max_id");
                    if (!rs.wasNull()) {
                        siguienteId = maxId + 1;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener el siguiente ID del producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return siguienteId;
    }
}
