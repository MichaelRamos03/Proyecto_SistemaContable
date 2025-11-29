/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

/**
 *
 * @author PC
 */
import Conexion.Conexion;
import Modelo.BalanzaDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BalanzaDao {

    /**
     * Obtiene los totales acumulados del DEBE y HABER para todas las cuentas de movimiento.
     * @return Lista de BalanzaDTO con el código, nombre, totalDebe y totalHaber.
     */
    public List<BalanzaDto> obtenerTotalesBalanza() throws SQLException {
        List<BalanzaDto> lista = new ArrayList<>();
        
        // Consulta optimizada para sumar los movimientos de cada cuenta en la DB
        String sql = "SELECT " +
                     "    cc.codigo_cuenta, " +
                     "    cc.nombre_cuenta, " +
                     "    SUM(ad.debe) AS total_debe, " +
                     "    SUM(ad.haber) AS total_haber " +
                     "FROM cuentas_contables cc " +
                     "JOIN asientos_detalle ad ON cc.id_cuenta = ad.id_cuenta " +
                     "WHERE cc.es_cuenta_movimiento = TRUE " +
                     "GROUP BY cc.codigo_cuenta, cc.nombre_cuenta " +
                     "ORDER BY cc.codigo_cuenta";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BalanzaDto dto = new BalanzaDto();
                dto.setCodigoCuenta(rs.getString("codigo_cuenta"));
                dto.setNombreCuenta(rs.getString("nombre_cuenta"));
                dto.setTotalDebe(rs.getDouble("total_debe"));
                dto.setTotalHaber(rs.getDouble("total_haber"));
                lista.add(dto);
            }
        }
        return lista;
    }
}
