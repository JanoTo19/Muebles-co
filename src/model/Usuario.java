package model;

public class Usuario {
	
	private String nombre,contraseña;
	
	public Usuario() {
		this("", "");
	}
	
	public Usuario(String nombre, String contraseña) {
		this.nombre = nombre;
		this.contraseña = contraseña;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	@Override
	public String toString() {
		return "Usuario:\nnombre: " + nombre + "\nContraseña: " + contraseña;
	}

}
