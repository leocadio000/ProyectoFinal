// Paquete de acceso a datos del proyecto
package com.sakila.data;

// Importamos el modelo Film que este DAO gestiona
import com.sakila.models.Film;
// ResultSet para mapear filas de la BD
import java.sql.ResultSet;
// SQLException para manejo de errores de SQL
import java.sql.SQLException;
// BigDecimal para campos monetarios
import java.math.BigDecimal;
// List para retornar colecciones
import java.util.List;
// ArrayList como implementación de List
import java.util.ArrayList;
// HashMap para estadísticas en memoria (requerido por las normas)
import java.util.HashMap;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * FilmData: Clase DAO (Data Access Object) CONCRETA y FINAL para la tabla 'film'.
 * - Hereda de DataContext<Film>: obtiene todos los métodos CRUD base
 * - Es FINAL: no puede tener subclases (según requerimiento del proyecto)
 * - Implementa los métodos abstractos de DataContext para la entidad Film
 * - Agrega métodos de búsqueda específicos para películas
 */
public final class FilmData extends DataContext<Film> {

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    /**
     * Constructor: llama al padre DataContext que abre la conexión MySQL.
     * No necesita lógica adicional para Film.
     */
    public FilmData() {
        super(); // Llama al constructor de DataContext (abre la conexión)
    }

    // =========================================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS DEL PADRE
    // =========================================================================

    /**
     * mapResultSet — Convierte una fila del ResultSet en un objeto Film.
     * Lee cada columna por nombre y asigna el valor al campo correspondiente.
     *
     * @param rs  ResultSet posicionado en la fila actual
     * @return    Objeto Film con todos los datos de esa fila
     */
    @Override
    protected Film mapResultSet(ResultSet rs) throws SQLException {
        Film film = new Film(); // Crea un Film vacío para llenarlo con los datos de la fila

        // Asigna la PK (film_id) al objeto — respeta el autoincrement de la BD
        film.setFilmId(rs.getInt("film_id"));

        // Asigna el título de la película
        film.setTitle(rs.getString("title"));

        // Asigna la descripción/sinopsis (puede ser null en la BD)
        film.setDescription(rs.getString("description"));

        // Asigna el año de estreno
        film.setReleaseYear(rs.getInt("release_year"));

        // Asigna el FK del idioma (referencia a tabla language)
        film.setLanguageId(rs.getInt("language_id"));

        // Asigna el FK del idioma original (puede ser 0/null si no aplica)
        film.setOriginalLanguageId(rs.getInt("original_language_id"));

        // Asigna la duración del período de renta en días
        film.setRentalDuration(rs.getInt("rental_duration"));

        // Asigna el precio de renta como BigDecimal (precisión monetaria)
        film.setRentalRate(rs.getBigDecimal("rental_rate"));

        // Asigna la duración de la película en minutos
        film.setLength(rs.getInt("length"));

        // Asigna el costo de reposición como BigDecimal
        film.setReplacementCost(rs.getBigDecimal("replacement_cost"));

        // Asigna la clasificación (G, PG, PG-13, R, NC-17)
        film.setRating(rs.getString("rating"));

        // Asigna las características especiales (Trailers, etc.)
        film.setSpecialFeatures(rs.getString("special_features"));

        // Asigna el timestamp de última actualización
        film.setLastUpdate(rs.getTimestamp("last_update"));

        return film; // Retorna el objeto Film completamente cargado
    }

    /**
     * getTableName — Retorna el nombre exacto de la tabla en MySQL.
     * Usado por los métodos heredados del padre (get, getAll, delete).
     *
     * @return  "film" — nombre de la tabla en la BD sakila
     */
    @Override
    protected String getTableName() {
        return "film"; // Nombre de la tabla en la BD
    }

    /**
     * getPrimaryKeyColumn — Retorna el nombre de la columna PK.
     * Usado por los métodos heredados del padre para WHERE clauses.
     *
     * @return  "film_id" — columna PK de la tabla film
     */
    @Override
    protected String getPrimaryKeyColumn() {
        return "film_id"; // Nombre de la PK en la BD
    }

