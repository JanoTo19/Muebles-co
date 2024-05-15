package graphical;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Aplication {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Aplication window = new Aplication();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Aplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Test_table m=new Test_table();
		String tiempo="";
		frame = new JFrame();
		frame.setBounds(100, 100, 730, 548);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(277, 11, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Salir");
		btnNewButton.setBounds(56, 39, 123, 45);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnMostrarProductos = new JButton("Mostrar productos");
		btnMostrarProductos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnMostrarProductos.setBounds(431, 39, 228, 45);
		frame.getContentPane().add(btnMostrarProductos);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		table.repaint();
		table.setModel(new DefaultTableModel(
			new String[][] {
				{m.geta(),m.getb()},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"New column", "New column"
			}
		));
		table.setBounds(77, 156, 571, 255);
		frame.getContentPane().add(table);
		
		JButton btnAadir = new JButton("Añadir");
		btnAadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Test(table);
			}
		});
		btnAadir.setToolTipText("Añadir");
		btnAadir.setBounds(235, 39, 123, 45);
		frame.getContentPane().add(btnAadir);
		

	 
	        
	}void Test(JTable table){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        String[] cols = {"Id","First Name","Last Name","Age"};
        String[][] data = { 
                            {"1", "FN1","LN1","10"},
                            {"2", "FN1","LN1","15"},
                            {"3", "FN1","LN1","20"},
                            {"4", "FN1","LN1","25"},
                            {"5", "FN1","LN1","30"},
                            {"6", "FN1","LN1","35"},
                            {"7", "FN1","LN1","40"},
                            {"8", "FN1","LN1","45"},
                            {"9", "FN1","LN1","50"},
                            {"10", "FN1","LN1","60"}
                          };
        
        model.setDataVector(data, cols);
        
        
    }
}
