package miApp;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
    private JTextField textField_gama;
    private JTextField textField_cantidad;
    private JTextField textField_precio;
    private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);
    private JPanel mainPanel;
    
    public AltaProducto(String usuarioActivo) {
        // Propiedades del marco
        setTitle("Alta Producto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);

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
		
		        textField_nombre = new JTextField();
		        textField_nombre.setBounds(188, 52, 300, 30);
		        mainPanel.add(textField_nombre);
		        textField_nombre.setFont(miFont);
		
        // Etiquetas y campos de texto para ingresar datos del producto
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(39, 52, 150, 30);
        mainPanel.add(lblNombre);
        lblNombre.setFont(miFont);
        
                textField_tipo = new JTextField();
                textField_tipo.setBounds(188, 93, 300, 30);
                mainPanel.add(textField_tipo);
                textField_tipo.setFont(miFont);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(39, 93, 150, 30);
        mainPanel.add(lblTipo);
        lblTipo.setFont(miFont);
        
                textField_gama = new JTextField();
                textField_gama.setBounds(188, 134, 300, 30);
                mainPanel.add(textField_gama);
                textField_gama.setFont(miFont);

        JLabel lblGama = new JLabel("Gama:");
        lblGama.setBounds(39, 134, 150, 30);
        mainPanel.add(lblGama);
        lblGama.setFont(miFont);
        
                textField_cantidad = new JTextField();
                textField_cantidad.setBounds(188, 175, 300, 30);
                mainPanel.add(textField_cantidad);
                textField_cantidad.setFont(miFont);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(39, 175, 150, 30);
        mainPanel.add(lblCantidad);
        lblCantidad.setFont(miFont);
        
                textField_precio = new JTextField();
                textField_precio.setBounds(188, 216, 300, 30);
                mainPanel.add(textField_precio);
                textField_precio.setFont(miFont);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(39, 216, 150, 30);
        mainPanel.add(lblPrecio);
        lblPrecio.setFont(miFont);

        // Botón para dar de alta el producto
        JButton btnAltaProducto = new JButton("Dar de Alta Producto");
        btnAltaProducto.setBounds(188, 273, 300, 30);
        mainPanel.add(btnAltaProducto);
        btnAltaProducto.setFont(miFont);
        btnAltaProducto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altaProducto(usuarioActivo);
                AltaProducto ventanaAltaProducto = new AltaProducto(usuarioActivo);
                dispose(); //borra la ventan de Login
                ventanaAltaProducto.setVisible(true);
            }
        });
        
        //Botón para volver al menu principal
        JButton btnNewButton = new JButton("Volver");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                MenuPrincipal ventanaMenuPrincipal = new MenuPrincipal(usuarioActivo);
                dispose(); //borra la ventan de Login
                ventanaMenuPrincipal.setVisible(true);
        	}
        });
        btnNewButton.setBounds(39, 273, 89, 30);
        mainPanel.add(btnNewButton);
        btnNewButton.setFont(miFont);

        
    }

    private void altaProducto(String usuarioActivo) {
        // Obtener los datos del producto desde los campos de texto
        String nombre = textField_nombre.getText();
        String tipo = textField_tipo.getText();
        String gama = textField_gama.getText();
        int cantidad = Integer.parseInt(textField_cantidad.getText());
        double precio = Double.parseDouble(textField_precio.getText());

        // Consultar el id_usuario del usuario activo
        int idUsuarioActivo = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "")) {
            String idUserSql = "SELECT id_usuario FROM usuarios WHERE username = ?";
            try (PreparedStatement idUserStmt = conn.prepareStatement(idUserSql)) {
                idUserStmt.setString(1, usuarioActivo);
                ResultSet idUserRs = idUserStmt.executeQuery();
                if (idUserRs.next()) {
                    idUsuarioActivo = idUserRs.getInt("id_usuario");
                }
            } catch (SQLException ex) {
                System.out.println("Error al consultar el id_usuario del usuario activo.");
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            System.out.println("Error de conexión a la base de datos.");
            ex.printStackTrace();
        }

        if (idUsuarioActivo != 0) {
            // Insertar el producto en la base de datos
            try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "")) {
                String sql = "INSERT INTO productos (nombre, tipo, gama, cantidad, precio, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, tipo);
                    pstmt.setString(3, gama);
                    pstmt.setInt(4, cantidad);
                    pstmt.setDouble(5, precio);
                    pstmt.setInt(6, idUsuarioActivo); // Utiliza setInt para id_usuario
                    pstmt.executeUpdate();
                    System.out.println("Producto registrado correctamente.");
                    JOptionPane.showMessageDialog(this, "El producto se añadió correctamente.");
                } catch (SQLException ex) {
                    System.out.println("Error al insertar el producto en la base de datos.");
                    ex.printStackTrace();
                }
            } catch (SQLException ex) {
                System.out.println("Error de conexión a la base de datos.");
                ex.printStackTrace();
            }
        } else {
            System.out.println("No se pudo obtener el id_usuario del usuario activo.");
        }
    }
}
