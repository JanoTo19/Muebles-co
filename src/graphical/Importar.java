package graphical;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Importar {
	//Intended porpouse of importing data from files
	//Importar datos
	//Excepciones deben ser capturadas en la clase que utilize esta clase
	public static void main(String args[]) {
		try {
			try {
				Lectura("Libro1.csv");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			insertar("2","1","1","1","1");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}public static void Lectura(String file) throws FileNotFoundException,IOException{//Lee datos de un fichero
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String o;
		String[] datos;
		
		
		while(br.ready()) {
			o=br.readLine();
			datos=((o.split(";")));
			try {
				insertar(datos[0],datos[1],datos[2],datos[3],datos[4]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}public static void insertar(String ID,String Nombre,String Descripcion,String Precio,String Stock) throws SQLException{ //conexion con la base de datos
		String b="proyectotienda";//parametros locales(Deben ser cambiados)
		String d="jdbc:mariadb://127.0.0.1:3306/" +b ;
		String user="root";
		String pw="";
		//objetos para la conexion de la base de datos
		Connection con=null;
		Statement s=null;
		ResultSet rs=null;
		int tablas=0;//no se que hace esto
		//
		con = DriverManager.getConnection(d,user,pw);//
		String sql = "INSERT INTO productos (id_Producto,Nombre,Descripcion,precio,stock) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        //yay insercion a la base de datos
        pstmt.setString(1, ID);
        pstmt.setString(2, Nombre);
        pstmt.setString(3, Descripcion);
        pstmt.setString(4, Precio);
        pstmt.setString(5, Stock);
        pstmt.executeUpdate();//consulta bing chiling
		
	}
}
