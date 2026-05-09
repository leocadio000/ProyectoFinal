// Paquete de acceso a datos
package com.sakila.data;

import com.sakila.models.Inventory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * InventoryData: DAO CONCRETO y FINAL para la tabla 'inventory'.
 * Hereda CRUD de DataContext. Es FINAL: sin subclases.
 */
public final class InventoryData extends DataContext<Inventory> {

    /** Constructor: abre conexión al padre DataContext */
    public InventoryData() {
        super(); // Delega la apertura de conexión al padre
    }

    /**
     * Mapea una fila de 'inventory' a un objeto Inventory.
     */
    @Override
    protected Inventory mapResultSet(ResultSet rs) throws SQLException {
        Inventory inv = new Inventory(); // Objeto vacío para cargar datos

        inv.setInventoryId(rs.getInt("inventory_id"));       // PK del inventario
        inv.setFilmId(rs.getInt("film_id"));                 // FK de la película
        inv.setStoreId(rs.getInt("store_id"));               // FK de la tienda
        inv.setLastUpdate(rs.getTimestamp("last_update"));   // Última actualización

        return inv; // Inventario con datos completos de la BD
    }

    @Override
    protected String getTableName() { return "inventory"; } // Tabla inventory

    @Override
    protected String getPrimaryKeyColumn() { return "inventory_id"; } // PK

    /**
     * post — Agrega un nuevo ítem al inventario.
     * Respeta FKs: film_id y store_id deben existir en sus tablas respectivas.
     */
    @Override
    public boolean post(Inventory inv) {
        // INSERT de nuevo ítem — no incluye inventory_id (autoincrement)
        String sql = "INSERT INTO inventory (film_id, store_id) VALUES (?, ?)";
        return executeUpdate(sql, inv.getFilmId(), inv.getStoreId()); // FK film + FK store
    }

    /**
     * put — Actualiza un ítem de inventario (cambio de tienda).
     */
    @Override
    public boolean put(Inventory inv) {
        // UPDATE — principalmente usado para mover ítem entre tiendas
        String sql = "UPDATE inventory SET film_id=?, store_id=? WHERE inventory_id=?";
        return executeUpdate(sql,
                inv.getFilmId(),      // Nuevo FK de película
                inv.getStoreId(),     // Nuevo FK de tienda
                inv.getInventoryId()  // WHERE inventory_id = ?
        );
    }

    /**
     * getInventarioPorTienda — Obtiene todo el inventario de una tienda.
     *
     * @param storeId  ID de la tienda
     * @return         Lista de ítems de inventario en esa tienda
     */
    public List<Inventory> getInventarioPorTienda(int storeId) {
        String sql = "SELECT * FROM inventory WHERE store_id = ?"; // Filtra por tienda
        ResultSet rs = executeQuery(sql, storeId);
        List<Inventory> lista = new ArrayList<>();
        try {
            while (rs != null && rs.next()) lista.add(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("[InventoryData] getInventarioPorTienda: " + e.getMessage());
        }
        return lista;
    }

    /**
     * getInventarioPorFilm — Obtiene todas las copias disponibles de una película.
     *
     * @param filmId  ID de la película
     * @return        Lista de ítems de inventario de esa película
     */
    public List<Inventory> getInventarioPorFilm(int filmId) {
        String sql = "SELECT * FROM inventory WHERE film_id = ?"; // Filtra por película
        ResultSet rs = executeQuery(sql, filmId);
        List<Inventory> lista = new ArrayList<>();
        try {
            while (rs != null && rs.next()) lista.add(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("[InventoryData] getInventarioPorFilm: " + e.getMessage());
        }
        return lista;
    }
}