    // =========================================================================
    // IMPLEMENTACIÓN DE post() Y put() — Específicos para Film
    // =========================================================================

    /**
     * post — Inserta una nueva película en la tabla 'film'.
     * NO incluye film_id en el INSERT (la BD lo genera con autoincrement).
     * Respeta la regla de PK autoincrement del requerimiento.
     *
     * @param film  Objeto Film con los datos a insertar
     * @return      true si la inserción fue exitosa
     */
    @Override
    public boolean post(Film film) {
        // SQL de inserción — los '?' serán reemplazados por los valores del objeto
        // No incluimos film_id porque es AUTOINCREMENT
        String sql = "INSERT INTO film (title, description, release_year, language_id, " +
                     "rental_duration, rental_rate, length, replacement_cost, rating, special_features) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Ejecuta el INSERT con los valores del objeto Film y retorna el resultado
        return executeUpdate(sql,
                film.getTitle(),           // ? 1 — título
                film.getDescription(),     // ? 2 — descripción
                film.getReleaseYear(),     // ? 3 — año
                film.getLanguageId(),      // ? 4 — FK idioma
                film.getRentalDuration(),  // ? 5 — días de renta
                film.getRentalRate(),      // ? 6 — precio de renta
                film.getLength(),          // ? 7 — duración
                film.getReplacementCost(), // ? 8 — costo reposición
                film.getRating(),          // ? 9 — clasificación
                film.getSpecialFeatures()  // ? 10 — características
        );
    }

    /**
     * put — Actualiza los datos de una película existente.
     * Usa film_id como condición del WHERE para identificar el registro.
     * Respeta la integridad de la PK.
     *
     * @param film  Objeto Film con datos actualizados (debe tener filmId asignado)
     * @return      true si la actualización fue exitosa
     */
    @Override
    public boolean put(Film film) {
        // SQL de actualización — actualiza todos los campos editables
        String sql = "UPDATE film SET title=?, description=?, release_year=?, language_id=?, " +
                     "rental_duration=?, rental_rate=?, length=?, replacement_cost=?, " +
                     "rating=?, special_features=? WHERE film_id=?";

        // Ejecuta el UPDATE — el último parámetro es el film_id para el WHERE
        return executeUpdate(sql,
                film.getTitle(),           // Nuevo título
                film.getDescription(),     // Nueva descripción
                film.getReleaseYear(),     // Nuevo año
                film.getLanguageId(),      // Nuevo FK idioma
                film.getRentalDuration(),  // Nuevos días de renta
                film.getRentalRate(),      // Nuevo precio
                film.getLength(),          // Nueva duración
                film.getReplacementCost(), // Nuevo costo reposición
                film.getRating(),          // Nueva clasificación
                film.getSpecialFeatures(), // Nuevas características
                film.getFilmId()           // WHERE film_id = ? (identifica el registro a actualizar)
        );
    }

    // =========================================================================
    // MÉTODOS ESPECÍFICOS DE BÚSQUEDA PARA FILM (overloads del get)
    // =========================================================================

    /**
     * Busca películas por título (búsqueda parcial con LIKE).
     * Ejemplo: buscarPorTitulo("ACT") retorna todas las películas que contienen "ACT".
     *
     * @param titulo  Texto a buscar dentro del título
     * @return        Lista de películas que coinciden
     */
    public List<Film> buscarPorTitulo(String titulo) {
        // LIKE '%texto%' busca el patrón en cualquier posición del título
        String sql = "SELECT * FROM film WHERE title LIKE ?";

        // Agrega los comodines '%' al texto de búsqueda
        ResultSet rs = executeQuery(sql, "%" + titulo + "%");

        // Lista que acumulará los resultados
        List<Film> lista = new ArrayList<>();

        try {
            // Itera cada fila del resultado y la convierte en objeto Film
            while (rs != null && rs.next()) {
                lista.add(mapResultSet(rs)); // Convierte la fila en Film y agrega a la lista
            }
        } catch (SQLException e) {
            System.err.println("[FilmData] buscarPorTitulo error: " + e.getMessage());
        }
        return lista; // Retorna las películas encontradas (lista vacía si no hay coincidencias)
    }

