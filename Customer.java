// Paquete de modelos
package com.sakila.models;

// Date para el campo create_date de MySQL
import java.sql.Date;
// Timestamp para last_update
import java.sql.Timestamp;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Customer: POJO que representa la tabla 'customer' de la BD sakila.
 * Almacena datos de los clientes de la tienda de renta de películas.
 * FK: store_id → store | address_id → address
 */
public class Customer {

    // customer_id: PK autoincrement de la tabla customer
    private int customerId;

    // store_id: FK que referencia a la tienda donde está registrado el cliente
    private int storeId;

    // first_name: nombre del cliente (VARCHAR 45)
    private String firstName;

    // last_name: apellido del cliente (VARCHAR 45)
    private String lastName;

    // email: correo electrónico del cliente (VARCHAR 50, puede ser null)
    private String email;

    // address_id: FK que referencia la dirección del cliente
    private int addressId;

    // active: indica si el cliente está activo (TINYINT 1=activo, 0=inactivo)
    private boolean active;

    // create_date: fecha en que se registró el cliente en el sistema
    private Date createDate;

    // last_update: timestamp de la última modificación
    private Timestamp lastUpdate;

    // -------------------------------------------------------------------------
    // CONSTRUCTORES
    // -------------------------------------------------------------------------

    /** Constructor vacío para el ORM */
    public Customer() {}

    /**
     * Constructor para registrar un nuevo cliente.
     * No incluye customerId (autoincrement).
     *
     * @param storeId    ID de la tienda donde se registra
     * @param firstName  Nombre del cliente
     * @param lastName   Apellido del cliente
     * @param email      Correo electrónico
     * @param addressId  ID de la dirección (FK)
     * @param active     Estado activo/inactivo
     */
    public Customer(int storeId, String firstName, String lastName,
                    String email, int addressId, boolean active) {
        this.storeId   = storeId;   // FK de tienda
        this.firstName = firstName; // Nombre
        this.lastName  = lastName;  // Apellido
        this.email     = email;     // Email
        this.addressId = addressId; // FK de dirección
        this.active    = active;    // Estado
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------

    /** @return  ID del cliente (PK) */
    public int getCustomerId() { return customerId; }
    /** @param customerId  ID del cliente */
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    /** @return  ID de la tienda (FK) */
    public int getStoreId() { return storeId; }
    /** @param storeId  FK de la tienda */
    public void setStoreId(int storeId) { this.storeId = storeId; }

    /** @return  Nombre del cliente */
    public String getFirstName() { return firstName; }
    /** @param firstName  Nuevo nombre */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return  Apellido del cliente */
    public String getLastName() { return lastName; }
    /** @param lastName  Nuevo apellido */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return  Email del cliente */
    public String getEmail() { return email; }
    /** @param email  Nuevo email */
    public void setEmail(String email) { this.email = email; }

    /** @return  ID de dirección (FK) */
    public int getAddressId() { return addressId; }
    /** @param addressId  FK de dirección */
    public void setAddressId(int addressId) { this.addressId = addressId; }

    /** @return  true si el cliente está activo */
    public boolean isActive() { return active; }
    /** @param active  true=activo, false=inactivo */
    public void setActive(boolean active) { this.active = active; }

    /** @return  Fecha de registro */
    public Date getCreateDate() { return createDate; }
    /** @param createDate  Fecha de registro */
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    /** @return  Timestamp de última actualización */
    public Timestamp getLastUpdate() { return lastUpdate; }
    /** @param lastUpdate  Nuevo timestamp */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Representación legible del cliente.
     * Formato: [ID] Nombre Apellido | Email | Tienda | Activo
     */
    @Override
    public String toString() {
        return String.format("[%d] %-15s %-15s | %-35s | Store:%d | Active:%b",
                customerId, firstName, lastName, email, storeId, active);
    }
}
