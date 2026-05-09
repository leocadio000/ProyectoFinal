// Paquete de controladores MVC
package com.sakila.controllers;

import com.sakila.data.RentalData;
import com.sakila.models.Rental;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * RentalController: Controlador MVC para gestionar rentas de películas.
 * Lógica de negocio de rentas: validaciones de FK y fechas.
 */
public class RentalController {

    // DAO de rentas — maneja el acceso a la tabla rental
    private RentalData rentalData;

    // Lista en memoria de rentas activas — permite operar sin consultar BD cada vez
    private List<Rental> listaRentas;

    /** Constructor: inicializa DAO y carga rentas desde la BD */
    public RentalController() {
        rentalData  = new RentalData();   // Crea el DAO con conexión BD
        listaRentas = new ArrayList<>();  // Inicializa la lista vacía
        // No carga todas las rentas al inicio porque la tabla es muy grande
    }

    /**
     * registrarRenta — Registra una nueva renta en el sistema.
     * Valida que los IDs de inventario y cliente sean positivos.
     *
     * @param inventoryId  ID del ítem de inventario a rentar (FK)
     * @param customerId   ID del cliente que renta (FK)
     * @param staffId      ID del empleado que procesa (FK)
     * @return             true si se registró exitosamente
     */
    public boolean registrarRenta(int inventoryId, int customerId, int staffId) {
        // Validación: todos los IDs deben ser positivos para ser FK válidas
        if (inventoryId <= 0 || customerId <= 0 || staffId <= 0) {
            System.err.println("[RentalController] IDs inválidos para registrar renta.");
            return false; // Rechaza si algún FK no es válido
        }

        // Crea la nueva renta con la fecha y hora actual del sistema
        Rental nueva = new Rental(
                new Timestamp(System.currentTimeMillis()), // rental_date = ahora mismo
                inventoryId,  // FK del ítem rentado
                customerId,   // FK del cliente
                null,         // return_date = null (aún no ha sido devuelta)
                staffId       // FK del empleado
        );

        boolean resultado = rentalData.post(nueva); // Inserta la renta en la BD

        if (resultado) {
            listaRentas.add(nueva); // Agrega también a la lista en memoria
            System.out.println("[RentalController] Renta registrada. Cliente: " + customerId);
        }
        return resultado;
    }

    /**
     * registrarDevolucion — Marca una renta como devuelta.
     * Registra la fecha/hora actual como fecha de devolución.
     *
     * @param rentalId  ID de la renta a cerrar
     * @return          true si se registró la devolución
     */
    public boolean registrarDevolucion(int rentalId) {
        if (rentalId <= 0) { // Validación básica del ID
            System.err.println("[RentalController] ID de renta no válido.");
            return false;
        }

        // Usa la fecha y hora actual como fecha de devolución
        Timestamp ahora = new Timestamp(System.currentTimeMillis());

        // Llama al método específico del DAO para actualizar solo return_date
        return rentalData.registrarDevolucion(rentalId, ahora);
    }

    /**
     * getRentasPorCliente — Obtiene el historial de rentas de un cliente.
     *
     * @param customerId  ID del cliente
     * @return            Lista de sus rentas ordenadas por fecha
     */
    public List<Rental> getRentasPorCliente(int customerId) {
        return rentalData.getRentasPorCliente(customerId); // Delega al DAO
    }

    /**
     * getRentasPendientes — Obtiene todas las películas no devueltas.
     * Útil para el reporte de aging de cuentas por cobrar.
     *
     * @return  Lista de rentas sin fecha de devolución
     */
    public List<Rental> getRentasPendientes() {
        return rentalData.getRentasPendientes(); // Delega al DAO
    }

    /** Cierra la conexión del DAO */
    public void cerrar() {
        rentalData.closeConnection(); // Libera el recurso de conexión
    }
}
