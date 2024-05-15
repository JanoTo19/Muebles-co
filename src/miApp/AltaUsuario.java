package miApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AltaUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel miPanel;
	private JTextField textField_username;
	private JPasswordField textField_password;
	private final Font miFont = new Font("Tahoma", Font.PLAIN, 20);
	private JPasswordField passwordField;
    private JPanel mainPanel;
    private JButton btnLogin;
    
	public AltaUsuario(String nombreUsuario) {
		//pROPIEDADES DEL MARCO
		setTitle("AltaUsuario APP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		//Contenedor del frame
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); //esto no es necesario
		//Indicamos el contenedor del frame
		setContentPane(miPanel);
		miPanel.setLayout(null);
		
        // Crear el panel principal (main)
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Alta de Usuario"));
        mainPanel.setBounds(10, 11, 416, 241);
        miPanel.add(mainPanel);
        		mainPanel.setLayout(null);
        
        		//Botón de registro con la contraseña y usuario - ver apuntes de actionlistener y demás opciones
        		JButton btnRegistro = new JButton("Registrarse");
        		btnRegistro.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				altaUsuario();
        			}
        		});
        		btnRegistro.setBounds(250, 176, 150, 33);
        		mainPanel.add(btnRegistro);
        		btnRegistro.setFont(miFont);
        		
        		passwordField = new JPasswordField();
        		passwordField.setBounds(200, 119, 200, 30);
        		mainPanel.add(passwordField);
        		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        		passwordField.setColumns(10);
        		
        		JLabel lblPassword_1 = new JLabel("Repetir Contraseña:");
        		lblPassword_1.setBounds(20, 119, 184, 30);
        		mainPanel.add(lblPassword_1);
        		lblPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        		
        		textField_password = new JPasswordField();
        		textField_password.setBounds(200, 78, 200, 30);
        		mainPanel.add(textField_password);
        		textField_password.setFont(miFont);
        		textField_password.setColumns(10);
        		
        		JLabel lblPassword = new JLabel("Contraseña:");
        		lblPassword.setBounds(88, 78, 116, 30);
        		mainPanel.add(lblPassword);
        		lblPassword.setFont(miFont);
        		
        		textField_username = new JTextField();
        		textField_username.setBounds(200, 37, 200, 30);
        		mainPanel.add(textField_username);
        		textField_username.setFont(miFont);
        		textField_username.setColumns(10);
        		//Añadimos componentes al panel
        		JLabel lblUsername = new JLabel("Usuario:");
        		lblUsername.setBounds(88, 37, 116, 30);
        		mainPanel.add(lblUsername);
        		lblUsername.setFont(miFont);
        		
        		btnLogin = new JButton("Login");
        		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
        		btnLogin.setBounds(73, 176, 131, 33);
        		mainPanel.add(btnLogin);
        		btnLogin.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
                        Login ventanaLogin = new Login();
                        dispose(); //borra la ventana de altaUsuario
                        ventanaLogin.setVisible(true);
        			}
        		});
		
	}
	private void altaUsuario() {
		String username = textField_username.getText();
		String password= new String (textField_password.getPassword());
		String confirmPassword = new String(passwordField.getPassword());
		
		if (!(username.isBlank()||password.isBlank()||confirmPassword.isBlank())) {
	        if (!password.equals(confirmPassword)) {
	            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
	            return;
	        }else {
			try {
                // Verificar si el usuario ya existe
                if (existeUsuario(username)) {
                    JOptionPane.showMessageDialog(this, "Usuario ya registrado. Por favor, elija otro nombre de usuario.");
                    return;
                }
                // Proceder con el registro del usuario
    			BaseDatos bbdd= new BaseDatos(); 
		        // Cifrar la contraseña
		        String contraseñaCifrada = Login.cifrarContraseña(password);
				if (!bbdd.loginDB(textField_username.getText(),contraseñaCifrada)) {
					System.out.println("Procedemos a registrar");
					//HAY QUE COMPARAR LAS DOS CONTRASEÑAS PARA QUE COINCIDAN Y VALIDARLAS
					 try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda",
							 "root", "")) {
				            // Preparar la consulta SQL para insertar el usuario
				            String sql = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
				            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				                pstmt.setString(1, textField_username.getText());
				                pstmt.setString(2, contraseñaCifrada);
				                pstmt.executeUpdate();
				                System.out.println("Usuario registrado correctamente.");
				                MenuPrincipal ventanaPrincipal= new MenuPrincipal(textField_username.getText());
				                this.setVisible(false);
				                ventanaPrincipal.setVisible(true);
				            } catch (SQLException e) {
								System.out.println("Error de conexión a la base de datos");
							}
				        }
				}else {
					System.out.println("No se puede registrar");
		            JOptionPane.showMessageDialog(this, "Usuario ya registrado. Vaya al Login.");
		            return;
				}
				
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	//			bbdd.cerrarBD();
	        	
		        }
		}
	}
	private boolean existeUsuario(String username) throws SQLException {
	    boolean existe = false;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "");
	        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, username);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            int count = rs.getInt(1);
	            existe = count > 0;
	        }
	    } finally {
	        // Cerrar recursos en un bloque finally para garantizar que se cierren adecuadamente
	        if (rs != null) {
	            rs.close();
	        }
	        if (pstmt != null) {
	            pstmt.close();
	        }
	        if (conn != null) {
	            conn.close();
	        }
	    }

	    return existe;
	}

}
