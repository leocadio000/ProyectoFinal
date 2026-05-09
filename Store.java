// Paquete de modelos del proyecto sakila
package com.sakila.models;

// Timestamp para el campo last_update de MySQL
import java.sql.Timestamp;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Store: POJO que representa la tabla 'store' de la base de datos sakila.
 * Almacena la información de cada tienda física de renta de películas.
 * FK: manager_staff_id → staff | address_id → address
 */
public class Store {

    // store_id: llave primaria autoincrement de la tabla store
    private int storeId;

    // manager_staff_id: FK que referencia al empleado gerente de la tienda
    private int managerStaffId;

    // address_id: FK que referencia la dirección física de la tienda
    private int addressId;

    // last_update: timestamp de la última modificación del registro
    private Timestamp lastUpdate;

    // -------------------------------------------------------------------------
    // CONSTRUCTORES
    // -------------------------------------------------------------------------

    /** Constructor vacío para el ORM */
    public Store() {}

    /**
     * Constructor para crear una nueva tienda.
     * No incluye storeId porque es autoincrement.
     *
     * @param managerStaffId  FK del empleado gerente
     * @param addressId       FK de la dirección de la tienda
     */
    public Store(int managerStaffId, int addressId) {
        this.managerStaffId = managerStaffId; // FK del gerente de la tienda
        this.addressId      = addressId;      // FK de la dirección física
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------

    /** @return  ID de la tienda (PK) */
    public int getStoreId() { return storeId; }
    /** @param storeId  ID de la tienda */
    public void setStoreId(int storeId) { this.storeId = storeId; }

    /** @return  ID del gerente (FK → staff) */
    public int getManagerStaffId() { return managerStaffId; }
    /** @param managerStaffId  FK del gerente */
    public void setManagerStaffId(int managerStaffId) { this.managerStaffId = managerStaffId; }

    /** @return  ID de la dirección (FK → address) */
    public int getAddressId() { return addressId; }
    /** @param addressId  FK de la dirección */
    public void setAddressId(int addressId) { this.addressId = addressId; }

    /** @return  Timestamp de última actualización */
    public Timestamp getLastUpdate() { return lastUpdate; }
    /** @param lastUpdate  Nuevo timestamp */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    @Override
    public String toString() {
        // Muestra ID de tienda, gerente y dirección
        return String.format("[Tienda #%d] Gerente StaffID: %d | AddressID: %d",
                storeId, managerStaffId, addressId);
    }
}
