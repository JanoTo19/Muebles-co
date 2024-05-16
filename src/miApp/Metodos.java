package miApp;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import java.sql.Connection;

public class Metodos {
	// Método para eliminar un producto por su ID
	public void eliminarProductoPorID(String idProducto) {
	    try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "")) {
	        String sql = "DELETE FROM productos WHERE id = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, idProducto);
	            int filasAfectadas = stmt.executeUpdate();
	            if (filasAfectadas > 0) {
	                JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
	            } else {
	                JOptionPane.showMessageDialog(null, "No se encontró un producto con el ID especificado.");
	            }
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(null, "Error al eliminar el producto.");
	            ex.printStackTrace();
	        }
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.");
	        ex.printStackTrace();
	    }
	}

	public void modificarCantidadProducto(int idProducto, int cantidadOperacion, String operacion) {
	    try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proyectotienda", "root", "")) {
	        // Verificar si hay suficientes unidades para realizar la operación
	        String checkSql = "SELECT cantidad FROM productos WHERE id = ?";
	        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
	            checkStmt.setInt(1, idProducto);
	            ResultSet rs = checkStmt.executeQuery();
	            if (rs.next()) {
	                int cantidadActual = rs.getInt("cantidad");
	                int nuevaCantidad;
	                // Determinar la nueva cantidad según la operación
	                if (operacion.equals("+")) {
	                    nuevaCantidad = cantidadActual + cantidadOperacion;
	                    JOptionPane.showMessageDialog(null, "Cantidad del producto sumada correctamente.\nNueva cantidad: " + nuevaCantidad);
	                } else if (operacion.equals("-")) {
	                    if (cantidadActual >= cantidadOperacion) {
	                        nuevaCantidad = cantidadActual - cantidadOperacion;
	                        JOptionPane.showMessageDialog(null, "Cantidad del producto restada correctamente.\nNueva cantidad: " + nuevaCantidad);
	                    } else {
	                        JOptionPane.showMessageDialog(null, "No se puede realizar la operación. La cantidad a restar es mayor que las unidades disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
	                            System.out.println("No hay suficientes unidades para restar.");
	                        return; // Salir del método si no hay suficientes unidades para restar
	                    }
	                } else {
	                    System.out.println("Operación no válida.");
	                    return; // Salir del método si la operación no es válida
	                }
	                // Actualizar la cantidad en la base de datos
	                String updateSql = "UPDATE productos SET cantidad = ? WHERE id = ?";
	                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
	                    pstmt.setInt(1, nuevaCantidad);
	                    pstmt.setInt(2, idProducto);
	                    int rowsAffected = pstmt.executeUpdate();
	                    if (rowsAffected > 0) {
	                        System.out.println("Cantidad del producto actualizada correctamente.");


	                    } else {
	                        System.out.println("No se encontró ningún producto con ese ID.");
	                    }
	                } catch (SQLException ex) {
	                    System.out.println("Error al actualizar la cantidad del producto.");
	                    ex.printStackTrace();
	                }
	            } else {
	                System.out.println("No se encontró ningún producto con ese ID.");
	            }
	        } catch (SQLException ex) {
	            System.out.println("Error al verificar la cantidad del producto.");
	            ex.printStackTrace();
	        }
	    } catch (SQLException ex) {
	        System.out.println("Error de conexión a la base de datos.");
	        ex.printStackTrace();
	    }
	}

}
