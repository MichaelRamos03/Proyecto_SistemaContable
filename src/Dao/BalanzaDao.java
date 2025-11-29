package Dao;

import Conexion.Conexion;
import Modelo.BalanzaDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; // Usamos java.sql.Date
import java.util.ArrayList;
import java.util.List;

public class BalanzaDao {

    public List<BalanzaDto> obtenerTotalesBalanza(Date fechaInicio, Date fechaFin) throws SQLException {
        List<BalanzaDto> lista = new ArrayList<>();
        
        // Fechas Comodín para rango completo
        Date fD = (fechaInicio != null) ? fechaInicio : new Date(0); 
        Date fH = (fechaFin != null) ? fechaFin : new Date(new java.util.Date().getTime());
        
        String sql = "SELECT " +
                     "    cc.codigo_cuenta, " +
                     "    cc.nombre_cuenta, " +
                     "    SUM(ad.debe) AS total_debe, " +
                     "    SUM(ad.haber) AS total_haber " +
                     "FROM cuentas_contables cc " +
                     "JOIN asientos_detalle ad ON cc.id_cuenta = ad.id_cuenta " +
                     "JOIN asientos_cabecera ac ON ad.id_asiento = ac.id_asiento " 
                     + "WHERE cc.es_cuenta_movimiento = TRUE AND ac.fecha_contable BETWEEN ? AND ? " 
                     + "GROUP BY cc.codigo_cuenta, cc.nombre_cuenta " +
                     "ORDER BY cc.codigo_cuenta";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setDate(1, fD);
            ps.setDate(2, fH);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BalanzaDto dto = new BalanzaDto();
                    dto.setCodigoCuenta(rs.getString("codigo_cuenta"));
                    dto.setNombreCuenta(rs.getString("nombre_cuenta"));
                    dto.setTotalDebe(rs.getDouble("total_debe"));
                    dto.setTotalHaber(rs.getDouble("total_haber"));
                    lista.add(dto);
                }
            }
        }
        return lista;
    }
}