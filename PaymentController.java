// Paquete de controladores MVC del proyecto
package com.sakila.controllers;

// Importamos el DAO y el modelo de pagos
import com.sakila.data.PaymentData;
import com.sakila.models.Payment;

// Colecciones para manejo de listas y estadísticas
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * PaymentController: Controlador MVC para gestión de pagos y cobranzas.
 * Valida los datos antes de registrar pagos y genera estadísticas financieras.
 * Cumple el requerimiento de reportes de pagos/cobranzas por tienda, ciudad,
 * país y clientes.
 */
public class PaymentController {

    // DAO de pagos — accede a la tabla payment en la BD
    private PaymentData paymentData;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    /**
     * Constructor: inicializa el DAO de pagos.
     */
    public PaymentController() {
        paymentData = new PaymentData(); // Crea el DAO con conexión a la BD
    }

    // -------------------------------------------------------------------------
    // OPERACIONES CRUD
    // -------------------------------------------------------------------------

    /**
     * registrarPago — Registra un nuevo pago en el sistema.
     * Valida que el monto sea positivo y los IDs sean válidos.
     *
     * @param customerId  ID del cliente que paga (FK → customer)
     * @param staffId     ID del empleado que recibe (FK → staff)
     * @param rentalId    ID de la renta asociada (FK → rental)
     * @param monto       Monto del pago (debe ser > 0)
     * @return            true si el pago fue registrado correctamente
     */
    public boolean registrarPago(int customerId, int staffId, int rentalId, BigDecimal monto) {
        // Validación: todos los IDs deben ser positivos para ser FKs válidas
        if (customerId <= 0 || staffId <= 0 || rentalId <= 0) {
            System.err.println("[PaymentController] IDs inválidos para el pago.");
            return false; // Rechaza si algún FK no es válido
        }

        // Validación: el monto debe ser positivo (no se aceptan pagos de $0 o negativos)
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("[PaymentController] El monto debe ser mayor a $0.00");
            return false; // Rechaza si el monto no es válido
        }

        // Crea el objeto Payment con la fecha actual del sistema
        Payment pago = new Payment(
                customerId,                                  // FK del cliente
                staffId,                                     // FK del empleado
                rentalId,                                    // FK de la renta
                monto,                                       // Monto a cobrar
                new Timestamp(System.currentTimeMillis())    // Fecha/hora actual
        );

        // Inserta el pago en la BD a través del DAO
        boolean resultado = paymentData.post(pago);

        if (resultado) {
            // Confirma en consola el registro exitoso
            System.out.printf("[PaymentController] Pago registrado: $%.2f | Cliente: %d%n",
                    monto, customerId);
        }
        return resultado; // Retorna true si se insertó correctamente
    }

    /**
     * buscarPago — Busca un pago específico por su ID.
     *
     * @param paymentId  ID del pago a buscar (PK)
     * @return           Objeto Payment encontrado, o null si no existe
     */
    public Payment buscarPago(int paymentId) {
        if (paymentId <= 0) return null; // Valida el ID antes de consultar
        return paymentData.get(paymentId); // Delega la búsqueda al DAO
    }

    /**
     * getPagosPorCliente — Obtiene el historial de pagos de un cliente.
     * Ordenados por fecha descendente (más reciente primero).
     *
     * @param customerId  ID del cliente
     * @return            Lista de pagos del cliente
     */
    public List<Payment> getPagosPorCliente(int customerId) {
        if (customerId <= 0) return null; // Valida el FK
        return paymentData.getPagosPorCliente(customerId); // Delega al DAO
    }

    /**
     * anularPago — Elimina un pago por su ID (anulación).
     * En un sistema real debería registrarse la anulación, no eliminar.
     *
     * @param paymentId  ID del pago a anular
     * @return           true si fue anulado correctamente
     */
    public boolean anularPago(int paymentId) {
        if (paymentId <= 0) { // Valida el ID
            System.err.println("[PaymentController] ID de pago inválido para anular.");
            return false;
        }
        // Elimina el registro de pago de la BD
        boolean resultado = paymentData.delete(paymentId);

        if (resultado) {
            System.out.println("[PaymentController] Pago anulado. ID: " + paymentId);
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // ESTADÍSTICAS Y REPORTES
    // -------------------------------------------------------------------------

    /**
     * getEstadisticasGenerales — Obtiene métricas generales de todos los pagos.
     * Calcula total de transacciones, monto total, promedio, máximo y mínimo.
     *
     * @return  HashMap con las métricas: "total_pagos", "monto_total",
     *          "promedio", "max_pago", "min_pago"
     */
    public HashMap<String, Object> getEstadisticasGenerales() {
        return paymentData.getEstadisticasPagos(); // Delega el cálculo al DAO
    }

    /**
     * getCobranzasPorTienda — Obtiene el total recaudado desglosado por tienda.
     * Usa HashMap para asociar cada tienda con su monto total recaudado.
     *
     * @return  HashMap con storeId como clave y monto total como valor
     */
    public HashMap<Integer, BigDecimal> getCobranzasPorTienda() {
        return paymentData.getPagosPorTienda(); // Delega el agrupamiento al DAO
    }

    /**
     * getTotalPagado — Calcula cuánto ha pagado un cliente en total.
     * Suma todos los montos de sus pagos usando la lista retornada.
     *
     * @param customerId  ID del cliente
     * @return            Total acumulado de todos sus pagos
     */
    public BigDecimal getTotalPagado(int customerId) {
        // Obtiene todos los pagos del cliente
        List<Payment> pagos = paymentData.getPagosPorCliente(customerId);

        // Acumula la suma de todos los montos usando BigDecimal.ZERO como inicio
        BigDecimal total = BigDecimal.ZERO; // Inicia el acumulador en cero

        for (Payment p : pagos) {
            // Suma el monto de cada pago al acumulador
            total = total.add(p.getAmount()); // BigDecimal.add() para precisión exacta
        }
        return total; // Retorna el total acumulado de pagos del cliente
    }

    /**
     * cerrar — Cierra la conexión del DAO de pagos.
     * Debe llamarse al terminar de usar el controlador.
     */
    public void cerrar() {
        paymentData.closeConnection(); // Libera el recurso de conexión MySQL
    }
}
