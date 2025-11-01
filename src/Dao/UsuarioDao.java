package Dao;

import Conexion.Conexion;
import Modelo.Rol;
import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Michael Ramos;
 *
 */
public class UsuarioDao {

    public Usuario validarUsuario(String usuario, String contrasena) {
        // Traemos también el id_rol para construir el objeto Rol completo
        String sql = "SELECT u.id_usuario, u.nombre_usuario, r.id_rol, r.nombre_rol "
                + "FROM usuarios u "
                + "JOIN roles r ON u.id_rol = r.id_rol "
                + "WHERE u.nombre_usuario = ? AND u.contrasena = ?";

        Usuario user = null;

        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, contrasena);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 1. Creamos el objeto Rol con sus datos
                    Rol rolUsuario = new Rol();
                    rolUsuario.setIdRol(rs.getInt("id_rol"));
                    rolUsuario.setNombreRol(rs.getString("nombre_rol"));

                    // 2. Creamos el objeto Usuario
                    user = new Usuario();
                    user.setId(rs.getInt("id_usuario"));
                    user.setNombreUsuario(rs.getString("nombre_usuario"));

                    // 3. Asignamos el objeto Rol completo al Usuario
                    user.setRol(rolUsuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
        }
        return user;
    }

    /**
     * Verifica si un correo electrónico ya existe en la base de datos. Lo usa
     * 'RecuperarContrasenaControlador'.
     *
     * @param correo El email a verificar.
     * @return true si el email existe, false en caso contrario.
     */
    public boolean verificarEmailExiste(String correo) {

        // Ajusta 'usuarios' y 'email' si tu tabla o columna se llaman diferente
        String sql = "SELECT COUNT(1) FROM usuarios WHERE email = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si el conteo (rs.getInt(1)) es mayor a 0, el email existe
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si el código de recuperación coincide con el del email. Lo usa
     * 'IngresarCodigoControlador'.
     *
     * @param correo El email del usuario.
     * @param codigo El código de 6 dígitos ingresado.
     * @return true si el código es correcto, false en caso contrario.
     */
    public boolean verificarCodigo(String correo, String codigo) {

        // Asume que tienes una columna 'codigo_recuperacion'
        // Ajusta los nombres si es necesario
        String sql = "SELECT COUNT(1) FROM usuarios WHERE email = ? AND codigo_recuperacion = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si el conteo es > 0, el email Y el código coinciden
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar código: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza la contraseña del usuario en la base de datos y limpia el
     * código. Lo usa 'ActualizarContrasenaControlador'.
     *
     * @param correo El email del usuario a actualizar.
     * @param nuevaPass La nueva contraseña (¡idealmente encriptada!).
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarContraseña(String correo, String nuevaPass) {

        // Esta consulta también pone el 'codigo_recuperacion' en NULL
        // para que no se pueda volver a usar.
        String sql = "UPDATE usuarios SET contrasena = ?, codigo_recuperacion = NULL WHERE email = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // 🔒 IMPORTANTE: Aquí deberías encriptar 'nuevaPass' antes de guardarla
            ps.setString(1, nuevaPass);
            ps.setString(2, correo);

            // executeUpdate() devuelve el número de filas afectadas
            int filasAfectadas = ps.executeUpdate();

            // Si se afectó 1 fila, la actualización fue exitosa
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
        }
        return false;
    }

    /**
     * Guarda el código de recuperación temporal en la base de datos.
     *
     * @param correo El email del usuario.
     * @param codigo El código de 6 dígitos generado.
     * @return true si se guardó con éxito, false si no.
     */
    public boolean guardarCodigoTemporal(String correo, String codigo) {

        // Asume que tu columna se llama 'codigo_recuperacion'
        String sql = "UPDATE usuarios SET codigo_recuperacion = ? WHERE email = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setString(2, correo);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // true si se actualizó 1 fila

        } catch (SQLException e) {
            System.err.println("Error al guardar código temporal: " + e.getMessage());
            return false;
        }
    }

}
