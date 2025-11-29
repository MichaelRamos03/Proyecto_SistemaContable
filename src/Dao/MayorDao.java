/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexion.Conexion;
import Modelo.CuentaItem;
import Modelo.CuentaMayor;
import Modelo.MayorDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PC
 */
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

    public List<MayorDto> obtenerCuentaMayor(String codigo) {
        List<MayorDto> transacciones = new ArrayList<>();

        String sql = "SELECT c.fecha_contable, c.id_asiento, cu.codigo_cuenta, cu.nombre_cuenta, c.descripcion, d.debe, d.haber "
                + "FROM asientos_cabecera c JOIN asientos_detalle d ON c.id_asiento = d.id_asiento "
                + "JOIN cuentas_contables cu ON d.id_cuenta = cu.id_cuenta "
                + "WHERE cu.codigo_cuenta = ? "
                + // Usamos el placeholder '?'
                "ORDER BY c.fecha_contable ASC, c.id_asiento ASC, d.debe DESC";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

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

    public CuentaMayor obtenerInfoCuentaPorCodigo(String codigo) throws SQLException {
        CuentaMayor cuenta = null;

        String sql = "SELECT naturaleza, nombre_cuenta FROM cuentas_contables WHERE codigo_cuenta = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cuenta = new CuentaMayor();
                    cuenta.setTipoCuenta(rs.getString("naturaleza"));
                    cuenta.setNombre(rs.getString("nombre_cuenta"));
                }
            }
        }
        return cuenta;
    }

//    public CuentaMayor obtenerInfoCuentaPorCodigo(String codigo) throws SQLException {
//        CuentaMayor cuenta = null;
//
//        String sql = "SELECT naturaleza FROM cuentas_contables WHERE codigo_cuenta = ?";
//
//        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, codigo);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    cuenta = new CuentaMayor();
//                    cuenta.setTipoCuenta(rs.getString("naturaleza"));
//                }
//            }
//        }

//        return cuenta;
//    }
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
}
