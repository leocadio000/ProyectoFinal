// Paquete de acceso a datos
package com.sakila.data;

import com.sakila.models.Payment;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * PaymentData: DAO CONCRETO y FINAL para la tabla 'payment'.
 * Hereda CRUD de DataContext. Es FINAL: sin subclases.
 */
public final class PaymentData extends DataContext<Payment> {

    /** Constructor: abre conexión al padre */
    public PaymentData() {
        super(); // DataContext abre la conexión MySQL
    }

    /**
     * Mapea una fila de 'payment' a un objeto Payment.
     */
    @Override
    protected Payment mapResultSet(ResultSet rs) throws SQLException {
        Payment p = new Payment(); // Objeto vacío para recibir datos de la fila

        p.setPaymentId(rs.getInt("payment_id"));           // PK del pago
        p.setCustomerId(rs.getInt("customer_id"));         // FK del cliente
        p.setStaffId(rs.getInt("staff_id"));               // FK del empleado
        p.setRentalId(rs.getInt("rental_id"));             // FK de la renta
        p.setAmount(rs.getBigDecimal("amount"));           // Monto del pago
        p.setPaymentDate(rs.getTimestamp("payment_date")); // Fecha del pago
        p.setLastUpdate(rs.getTimestamp("last_update"));   // Última actualización

        return p; // Pago con todos sus datos
    }

    @Override
    protected String getTableName() { return "payment"; } // Tabla payment

    @Override
    protected String getPrimaryKeyColumn() { return "payment_id"; } // PK

    /**
     * post — Registra un nuevo pago en la BD.
     * Respeta FKs: customer_id, staff_id, rental_id.
     */
    @Override
    public boolean post(Payment p) {
        // INSERT de nuevo pago — no incluye payment_id (autoincrement)
        String sql = "INSERT INTO payment (customer_id, staff_id, rental_id, amount, payment_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                p.getCustomerId(),  // FK del cliente que paga
                p.getStaffId(),     // FK del empleado que recibe
                p.getRentalId(),    // FK de la renta asociada
                p.getAmount(),      // Monto a cobrar
                p.getPaymentDate()  // Fecha del pago
        );
    }

    /**
     * put — Actualiza un pago existente (ej: corregir monto).
     */
    @Override
    public boolean put(Payment p) {
        String sql = "UPDATE payment SET customer_id=?, staff_id=?, rental_id=?, " +
                     "amount=?, payment_date=? WHERE payment_id=?";
        return executeUpdate(sql,
                p.getCustomerId(),  // Nuevo FK cliente
                p.getStaffId(),     // Nuevo FK empleado
                p.getRentalId(),    // Nuevo FK renta
                p.getAmount(),      // Nuevo monto
                p.getPaymentDate(), // Nueva fecha
                p.getPaymentId()    // WHERE payment_id = ?
        );
    }

    /**
     * getPagosPorCliente — Obtiene todos los pagos de un cliente.
     * Útil para el reporte de cobranzas por cliente.
     *
     * @param customerId  ID del cliente
     * @return            Lista de pagos del cliente
     */
    public List<Payment> getPagosPorCliente(int customerId) {
        String sql = "SELECT * FROM payment WHERE customer_id = ? ORDER BY payment_date DESC";
        ResultSet rs = executeQuery(sql, customerId); // Filtra por FK del cliente
        List<Payment> lista = new ArrayList<>();
        try {
            while (rs != null && rs.next()) lista.add(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("[PaymentData] getPagosPorCliente: " + e.getMessage());
        }
        return lista;
    }

    /**
     * getEstadisticasPagos — Estadísticas de pagos usando HashMap.
     * Calcula totales y promedios de facturación.
     *
     * @return  HashMap con métricas: "total_pagos", "monto_total", "promedio", "max", "min"
     */
    public HashMap<String, Object> getEstadisticasPagos() {
        HashMap<String, Object> stats = new HashMap<>(); // Mapa para almacenar métricas

        // Consulta que calcula todas las métricas en una sola operación en la BD
        String sql = "SELECT COUNT(*) as total_pagos, SUM(amount) as monto_total, " +
                     "AVG(amount) as promedio, MAX(amount) as max_pago, MIN(amount) as min_pago " +
                     "FROM payment";

        ResultSet rs = executeQuery(sql); // Ejecuta la consulta de estadísticas

        try {
            if (rs != null && rs.next()) {
                stats.put("total_pagos",  rs.getInt("total_pagos"));          // Número de transacciones
                stats.put("monto_total",  rs.getBigDecimal("monto_total"));   // Suma total de ingresos
                stats.put("promedio",     rs.getBigDecimal("promedio"));      // Pago promedio
                stats.put("max_pago",     rs.getBigDecimal("max_pago"));      // Pago más alto
                stats.put("min_pago",     rs.getBigDecimal("min_pago"));      // Pago más bajo
            }
        } catch (SQLException e) {
            System.err.println("[PaymentData] getEstadisticasPagos: " + e.getMessage());
        }
        return stats; // Retorna el mapa con todas las métricas financieras
    }

    /**
     * getPagosPorTienda — Total recaudado por tienda.
     * Útil para el reporte de pagos/cobranzas por tienda.
     *
     * @return  HashMap con storeId como clave y total recaudado como valor
     */
    public HashMap<Integer, BigDecimal> getPagosPorTienda() {
        HashMap<Integer, BigDecimal> porTienda = new HashMap<>(); // storeId → monto total

        // JOIN entre payment, customer para obtener total por tienda del cliente
        String sql = "SELECT c.store_id, SUM(p.amount) as total " +
                     "FROM payment p JOIN customer c ON p.customer_id = c.customer_id " +
                     "GROUP BY c.store_id"; // Agrupa por tienda para sumar ingresos

        ResultSet rs = executeQuery(sql);

        try {
            while (rs != null && rs.next()) {
                // Clave: ID de la tienda | Valor: total recaudado en esa tienda
                porTienda.put(rs.getInt("store_id"), rs.getBigDecimal("total"));
            }
        } catch (SQLException e) {
            System.err.println("[PaymentData] getPagosPorTienda: " + e.getMessage());
        }
        return porTienda; // Mapa con ingresos desglosados por tienda
    }
}
