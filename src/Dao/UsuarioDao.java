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

    public Usuario validarUsuario(String nombreUsuario, String contrasena) {
        final String sql
                = "SELECT u.id_usuario, u.nombre_usuario, u.contrasena, "
                + "       u.id_rol, r.nombre_rol, u.id_persona "
                + "FROM usuarios u "
                + "JOIN roles r ON r.id_rol = u.id_rol "
                + "WHERE u.nombre_usuario = ? AND u.contrasena = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("id_rol"));
                    rol.setNombreRol(rs.getString("nombre_rol"));

                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    u.setNombreUsuario(rs.getString("nombre_usuario"));
                    u.setRol(rol);
                    // u.setIdPersona(rs.getInt("id_persona")); 
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("validarUsuario: " + e.getMessage());
        }
        return null;
    }

    public boolean verificarEmailExiste(String correo) {
        final String sql
                = "SELECT COUNT(1) "
                + "FROM personas p "
                + "JOIN usuarios u ON u.id_persona = p.id_persona "
                + "WHERE p.correo_electronico = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("verificarEmailExiste: " + e.getMessage());
            return false;
        }
    }

    public boolean guardarCodigoTemporal(String correo, String codigo6) {
        if (codigo6 == null || !codigo6.matches("\\d{6}")) {
            return false;
        }

        final String sqlFindUser
                = "SELECT u.id_usuario "
                + "FROM usuarios u "
                + "JOIN personas p ON p.id_persona = u.id_persona "
                + "WHERE p.correo_electronico = ?";

        final String sqlInsertToken
                = "INSERT INTO tokens_recuperacion (id_usuario, token, fecha_expiracion, usado) "
                + "VALUES (?, ?, NOW() + INTERVAL '10 minutes', FALSE)";

        try (Connection con = Conexion.getConnection()) {
            Integer idUsuario = null;

            try (PreparedStatement ps = con.prepareStatement(sqlFindUser)) {
                ps.setString(1, correo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idUsuario = rs.getInt(1);
                    }
                }
            }
            if (idUsuario == null) {
                System.err.println("guardarCodigoTemporal: no existe usuario para " + correo);
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sqlInsertToken)) {
                ps.setInt(1, idUsuario);
                ps.setString(2, codigo6);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("guardarCodigoTemporal: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarCodigo(String correo, String codigo6) {
        if (codigo6 == null || !codigo6.matches("\\d{6}")) {
            return false;
        }

        final String sqlSelectValid
                = "SELECT t.id_token "
                + "FROM tokens_recuperacion t "
                + "JOIN usuarios u ON u.id_usuario = t.id_usuario "
                + "JOIN personas p ON p.id_persona = u.id_persona "
                + "WHERE p.correo_electronico = ? "
                + "  AND t.token = ? "
                + "  AND t.usado = FALSE "
                + "  AND t.fecha_expiracion > NOW() "
                + "ORDER BY t.id_token DESC "
                + "LIMIT 1";

        final String sqlMarkUsed
                = "UPDATE tokens_recuperacion SET usado = TRUE WHERE id_token = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement psSel = con.prepareStatement(sqlSelectValid)) {

            psSel.setString(1, correo);
            psSel.setString(2, codigo6);

            Integer idToken = null;
            try (ResultSet rs = psSel.executeQuery()) {
                if (rs.next()) {
                    idToken = rs.getInt(1);
                }
            }
            if (idToken == null) {
                return false;
            }

            try (PreparedStatement psUpd = con.prepareStatement(sqlMarkUsed)) {
                psUpd.setInt(1, idToken);
                psUpd.executeUpdate();
            }
            return true;

        } catch (SQLException e) {
            System.err.println("verificarCodigo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarContraseña(String correo, String nuevaPass) {
        final String sqlUpdatePass
                = "UPDATE usuarios SET contrasena = ? "
                + "WHERE id_persona = (SELECT id_persona FROM personas WHERE correo_electronico = ?)";

        final String sqlDeleteTokens
                = "DELETE FROM tokens_recuperacion "
                + "WHERE id_usuario = ("
                + "  SELECT u.id_usuario FROM usuarios u "
                + "  JOIN personas p ON p.id_persona = u.id_persona "
                + "  WHERE p.correo_electronico = ?"
                + ")";

        try (Connection con = Conexion.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement(sqlUpdatePass)) {
                ps.setString(1, nuevaPass);
                ps.setString(2, correo);
                if (ps.executeUpdate() == 0) {
                    return false;
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlDeleteTokens)) {
                ps.setString(1, correo);
                ps.executeUpdate();
            }
            return true;

        } catch (SQLException e) {
            System.err.println("actualizarContraseña: " + e.getMessage());
            return false;
        }
    }

    private Integer findUserIdByCorreo(Connection con, String correo) throws SQLException {
        final String sql
                = "SELECT u.id_usuario "
                + "FROM usuarios u "
                + "JOIN personas p ON p.id_persona = u.id_persona "
                + "WHERE p.correo_electronico = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }
}
