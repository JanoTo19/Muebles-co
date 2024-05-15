package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MenuPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel miPanel;
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JTable tablaProductos;

    public MenuPrincipal(String nombreUsuario) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        miPanel = new JPanel();
        miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        miPanel.setLayout(new BorderLayout());
        setContentPane(miPanel);

        // Crear el panel del encabezado (header)
        headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createTitledBorder("Usuario"));
        miPanel.add(headerPanel, BorderLayout.NORTH);

        // Etiqueta para mostrar el nombre de usuario
        JLabel lblNewLabel = new JLabel("Usuario logeado: " + nombreUsuario);
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
        headerPanel.add(btnSalir);

        // Crear el panel principal (main)
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Resumen"));
        miPanel.add(mainPanel, BorderLayout.CENTER);

        // Crear la tabla de productos
        String[] columnas = {"Nombre", "Tipo", "Gama", "Cantidad", "Precio"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        mainPanel.add(scrollPane);

        // Obtener los datos de la tabla 'productos' de la base de datos
        obtenerProductos(modeloTabla);
    }
    // Método para obtener los productos de la base de datos
    private void obtenerProductos(DefaultTableModel modeloTabla) {
        try {
            // Establecer la conexión con la base de datos
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "");

            // Crear la consulta SQL para obtener los productos
            String sql = "SELECT nombre, tipo, gama, cantidad, precio FROM productos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Llenar la tabla con los datos de los productos
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo");
                String gama = rs.getString("gama");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                Object[] fila = {nombre, tipo, gama, cantidad, precio};
                modeloTabla.addRow(fila);
            }

            // Cerrar recursos
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de la base de datos.");
            e.printStackTrace();
        }
    }
}