// Paquete de acceso a datos
package com.sakila.data;

// Importamos el modelo Actor
import com.sakila.models.Actor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * ActorData: DAO CONCRETO y FINAL para la tabla 'actor'.
 * Hereda todos los métodos CRUD de DataContext.
 * Es FINAL: no puede tener subclases.
 */
public final class ActorData extends DataContext<Actor> {

    /** Constructor: abre conexión a través del padre DataContext */
    public ActorData() {
        super(); // Llama al constructor del padre que abre la conexión
    }

    /**
     * mapResultSet — Mapea una fila de 'actor' a un objeto Actor.
     * Lee cada columna por nombre y asigna al modelo.
     *
     * @param rs  ResultSet en la fila actual
     * @return    Objeto Actor con los datos de la fila
     */
    @Override
    protected Actor mapResultSet(ResultSet rs) throws SQLException {
        Actor actor = new Actor(); // Crea el objeto vacío

        // Lee actor_id (PK autoincrement)
        actor.setActorId(rs.getInt("actor_id"));

        // Lee el nombre del actor
        actor.setFirstName(rs.getString("first_name"));

        // Lee el apellido del actor
        actor.setLastName(rs.getString("last_name"));

        // Lee el timestamp de última actualización
        actor.setLastUpdate(rs.getTimestamp("last_update"));

        return actor; // Retorna el objeto Actor completamente cargado
    }

    /**
     * @return  Nombre de la tabla: "actor"
     */
    @Override
    protected String getTableName() {
        return "actor"; // Tabla de actores en sakila
    }

    /**
     * @return  Columna PK de la tabla: "actor_id"
     */
    @Override
    protected String getPrimaryKeyColumn() {
        return "actor_id"; // PK de la tabla actor
    }

    /**
     * post — Inserta un nuevo actor en la BD.
     * No incluye actor_id (autoincrement).
     *
     * @param actor  Actor a insertar
     * @return       true si fue exitoso
     */
    @Override
    public boolean post(Actor actor) {
        // INSERT sin actor_id — la BD lo genera automáticamente
        String sql = "INSERT INTO actor (first_name, last_name) VALUES (?, ?)";

        // Ejecuta con nombre y apellido del actor
        return executeUpdate(sql, actor.getFirstName(), actor.getLastName());
    }

    /**
     * put — Actualiza nombre y apellido de un actor existente.
     * Usa actor_id como condición WHERE.
     *
     * @param actor  Actor con datos actualizados (debe tener actorId asignado)
     * @return       true si fue exitoso
     */
    @Override
    public boolean put(Actor actor) {
        // UPDATE con los nuevos datos, filtrando por PK
        String sql = "UPDATE actor SET first_name=?, last_name=? WHERE actor_id=?";

        // Parámetros: nuevo nombre, nuevo apellido, ID del actor a actualizar
        return executeUpdate(sql,
                actor.getFirstName(),  // Nuevo nombre
                actor.getLastName(),   // Nuevo apellido
                actor.getActorId()     // WHERE actor_id = ? (identifica el registro)
        );
    }

    /**
     * buscarPorApellido — Busca actores por apellido (búsqueda parcial).
     *
     * @param apellido  Texto a buscar en last_name
     * @return          Lista de actores que coinciden
     */
    public List<Actor> buscarPorApellido(String apellido) {
        // LIKE con comodines para búsqueda parcial
        String sql = "SELECT * FROM actor WHERE last_name LIKE ?";

        // Ejecuta con el patrón de búsqueda
        ResultSet rs = executeQuery(sql, "%" + apellido + "%");

        List<Actor> lista = new ArrayList<>(); // Lista de resultados

        try {
            while (rs != null && rs.next()) {
                lista.add(mapResultSet(rs)); // Convierte y agrega cada actor
            }
        } catch (SQLException e) {
            System.err.println("[ActorData] buscarPorApellido error: " + e.getMessage());
        }
        return lista;
    }

    /**
     * getFilmsPorActor — Retorna los títulos de películas en las que participó un actor.
     * Usa JOIN entre film_actor y film.
     *
     * @param actorId  ID del actor
     * @return         Lista de títulos de películas
     */
    public List<String> getFilmsPorActor(int actorId) {
        // JOIN para obtener títulos de películas de un actor específico
        String sql = "SELECT f.title FROM film_actor fa " +
                     "JOIN film f ON fa.film_id = f.film_id " +
                     "WHERE fa.actor_id = ? ORDER BY f.title";

        ResultSet rs = executeQuery(sql, actorId); // Ejecuta el JOIN

        List<String> titulos = new ArrayList<>(); // Lista de títulos

        try {
            while (rs != null && rs.next()) {
                titulos.add(rs.getString("title")); // Agrega cada título a la lista
            }
        } catch (SQLException e) {
            System.err.println("[ActorData] getFilmsPorActor error: " + e.getMessage());
        }
        return titulos; // Lista de películas del actor
    }
}