    /**
     * Busca películas por clasificación (G, PG, PG-13, R, NC-17).
     *
     * @param rating  Clasificación a filtrar
     * @return        Lista de películas con esa clasificación
     */
    public List<Film> buscarPorRating(String rating) {
        // SELECT exacto por rating (no usa LIKE, es igualdad)
        String sql = "SELECT * FROM film WHERE rating = ?";

        // Ejecuta el SELECT con el rating como filtro
        ResultSet rs = executeQuery(sql, rating);

        // Lista de resultados
        List<Film> lista = new ArrayList<>();

        try {
            while (rs != null && rs.next()) {
                lista.add(mapResultSet(rs)); // Agrega cada film encontrado
            }
        } catch (SQLException e) {
            System.err.println("[FilmData] buscarPorRating error: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene estadísticas de películas en inventario usando HashMap.
     * Calcula total, promedio de renta y precio máximo/mínimo.
     * Usa HashMap<String, Object> para almacenar métricas diversas.
     *
     * @return  HashMap con claves: "total", "promedio_rental", "max_rate", "min_rate"
     */
    public HashMap<String, Object> getEstadisticas() {
        // HashMap para almacenar las estadísticas — key=nombre métrica, value=valor
        HashMap<String, Object> stats = new HashMap<>();

        // SQL que calcula métricas directamente en la BD (más eficiente que en Java)
        String sql = "SELECT COUNT(*) as total, " +
                     "AVG(rental_rate) as promedio_renta, " +
                     "MAX(rental_rate) as max_renta, " +
                     "MIN(rental_rate) as min_renta, " +
                     "AVG(length) as promedio_duracion " +
                     "FROM film";

        // Ejecuta la consulta de estadísticas
        ResultSet rs = executeQuery(sql);

        try {
            if (rs != null && rs.next()) {
                // Almacena cada métrica en el HashMap con una clave descriptiva
                stats.put("total",              rs.getInt("total"));              // Nro total de películas
                stats.put("promedio_renta",     rs.getBigDecimal("promedio_renta")); // Precio promedio
                stats.put("max_renta",          rs.getBigDecimal("max_renta"));      // Precio máximo
                stats.put("min_renta",          rs.getBigDecimal("min_renta"));      // Precio mínimo
                stats.put("promedio_duracion",  rs.getBigDecimal("promedio_duracion")); // Minutos promedio
            }
        } catch (SQLException e) {
            System.err.println("[FilmData] getEstadisticas error: " + e.getMessage());
        }
        return stats; // Retorna el mapa con todas las métricas calculadas
    }

    /**
     * Obtiene películas con sus actores usando JOIN.
     * Útil para el reporte de actores y películas en que participa.
     *
     * @param filmId  ID de la película
     * @return        Lista de nombres de actores en formato "Nombre Apellido"
     */
    public List<String> getActoresPorFilm(int filmId) {
        // JOIN entre film_actor y actor para obtener nombres de actores de una película
        String sql = "SELECT a.first_name, a.last_name " +
                     "FROM film_actor fa " +
                     "JOIN actor a ON fa.actor_id = a.actor_id " +
                     "WHERE fa.film_id = ?";

        // Ejecuta el JOIN con el film_id como filtro
        ResultSet rs = executeQuery(sql, filmId);

        // Lista para almacenar los nombres de actores
        List<String> actores = new ArrayList<>();

        try {
            while (rs != null && rs.next()) {
                // Concatena nombre y apellido y los agrega a la lista
                actores.add(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        } catch (SQLException e) {
            System.err.println("[FilmData] getActoresPorFilm error: " + e.getMessage());
        }
        return actores; // Lista de actores de la película
    }
}
