// Paquete de controladores MVC
package com.sakila.controllers;

import com.sakila.data.InventoryData;
import com.sakila.models.Inventory;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * InventoryController: Controlador MVC para gestión de inventario de películas.
 * Controla las copias físicas de películas disponibles en cada tienda.
 */
public class InventoryController {

    // DAO de inventario — accede a la tabla inventory en la BD
    private InventoryData inventoryData;

    /** Constructor: inicializa el DAO */
    public InventoryController() {
        inventoryData = new InventoryData(); // Crea DAO con conexión BD
    }

    /**
     * agregarCopia — Agrega una nueva copia de película al inventario de una tienda.
     * Valida que los IDs de film y store sean positivos (FKs válidas).
     *
     * @param filmId   ID de la película a agregar (FK)
     * @param storeId  ID de la tienda donde se agrega (FK)
     * @return         true si fue agregado correctamente
     */
    public boolean agregarCopia(int filmId, int storeId) {
        // Validación: ambos IDs deben ser positivos para ser FKs válidas
        if (filmId <= 0 || storeId <= 0) {
            System.err.println("[InventoryController] IDs inválidos.");
            return false; // Rechaza si los FK no son válidos
        }

        // Crea el objeto Inventory con los datos recibidos
        Inventory item = new Inventory(filmId, storeId);

        // Delega la inserción al DAO y retorna el resultado
        return inventoryData.post(item);
    }

    /**
     * eliminarCopia — Elimina un ítem del inventario por su ID.
     * CUIDADO: solo si no tiene rentas activas (la BD lanzará error de FK si las tiene).
     *
     * @param inventoryId  ID del ítem a eliminar
     * @return             true si fue eliminado
     */
    public boolean eliminarCopia(int inventoryId) {
        return inventoryData.delete(inventoryId); // Delega al DAO el DELETE
    }

    /**
     * getInventarioPorTienda — Obtiene el inventario completo de una tienda.
     * Útil para la gestión de inventario por tienda (requerimiento del proyecto).
     *
     * @param storeId  ID de la tienda
     * @return         Lista de ítems de inventario en esa tienda
     */
    public List<Inventory> getInventarioPorTienda(int storeId) {
        return inventoryData.getInventarioPorTienda(storeId); // Delega al DAO
    }

    /**
     * getCopiasDisponibles — Retorna todas las copias de una película.
     * Útil para saber cuántas copias hay disponibles para rentar.
     *
     * @param filmId  ID de la película
     * @return        Lista de ítems de inventario de esa película
     */
    public List<Inventory> getCopiasDisponibles(int filmId) {
        return inventoryData.getInventarioPorFilm(filmId); // Delega al DAO
    }

    /** Cierra la conexión del DAO */
    public void cerrar() { inventoryData.closeConnection(); }
}
