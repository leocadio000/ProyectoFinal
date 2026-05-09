// Paquete de acceso a datos
package com.sakila.data;

// Importamos el modelo Rental
import com.sakila.models.Rental;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * RentalData: DAO CONCRETO y FINAL para la tabla 'rental'.
 * Hereda CRUD de DataContext. Respeta FKs: inventory_id, customer_id, staff_id.
 * Es FINAL: no puede tener subclases.
 */
public final class RentalData extends DataContext<Rental> {

    /** Constructor: conecta con la BD sakila */
    public RentalData() {
        super(); // Abre la conexión MySQL a través del padre DataContext
    }

    /**
     * Mapea una fila de 'rental' a un objeto Rental.
     * Lee todas las columnas de la tabla incluyendo las FKs.
     */
    @Override
    protected Rental mapResultSet(ResultSet rs) throws SQLException {
        Rental r = new Rental(); // Objeto vacío para cargar con datos de la BD

        r.setRentalId(rs.getInt("rental_id"));          // PK de la renta
        r.setRentalDate(rs.getTimestamp("rental_date")); // Fecha en que se hizo la renta
        r.setInventoryId(rs.getInt("inventory_id"));     // FK del ítem rentado
        r.setCustomerId(rs.getInt("customer_id"));       // FK del cliente que renta
        r.setReturnDate(rs.getTimestamp("return_date")); // Fecha de devolución (puede ser null)
        r.setStaffId(rs.getInt("staff_id"));             // FK del empleado que procesa
        r.setLastUpdate(rs.getTimestamp("last_update")); // Última modificación

        return r; // Retorna la renta completamente cargada
    }

    @Override
    protected String getTableName() { return "rental"; } // Tabla de rentas

    @Override
    protected String getPrimaryKeyColumn() { return "rental_id"; } // PK de la tabla

    /**
     * post — Registra una nueva renta en la BD.
     * No incluye rental_id (autoincrement).
     * Respeta las FK: inventory_id, customer_id, staff_id deben existir en sus tablas.
     */
    @Override
    public boolean post(Rental r) {
        // INSERT de nueva renta — return_date puede ser null si no se especifica
        String sql = "INSERT INTO rental (rental_date, inventory_id, customer_id, return_date, staff_id) " +
                     "VALUES (?, ?, ?, ?, ?)";

        return executeUpdate(sql,
                r.getRentalDate(),   // Fecha de la nueva renta
                r.getInventoryId(),  // FK del inventario (ítem a rentar)
                r.getCustomerId(),   // FK del cliente que realiza la renta
                r.getReturnDate(),   // Fecha de devolución (null si aún no se devuelve)
                r.getStaffId()       // FK del empleado que procesa
        );
    }

    /**
     * put — Actualiza una renta existente.
     * Principalmente usado para registrar la fecha de devolución.
     */
    @Override
    public boolean put(Rental r) {
        // UPDATE — permite actualizar todos los campos de la renta
        String sql = "UPDATE rental SET rental_date=?, inventory_id=?, customer_id=?, " +
                     "return_date=?, staff_id=? WHERE rental_id=?";

        return executeUpdate(sql,
                r.getRentalDate(),   // Nueva fecha de renta
                r.getInventoryId(),  // Nuevo FK inventario
                r.getCustomerId(),   // Nuevo FK cliente
                r.getReturnDate(),   // Nueva fecha de devolución (registrar devolución)
                r.getStaffId(),      // Nuevo FK empleado
                r.getRentalId()      // WHERE rental_id = ? (identifica la renta)
        );
    }

    /**
     * registrarDevolucion — Método específico para marcar una película como devuelta.
     * Actualiza solo return_date sin modificar otros campos.
     *
     * @param rentalId    ID de la renta a actualizar
     * @param returnDate  Fecha y hora de la devolución
     * @return            true si se registró exitosamente
     */
    public boolean registrarDevolucion(int rentalId, java.sql.Timestamp returnDate) {
        // UPDATE solo de return_date para registrar devolución
        String sql = "UPDATE rental SET return_date=? WHERE rental_id=?";
        return executeUpdate(sql, returnDate, rentalId); // Actualiza solo la fecha de retorno
    }

    /**
     * getRentasPorCliente — Obtiene todas las rentas de un cliente específico.
     * Útil para historial de rentas del cliente.
     *
     * @param customerId  ID del cliente
     * @return            Lista de rentas del cliente
     */
    public List<Rental> getRentasPorCliente(int customerId) {
        // SELECT filtrado por customer_id ordenado por fecha más reciente
        String sql = "SELECT * FROM rental WHERE customer_id = ? ORDER BY rental_date DESC";
        ResultSet rs = executeQuery(sql, customerId); // Ejecuta con el FK del cliente
        List<Rental> lista = new ArrayList<>();
        try {
            while (rs != null && rs.next()) lista.add(mapResultSet(rs)); // Agrega cada renta
        } catch (SQLException e) {
            System.err.println("[RentalData] getRentasPorCliente: " + e.getMessage());
        }
        return lista;
    }

    /**
     * getRentasPendientes — Obtiene todas las rentas sin fecha de devolución.
     * Útil para el reporte de aging de cuentas por cobrar.
     *
     * @return  Lista de rentas donde return_date es NULL (no devueltas)
     */
    public List<Rental> getRentasPendientes() {
        // Filtra rentas donde return_date es NULL (películas no devueltas)
        String sql = "SELECT * FROM rental WHERE return_date IS NULL ORDER BY rental_date";
        ResultSet rs = executeQuery(sql); // Sin parámetros adicionales
        List<Rental> lista = new ArrayList<>();
        try {
            while (rs != null && rs.next()) lista.add(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("[RentalData] getRentasPendientes: " + e.getMessage());
        }
        return lista; // Lista de películas aún no devueltas
    }
}
