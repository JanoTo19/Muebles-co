package miApp;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

    public AltaProducto() {
        // Propiedades del marco
        setTitle("Alta Producto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);

        // Contenedor del frame
        miPanel = new JPanel();
        miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(miPanel);
        miPanel.setLayout(null);

        // Etiquetas y campos de texto para ingresar datos del producto
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 30, 150, 30);
        lblNombre.setFont(miFont);
        miPanel.add(lblNombre);

        textField_nombre = new JTextField();
        textField_nombre.setBounds(200, 30, 300, 30);
        textField_nombre.setFont(miFont);
        miPanel.add(textField_nombre);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(50, 80, 150, 30);
        lblTipo.setFont(miFont);
        miPanel.add(lblTipo);

        textField_tipo = new JTextField();
        textField_tipo.setBounds(200, 80, 300, 30);
        textField_tipo.setFont(miFont);
        miPanel.add(textField_tipo);

        JLabel lblGama = new JLabel("Gama:");
        lblGama.setBounds(50, 130, 150, 30);
        lblGama.setFont(miFont);
        miPanel.add(lblGama);

        textField_gama = new JTextField();
        textField_gama.setBounds(200, 130, 300, 30);
        textField_gama.setFont(miFont);
        miPanel.add(textField_gama);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(50, 180, 150, 30);
        lblCantidad.setFont(miFont);
        miPanel.add(lblCantidad);

        textField_cantidad = new JTextField();
        textField_cantidad.setBounds(200, 180, 300, 30);
        textField_cantidad.setFont(miFont);
        miPanel.add(textField_cantidad);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(50, 230, 150, 30);
        lblPrecio.setFont(miFont);
        miPanel.add(lblPrecio);

        textField_precio = new JTextField();
        textField_precio.setBounds(200, 230, 300, 30);
        textField_precio.setFont(miFont);
        miPanel.add(textField_precio);

        // Botón para dar de alta el producto
        JButton btnAltaProducto = new JButton("Dar de Alta Producto");
        btnAltaProducto.setFont(miFont);
        btnAltaProducto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altaProducto();
            }
        });
        btnAltaProducto.setBounds(200, 280, 300, 30);
        miPanel.add(btnAltaProducto);
    }

    private void altaProducto() {
        // Obtener los datos del producto desde los campos de texto
        String nombre = textField_nombre.getText();
        String tipo = textField_tipo.getText();
        String gama = textField_gama.getText();
        int cantidad = Integer.parseInt(textField_cantidad.getText());
        double precio = Double.parseDouble(textField_precio.getText());

        // Insertar el producto en la base de datos
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/tu_basededatos", "usuario", "contraseña")) {
            String sql = "INSERT INTO productos (nombre, tipo, gama, cantidad, precio) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, tipo);
                pstmt.setString(3, gama);
                pstmt.setInt(4, cantidad);
                pstmt.setDouble(5, precio);
                pstmt.executeUpdate();
                System.out.println("Producto registrado correctamente.");
            } catch (SQLException ex) {
                System.out.println("Error al insertar el producto en la base de datos.");
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            System.out.println("Error de conexión a la base de datos.");
            ex.printStackTrace();
        }
    }

}
