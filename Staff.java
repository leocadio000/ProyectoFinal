// Paquete de modelos del proyecto sakila
package com.sakila.models;

import java.sql.Timestamp;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Staff: POJO que representa la tabla 'staff' de la base de datos sakila.
 * Almacena los empleados de las tiendas de renta de películas.
 * FK: address_id → address | store_id → store
 */
public class Staff {

    // staff_id: PK autoincrement de la tabla staff
    private int staffId;

    // first_name: nombre del empleado (VARCHAR 45)
    private String firstName;

    // last_name: apellido del empleado (VARCHAR 45)
    private String lastName;

    // address_id: FK que referencia la dirección del empleado
    private int addressId;

    // email: correo electrónico del empleado (VARCHAR 50, puede ser null)
    private String email;

    // store_id: FK que referencia la tienda donde trabaja el empleado
    private int storeId;

    // active: indica si el empleado está activo (TINYINT 1=activo, 0=inactivo)
    private boolean active;

    // username: nombre de usuario para el sistema (VARCHAR 16)
    private String username;

    // password: contraseña hasheada del empleado (VARCHAR 40, puede ser null)
    private String password;

    // last_update: timestamp de la última modificación
    private Timestamp lastUpdate;

    // -------------------------------------------------------------------------
    // CONSTRUCTORES
    // -------------------------------------------------------------------------

    /** Constructor vacío para el ORM */
    public Staff() {}

    /**
     * Constructor para crear un nuevo empleado.
     * No incluye staffId (autoincrement).
     *
     * @param firstName  Nombre del empleado
     * @param lastName   Apellido del empleado
     * @param addressId  FK de la dirección
     * @param storeId    FK de la tienda donde trabaja
     * @param username   Nombre de usuario del sistema
     * @param active     Estado activo/inactivo
     */
    public Staff(String firstName, String lastName, int addressId,
                 int storeId, String username, boolean active) {
        this.firstName = firstName; // Nombre del empleado
        this.lastName  = lastName;  // Apellido del empleado
        this.addressId = addressId; // FK de su dirección
        this.storeId   = storeId;   // FK de su tienda
        this.username  = username;  // Usuario del sistema
        this.active    = active;    // Estado activo
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------

    /** @return  ID del empleado (PK) */
    public int getStaffId() { return staffId; }
    /** @param staffId  ID del empleado */
    public void setStaffId(int staffId) { this.staffId = staffId; }

    /** @return  Nombre del empleado */
    public String getFirstName() { return firstName; }
    /** @param firstName  Nuevo nombre */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return  Apellido del empleado */
    public String getLastName() { return lastName; }
    /** @param lastName  Nuevo apellido */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return  ID de la dirección (FK) */
    public int getAddressId() { return addressId; }
    /** @param addressId  FK de dirección */
    public void setAddressId(int addressId) { this.addressId = addressId; }

    /** @return  Email del empleado */
    public String getEmail() { return email; }
    /** @param email  Nuevo email */
    public void setEmail(String email) { this.email = email; }

    /** @return  ID de la tienda (FK) */
    public int getStoreId() { return storeId; }
    /** @param storeId  FK de la tienda */
    public void setStoreId(int storeId) { this.storeId = storeId; }

    /** @return  true si el empleado está activo */
    public boolean isActive() { return active; }
    /** @param active  Estado activo/inactivo */
    public void setActive(boolean active) { this.active = active; }

    /** @return  Nombre de usuario del sistema */
    public String getUsername() { return username; }
    /** @param username  Nuevo nombre de usuario */
    public void setUsername(String username) { this.username = username; }

    /** @return  Contraseña hasheada */
    public String getPassword() { return password; }
    /** @param password  Nueva contraseña */
    public void setPassword(String password) { this.password = password; }

    /** @return  Timestamp de última actualización */
    public Timestamp getLastUpdate() { return lastUpdate; }
    /** @param lastUpdate  Nuevo timestamp */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    @Override
    public String toString() {
        // Muestra ID, nombre completo, tienda y estado
        return String.format("[%d] %-15s %-15s | Store:%d | User:%-10s | Active:%b",
                staffId, firstName, lastName, storeId, username, active);
    }
}
