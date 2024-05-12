package mypkt;

import java.sql.SQLException;
import java.util.Scanner;

public class Muebles_co {

	public static void main(String[] ags) {

		MenuDeAcceso menu = new MenuDeAcceso();
		Scanner ent = new Scanner(System.in);
		int opcion = 0;

		try {
			do {
				System.out.println("Muebles&Co:\n" + 
						"1 - Iniciar Sesion\n" + 
						"2 - Registrarse\n" + 
						"3 - Salir");
				
				opcion = Integer.parseInt(ent.nextLine());

				switch (opcion) {
					case 1 -> menu.iniciarSesion();
					case 2 -> menu.registrarse();
					case 3 -> System.out.println("Saliendo....");
					default -> System.out.println("Opcion no valida");
				}
			} while (opcion != 3);

		} catch (SQLException ex) {
			System.out.println("Error, no se puede conectar con la base de datos. " + ex.getMessage());
		}
		ent.close();
	}
}
