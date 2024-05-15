package controller;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cifrado {
	
private static String llave = "muebles&co";
	
	public static String encriptar(String encriptar) {
		try {
			SecretKeySpec se = generadorClave(llave);
			Cipher c = Cipher.getInstance("AES");
			
			c.init(Cipher.ENCRYPT_MODE, se);
			
			byte[] cadena = encriptar.getBytes("UTF-8");
			byte[] encriptada = c.doFinal(cadena);
			String cadenaEncriptada = Base64.getEncoder().encodeToString(encriptada);
			return cadenaEncriptada;
		} catch (Exception e) {
			return "";
		}
	}

	
	public static String desencriptar(String desencriptar) {
		try {
			SecretKeySpec se = generadorClave(llave);
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, se);
			
			byte[] cadena = Base64.getDecoder().decode(desencriptar);
			byte[] desencriptacion = c.doFinal(cadena);
			String cadenaDesencriptada = new String(desencriptacion);
			return cadenaDesencriptada;
		} catch (Exception e) {
			return "";
		}
	}
	
	public static SecretKeySpec generadorClave(String llave) {
		
		try {
			byte [] cadena = llave.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			
			cadena = md.digest(cadena);
			cadena = Arrays.copyOf(cadena, 16);
			
			SecretKeySpec se = new SecretKeySpec(cadena, "AES");
			return se;
			
		} catch (Exception e) {
			return null;
		}
		
	}

}
