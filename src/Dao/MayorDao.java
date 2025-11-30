package Dao;

import Conexion.Conexion;
import Modelo.CuentaItem;
import Modelo.CuentaMayor;
import Modelo.MayorDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MayorDao {

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

    public List<String> obtenerTodosLosCodigosDeCuenta() {
        List<String> codigos = new ArrayList<>();
        String sql = "SELECT codigo_cuenta FROM cuentas_contables WHERE es_cuenta_movimiento = TRUE ORDER BY codigo_cuenta";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                codigos.add(rs.getString("codigo_cuenta"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los códigos de cuenta: " + e.getMessage());
        }
        return codigos;
    }

public CuentaMayor obtenerInfoCuentaPorCodigo(String codigo) throws SQLException {
        CuentaMayor cuenta = null;
        
        // --- AQUÍ ESTÁ EL ARREGLO ---
        // Usamos COALESCE para que si la naturaleza es nula, devuelva 'DEUDORA'
        String sql = "SELECT COALESCE(naturaleza, 'DEUDORA') as nat_segura, nombre_cuenta " +
                     "FROM cuentas_contables WHERE codigo_cuenta = ?";

        try (Connection con = Conexion.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cuenta = new CuentaMayor();
                    // Leemos la columna segura
                    cuenta.setTipoCuenta(rs.getString("nat_segura"));
                    cuenta.setNombre(rs.getString("nombre_cuenta"));
                }
            }
        }
        return cuenta;
    }

    public List<MayorDto> obtenerCuentaMayor(String codigo, Date fechaInicio, Date fechaFin) {
        List<MayorDto> transacciones = new ArrayList<>();

        // Fechas Comodín para rango completo (desde 1970 hasta hoy)
        Date fD = (fechaInicio != null) ? fechaInicio : new Date(0); 
        Date fH = (fechaFin != null) ? fechaFin : new Date(new java.util.Date().getTime());

        String sql = "SELECT c.fecha_contable, c.id_asiento, c.descripcion, d.debe, d.haber "
                + "FROM asientos_cabecera c JOIN asientos_detalle d ON c.id_asiento = d.id_asiento "
                + "JOIN cuentas_contables cu ON d.id_cuenta = cu.id_cuenta "
                + "WHERE cu.codigo_cuenta = ? AND c.fecha_contable BETWEEN ? AND ? "
                + "ORDER BY c.fecha_contable ASC, c.id_asiento ASC, d.debe DESC";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setDate(2, fD);
            ps.setDate(3, fH);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MayorDto dto = new MayorDto();
                    dto.setFecha(rs.getDate("fecha_contable"));
                    dto.setPartida(rs.getInt("id_asiento"));
                    dto.setDescripcion(rs.getString("descripcion"));
                    dto.setCargo(rs.getDouble("debe"));
                    dto.setAbono(rs.getDouble("haber"));
                    transacciones.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el Libro Mayor: " + e.getMessage());
        }
        return transacciones;
    }
    
    
}