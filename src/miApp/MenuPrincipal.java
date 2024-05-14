package miApp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel miPanel;


	/**
	 * Create the frame.
	 */
	public MenuPrincipal(String nombreUsuario) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		miPanel = new JPanel();
		miPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(miPanel);
		miPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Usuario logeado: "+nombreUsuario);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(69, 31, 315, 35);
		miPanel.add(lblNewLabel);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login frame = new Login();//llamamos a la ventana Login
				frame.setVisible(true);                
				dispose(); //borra la ventana del menu principal
			}
		});
		btnVolver.setBounds(344, 278, 89, 23);
		miPanel.add(btnVolver);
	}
}
