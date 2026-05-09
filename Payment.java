// Paquete de modelos del proyecto
package com.sakila.models;

// BigDecimal para el campo amount (valor monetario con precisión exacta)
import java.math.BigDecimal;
// Timestamp para payment_date y last_update
import java.sql.Timestamp;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Payment: POJO que representa la tabla 'payment' de la BD sakila.
 * Registra cada pago realizado por un cliente.
 * FK: customer_id → customer | staff_id → staff | rental_id → rental
 */
public class Payment {

    // payment_id: PK autoincrement de la tabla payment
    private int paymentId;

    // customer_id: FK que referencia al cliente que realizó el pago
    private int customerId;

    // staff_id: FK que referencia al empleado que recibió el pago
    private int staffId;

    // rental_id: FK que referencia la renta asociada al pago (puede ser null)
    private int rentalId;

    // amount: monto del pago (DECIMAL 5,2 en BD)
    private BigDecimal amount;

    // payment_date: fecha y hora en que se procesó el pago
    private Timestamp paymentDate;

    // last_update: timestamp de la última modificación del registro
    private Timestamp lastUpdate;

    // -------------------------------------------------------------------------
    // CONSTRUCTORES
    // -------------------------------------------------------------------------

    /** Constructor vacío para el ORM */
    public Payment() {}

    /**
     * Constructor para registrar un nuevo pago.
     * No incluye paymentId (autoincrement).
     *
     * @param customerId   FK del cliente que paga
     * @param staffId      FK del empleado que recibe el pago
     * @param rentalId     FK de la renta asociada
     * @param amount       Monto a pagar
     * @param paymentDate  Fecha y hora del pago
     */
    public Payment(int customerId, int staffId, int rentalId,
                   BigDecimal amount, Timestamp paymentDate) {
        this.customerId  = customerId;  // Cliente que realiza el pago (FK)
        this.staffId     = staffId;     // Empleado que procesa (FK)
        this.rentalId    = rentalId;    // Renta a la que aplica (FK)
        this.amount      = amount;      // Monto del pago
        this.paymentDate = paymentDate; // Fecha del pago
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------

    /** @return  ID del pago (PK) */
    public int getPaymentId() { return paymentId; }
    /** @param paymentId  ID del pago */
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    /** @return  ID del cliente (FK) */
    public int getCustomerId() { return customerId; }
    /** @param customerId  FK del cliente */
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    /** @return  ID del empleado (FK) */
    public int getStaffId() { return staffId; }
    /** @param staffId  FK del empleado */
    public void setStaffId(int staffId) { this.staffId = staffId; }

    /** @return  ID de la renta (FK) */
    public int getRentalId() { return rentalId; }
    /** @param rentalId  FK de la renta */
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    /** @return  Monto del pago */
    public BigDecimal getAmount() { return amount; }
    /** @param amount  Nuevo monto */
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    /** @return  Fecha y hora del pago */
    public Timestamp getPaymentDate() { return paymentDate; }
    /** @param paymentDate  Nueva fecha de pago */
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }

    /** @return  Timestamp de última actualización */
    public Timestamp getLastUpdate() { return lastUpdate; }
    /** @param lastUpdate  Nuevo timestamp */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Representación legible del pago.
     * Formato: [ID] Cliente | Renta | Monto | Fecha
     */
    @Override
    public String toString() {
        return String.format("[%d] Cust:%d | Rental:%d | Amount:$%.2f | Date:%s",
                paymentId, customerId, rentalId, amount, paymentDate);
    }
}
