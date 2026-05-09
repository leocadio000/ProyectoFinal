// Paquete de controladores MVC del proyecto
package com.sakila.controllers;

// Importamos el DAO y el modelo que este controlador gestiona
import com.sakila.data.FilmData;
import com.sakila.models.Film;

// Importaciones para colecciones genéricas
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * FilmController: Controlador MVC para gestionar la entidad Film.
 * - Actúa como intermediario entre la Vista (UI) y el Modelo (FilmData)
 * - Aplica validaciones y reglas de negocio antes de acceder a la BD
 * - Gestiona una List<Film> en memoria para operaciones de la vista
 * - Patrón MVC: Controller no dibuja pantallas ni accede directamente a BD
 */
public class FilmController {

    // DAO de películas — acceso a la BD para operaciones CRUD de Film
    private FilmData filmData;

    // Lista en memoria que mantiene el catálogo de películas cargado
    // Permite operar sobre la lista sin consultar la BD en cada acción
    private List<Film> listaFilms;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    /**
     * Constructor: inicializa el DAO y la lista en memoria.
     * Carga automáticamente todas las películas al crear el controlador.
     */
    public FilmController() {
        filmData   = new FilmData();    // Crea el DAO que abre la conexión BD
        listaFilms = new ArrayList<>(); // Inicializa la lista vacía en memoria
        cargarTodos();                  // Carga todas las películas desde la BD
    }

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD — Aplican validación antes de llamar al DAO
    // -------------------------------------------------------------------------

    /**
     * agregar — Valida y agrega una nueva película.
     * Verifica que el título no esté vacío antes de insertar.
     *
     * @param film  Objeto Film a insertar
     * @return      true si fue insertado correctamente
     */
    public boolean agregar(Film film) {
        // Validación: el título no puede ser null ni estar en blanco
        if (film.getTitle() == null || film.getTitle().trim().isEmpty()) {
            System.err.println("[FilmController] Error: El título no puede estar vacío.");
            return false; // Rechaza la inserción si no hay título
        }
        // Validación: el precio de renta debe ser mayor a cero
        if (film.getRentalRate() == null || film.getRentalRate().doubleValue() <= 0) {
            System.err.println("[FilmController] Error: El rental rate debe ser mayor a 0.");
            return false; // Rechaza si el precio no es válido
        }

        boolean resultado = filmData.post(film); // Delega al DAO la inserción en BD

        if (resultado) {
            listaFilms.add(film); // Si fue exitoso, agrega también a la lista en memoria
            System.out.println("[FilmController] Película agregada: " + film.getTitle());
        }
        return resultado; // Retorna el resultado de la operación
    }

    /**
     * actualizar — Valida y actualiza los datos de una película.
     *
     * @param film  Film con datos actualizados (debe tener filmId asignado)
     * @return      true si fue actualizado correctamente
     */
    public boolean actualizar(Film film) {
        // Validación: el ID debe ser mayor a 0 para identificar el registro
        if (film.getFilmId() <= 0) {
            System.err.println("[FilmController] Error: ID de película no válido.");
            return false; // No actualiza si el ID no es válido
        }

        boolean resultado = filmData.put(film); // Delega al DAO la actualización en BD

        if (resultado) {
            // Actualiza también en la lista en memoria buscando por ID
            for (int i = 0; i < listaFilms.size(); i++) {
                if (listaFilms.get(i).getFilmId() == film.getFilmId()) {
                    listaFilms.set(i, film); // Reemplaza el elemento en la posición i
                    break; // Sale del bucle al encontrar y actualizar
                }
            }
        }
        return resultado;
    }

    /**
     * eliminar — Elimina una película por su ID.
     *
     * @param filmId  ID de la película a eliminar
     * @return        true si fue eliminado
     */
    public boolean eliminar(int filmId) {
        // Validación: ID debe ser positivo
        if (filmId <= 0) {
            System.err.println("[FilmController] Error: ID no válido para eliminar.");
            return false;
        }

        boolean resultado = filmData.delete(filmId); // Delega al DAO el DELETE en BD

        if (resultado) {
            // Elimina también de la lista en memoria usando removeIf con lambda
            listaFilms.removeIf(f -> f.getFilmId() == filmId); // Remueve el film con ese ID
            System.out.println("[FilmController] Película eliminada. ID: " + filmId);
        }
        return resultado;
    }

    /**
     * buscarPorId — Busca una película por su ID.
     * Primero busca en la lista en memoria (más rápido), luego en BD.
     *
     * @param filmId  ID a buscar
     * @return        Objeto Film encontrado, o null si no existe
     */
    public Film buscarPorId(int filmId) {
        // Busca primero en la lista en memoria para evitar consultas innecesarias
        for (Film f : listaFilms) {
            if (f.getFilmId() == filmId) return f; // Retorna si encuentra en memoria
        }
        // Si no está en memoria, consulta la BD directamente
        return filmData.get(filmId);
    }

    /**
     * buscarPorTitulo — Busca películas por texto en el título.
     *
     * @param titulo  Texto a buscar
     * @return        Lista de películas que contienen el texto
     */
    public List<Film> buscarPorTitulo(String titulo) {
        return filmData.buscarPorTitulo(titulo); // Delega la búsqueda al DAO
    }

    /**
     * buscarPorRating — Filtra películas por clasificación.
     *
     * @param rating  Clasificación (G, PG, PG-13, R, NC-17)
     * @return        Lista de películas con esa clasificación
     */
    public List<Film> buscarPorRating(String rating) {
        return filmData.buscarPorRating(rating); // Delega al DAO
    }

    /**
     * getActoresDePelicula — Retorna los actores que participan en una película.
     *
     * @param filmId  ID de la película
     * @return        Lista de nombres de actores
     */
    public List<String> getActoresDePelicula(int filmId) {
        return filmData.getActoresPorFilm(filmId); // JOIN en el DAO
    }

    /**
     * getEstadisticas — Obtiene estadísticas del catálogo de películas.
     *
     * @return  HashMap con métricas calculadas
     */
    public HashMap<String, Object> getEstadisticas() {
        return filmData.getEstadisticas(); // Delega al DAO el cálculo
    }

    /**
     * getTodos — Retorna la lista completa de películas en memoria.
     *
     * @return  List<Film> con todas las películas cargadas
     */
    public List<Film> getTodos() {
        return listaFilms; // Retorna la lista en memoria (sin nueva consulta BD)
    }

    /**
     * cargarTodos — Recarga todas las películas desde la BD hacia la lista en memoria.
     * Útil para sincronizar después de cambios externos.
     */
    public void cargarTodos() {
        listaFilms.clear();              // Limpia la lista actual para evitar duplicados
        listaFilms = filmData.getAll();  // Carga todas las películas desde la BD
    }

    /**
     * cerrar — Cierra la conexión del DAO al finalizar el uso del controlador.
     * Debe llamarse al terminar para liberar recursos de conexión.
     */
    public void cerrar() {
        filmData.closeConnection(); // Libera la conexión MySQL
    }
}
