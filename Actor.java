// Paquete de modelos del proyecto
package com.sakila.models;

// Timestamp para el campo last_update de MySQL
import java.sql.Timestamp;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Actor: POJO que representa la tabla 'actor' de la BD sakila.
 * Almacena información de los actores del catálogo de películas.
 */
public class Actor {

    // actor_id: llave primaria autoincrement de la tabla actor
    private int actorId;

    // first_name: nombre del actor (VARCHAR 45)
    private String firstName;

    // last_name: apellido del actor (VARCHAR 45)
    private String lastName;

    // last_update: timestamp de la última modificación del registro
    private Timestamp lastUpdate;

    // -------------------------------------------------------------------------
    // CONSTRUCTORES
    // -------------------------------------------------------------------------

    /** Constructor vacío para el ORM */
    public Actor() {}

    /**
     * Constructor para crear actores nuevos.
     * No incluye actorId (lo genera la BD con autoincrement).
     *
     * @param firstName  Nombre del actor
     * @param lastName   Apellido del actor
     */
    public Actor(String firstName, String lastName) {
        this.firstName = firstName; // Asigna el nombre
        this.lastName  = lastName;  // Asigna el apellido
    }

    /**
     * Constructor completo (con ID) — usado al mapear desde la BD.
     *
     * @param actorId    ID del actor (PK)
     * @param firstName  Nombre
     * @param lastName   Apellido
     */
    public Actor(int actorId, String firstName, String lastName) {
        this.actorId   = actorId;   // Asigna el ID proveniente de la BD
        this.firstName = firstName; // Asigna el nombre
        this.lastName  = lastName;  // Asigna el apellido
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS
    // -------------------------------------------------------------------------

    /** @return  ID único del actor (PK autoincrement) */
    public int getActorId() { return actorId; }
    /** @param actorId  Nuevo ID (usar solo al mapear desde BD) */
    public void setActorId(int actorId) { this.actorId = actorId; }

    /** @return  Nombre del actor */
    public String getFirstName() { return firstName; }
    /** @param firstName  Nuevo nombre */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return  Apellido del actor */
    public String getLastName() { return lastName; }
    /** @param lastName  Nuevo apellido */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return  Timestamp de última actualización */
    public Timestamp getLastUpdate() { return lastUpdate; }
    /** @param lastUpdate  Nuevo timestamp */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    /**
     * Representación legible del actor.
     * Formato: [ID] NOMBRE APELLIDO
     */
    @Override
    public String toString() {
        return String.format("[%d] %-15s %-15s", actorId, firstName, lastName);
    }
}
