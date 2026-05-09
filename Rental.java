// Paquete de modelos del proyecto
package com.sakila.models;

// Timestamp para los campos de fecha/hora de MySQL
import java.sql.Timestamp;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Rental: POJO que representa la tabla 'rental' de la BD sakila.
 * Registra cada transacción de renta de película.
 * FK: inventory_id → inventory | customer_id → customer | staff_id → staff
 */
public class Rental {

    // rental_id: PK autoincrement de la tabla rental
    private int rentalId;

    // rental_date: fecha y hora en que se realizó la renta
    private Timestamp rentalDate;

    // inventory_id: FK que referencia al inventario específico rentado
    private int inventoryId;

    // customer_id: FK que referencia al cliente que realizó la renta
    private int customerId;

    // return_date: fecha y hora en que se devolvió la película (null si no se ha devuelto)
    private Timestamp returnDate;

    // staff_id: FK que referencia al empleado que procesó la renta
    private int staffId;

    // last_update: timestamp de la última modificación del registro
    private Timestamp lastUpdate;

    // -------------------------------------------------------------------------
    // CONSTRUCTORES
    // -------------------------------------------------------------------------

    /** Constructor vacío para el ORM */
    public Rental() {}

    /**
     * Constructor para crear una nueva renta.
     * No incluye rentalId (autoincrement).
     * returnDate puede ser null al momento de la renta.
     *
     * @param rentalDate   Fecha/hora de la renta
     * @param inventoryId  FK del ítem de inventario rentado
     * @param customerId   FK del cliente que renta
     * @param returnDate   Fecha/hora de devolución (null si aún no devuelve)
     * @param staffId      FK del empleado que atiende
     */
    public Rental(Timestamp rentalDate, int inventoryId, int customerId,
                  Timestamp returnDate, int staffId) {
        this.rentalDate  = rentalDate;  // Fecha en que se procesa la renta
        this.inventoryId = inventoryId; // Item específico del inventario (FK)
        this.customerId  = customerId;  // Cliente que renta (FK)
        this.returnDate  = returnDate;  // Fecha de devolución (puede ser null)
        this.staffId     = staffId;     // Empleado que atiende (FK)
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------

    /** @return  ID de la renta (PK) */
    public int getRentalId() { return rentalId; }
    /** @param rentalId  ID de la renta */
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    /** @return  Fecha y hora de la renta */
    public Timestamp getRentalDate() { return rentalDate; }
    /** @param rentalDate  Nueva fecha de renta */
    public void setRentalDate(Timestamp rentalDate) { this.rentalDate = rentalDate; }

    /** @return  ID del inventario rentado (FK) */
    public int getInventoryId() { return inventoryId; }
    /** @param inventoryId  FK del inventario */
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }

    /** @return  ID del cliente (FK) */
    public int getCustomerId() { return customerId; }
    /** @param customerId  FK del cliente */
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    /** @return  Fecha de devolución (null si no ha sido devuelta) */
    public Timestamp getReturnDate() { return returnDate; }
    /** @param returnDate  Fecha de devolución */
    public void setReturnDate(Timestamp returnDate) { this.returnDate = returnDate; }

    /** @return  ID del empleado que procesó la renta (FK) */
    public int getStaffId() { return staffId; }
    /** @param staffId  FK del empleado */
    public void setStaffId(int staffId) { this.staffId = staffId; }

    /** @return  Timestamp de última actualización */
    public Timestamp getLastUpdate() { return lastUpdate; }
    /** @param lastUpdate  Nuevo timestamp */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Representación legible de la renta.
     * Muestra ID, inventario, cliente, fechas y si ya fue devuelta.
     */
    @Override
    public String toString() {
        // Verifica si returnDate es null para mostrar "Pendiente" o la fecha
        String devolucion = (returnDate != null) ? returnDate.toString() : "PENDIENTE";
        return String.format("[%d] Inv:%d | Cust:%d | Rented:%s | Return:%s",
                rentalId, inventoryId, customerId, rentalDate, devolucion);
    }
}
