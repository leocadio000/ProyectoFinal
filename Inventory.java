// Paquete de modelos del proyecto
package com.sakila.models;

// Timestamp para last_update
import java.sql.Timestamp;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Inventory: POJO que representa la tabla 'inventory' de la BD sakila.
 * Cada registro es una copia física de una película en una tienda específica.
 * FK: film_id → film | store_id → store
 */
public class Inventory {

    // inventory_id: PK autoincrement de la tabla inventory
    private int inventoryId;

    // film_id: FK que referencia la película de este ítem de inventario
    private int filmId;

    // store_id: FK que indica en qué tienda física está este ítem
    private int storeId;

    // last_update: timestamp de la última modificación
    private Timestamp lastUpdate;

    // -------------------------------------------------------------------------
    // CONSTRUCTORES
    // -------------------------------------------------------------------------

    /** Constructor vacío para el ORM */
    public Inventory() {}

    /**
     * Constructor para agregar un ítem al inventario.
     * No incluye inventoryId (autoincrement).
     *
     * @param filmId   FK de la película
     * @param storeId  FK de la tienda donde se agrega
     */
    public Inventory(int filmId, int storeId) {
        this.filmId  = filmId;  // Qué película es (FK)
        this.storeId = storeId; // En qué tienda está (FK)
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------

    /** @return  ID del inventario (PK) */
    public int getInventoryId() { return inventoryId; }
    /** @param inventoryId  ID del inventario */
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }

    /** @return  ID de la película (FK) */
    public int getFilmId() { return filmId; }
    /** @param filmId  FK de la película */
    public void setFilmId(int filmId) { this.filmId = filmId; }

    /** @return  ID de la tienda (FK) */
    public int getStoreId() { return storeId; }
    /** @param storeId  FK de la tienda */
    public void setStoreId(int storeId) { this.storeId = storeId; }

    /** @return  Timestamp de última actualización */
    public Timestamp getLastUpdate() { return lastUpdate; }
    /** @param lastUpdate  Nuevo timestamp */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Representación legible del inventario.
     * Muestra el ID, película y tienda.
     */
    @Override
    public String toString() {
        return String.format("[%d] FilmID: %d | StoreID: %d", inventoryId, filmId, storeId);
    }
}
