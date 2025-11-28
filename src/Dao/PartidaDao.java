package Dao;

import Conexion.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Modelo.CuentaItem;
import Modelo.DetallePartida;

/**
 *
 * @author Michael Ramos;
 *
 */
public class PartidaDao {

    public List<CuentaItem> obtenerCuentasParaCombo() {
        List<CuentaItem> lista = new ArrayList<>();
        String sql = "SELECT codigo_cuenta, nombre_cuenta FROM cuentas_contables WHERE es_cuenta_movimiento = TRUE ORDER BY codigo_cuenta";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new CuentaItem(rs.getString("codigo_cuenta"), rs.getString("nombre_cuenta")));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar cuentas: " + e.getMessage());
        }
        return lista;
    }

    public boolean guardarPartida(java.sql.Date fecha, String descripcion, List<DetallePartida> detalles) {
        Connection con = Conexion.getConnection();
        PreparedStatement psCab = null;
        PreparedStatement psDet = null;

        try {
            con.setAutoCommit(false);

            String sqlCabecera = "INSERT INTO asientos_cabecera (fecha_contable, descripcion, id_periodo, id_usuario_creador) VALUES (?, ?, 1, 1) RETURNING id_asiento";

            psCab = con.prepareStatement(sqlCabecera);
            psCab.setDate(1, fecha);
            psCab.setString(2, descripcion);

            ResultSet rs = psCab.executeQuery();
            int idAsientoGenerado = 0;

            if (rs.next()) {
                idAsientoGenerado = rs.getInt(1); 
            }

            String sqlDetalle = "INSERT INTO asientos_detalle (id_asiento, id_cuenta, debe, haber) VALUES (?, (SELECT id_cuenta FROM cuentas_contables WHERE codigo_cuenta = ?), ?, ?)";
            psDet = con.prepareStatement(sqlDetalle);

            for (DetallePartida det : detalles) {
                psDet.setInt(1, idAsientoGenerado);
                psDet.setString(2, det.getCodigoCuenta()); 
                psDet.setDouble(3, det.getDebe());
                psDet.setDouble(4, det.getHaber());
                psDet.addBatch();
            }

            psDet.executeBatch();
            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
            }
            System.err.println("Error grave al guardar partida: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public java.util.List<Object[]> obtenerReporteLibroDiario(java.sql.Date desde, java.sql.Date hasta) {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();

        String sql = "SELECT c.fecha_contable, c.id_asiento, cu.codigo_cuenta, cu.nombre_cuenta, "
                + "c.descripcion, d.debe, d.haber "
                + "FROM asientos_cabecera c "
                + "JOIN asientos_detalle d ON c.id_asiento = d.id_asiento "
                + "JOIN cuentas_contables cu ON d.id_cuenta = cu.id_cuenta "
                + "WHERE c.fecha_contable BETWEEN ? AND ? "
                + "ORDER BY c.fecha_contable ASC, c.id_asiento ASC, d.debe DESC";

        try (java.sql.Connection con = Conexion.getConnection(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, desde);
            ps.setDate(2, hasta);

            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getDate(1), // Fecha
                    rs.getInt(2), // Num Partida
                    rs.getString(3), // Código
                    rs.getString(4), // Nombre Cuenta
                    rs.getString(5), // Descripción
                    rs.getDouble(6), // Debe
                    rs.getDouble(7) // Haber
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public java.util.List<Object[]> obtenerLibroDiarioCompleto() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();

        String sql = "SELECT c.fecha_contable, c.id_asiento, cu.codigo_cuenta, cu.nombre_cuenta, "
                + "c.descripcion, d.debe, d.haber "
                + "FROM asientos_cabecera c "
                + "JOIN asientos_detalle d ON c.id_asiento = d.id_asiento "
                + "JOIN cuentas_contables cu ON d.id_cuenta = cu.id_cuenta "
                + "ORDER BY c.fecha_contable ASC, c.id_asiento ASC, d.debe DESC";

        try (java.sql.Connection con = Conexion.getConnection(); java.sql.PreparedStatement ps = con.prepareStatement(sql); java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getDate(1), rs.getInt(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

}
