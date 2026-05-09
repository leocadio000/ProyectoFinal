// Paquete de modelos del proyecto: contiene todas las clases POJO
package com.sakila.models;

// Importamos BigDecimal para manejar valores monetarios con precisión exacta
import java.math.BigDecimal;
// Importamos Timestamp para campos de fecha y hora de MySQL
import java.sql.Timestamp;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Film: Modelo (POJO) que representa la tabla 'film' de la BD sakila.
 * Cada atributo corresponde exactamente a una columna de la tabla.
 * Esta clase NO tiene lógica de negocio ni acceso a BD — solo almacena datos.
 */
public class Film {

    // film_id: llave primaria autoincrement de la tabla film
    private int filmId;

    // title: título de la película (VARCHAR 255 en BD)
    private String title;

    // description: sinopsis de la película (TEXT en BD, puede ser null)
    private String description;

    // release_year: año de estreno de la película (YEAR en BD)
    private int releaseYear;

    // language_id: FK que referencia a la tabla 'language' (idioma original)
    private int languageId;

    // original_language_id: FK opcional al idioma original (puede ser null)
    private int originalLanguageId;

    // rental_duration: días que dura el período de renta (TINYINT en BD)
    private int rentalDuration;

    // rental_rate: precio de renta (DECIMAL 4,2 en BD)
    private BigDecimal rentalRate;

    // length: duración de la película en minutos (SMALLINT en BD)
    private int length;

    // replacement_cost: costo de reposición si la película no se devuelve (DECIMAL 5,2)
    private BigDecimal replacementCost;

    // rating: clasificación de la película (ENUM: 'G','PG','PG-13','R','NC-17')
    private String rating;

    // special_features: características especiales (SET en BD: Trailers, Commentaries, etc.)
    private String specialFeatures;

    // last_update: fecha y hora de la última modificación del registro (TIMESTAMP)
    private Timestamp lastUpdate;

    // -------------------------------------------------------------------------
    // CONSTRUCTORES
    // -------------------------------------------------------------------------

    /**
     * Constructor vacío — requerido por el ORM para instanciar objetos vacíos
     * antes de llenarlos con los datos del ResultSet.
     */
    public Film() {}

    /**
     * Constructor con parámetros principales para crear películas nuevas.
     * No incluye filmId porque es autoincrement (lo asigna la BD automáticamente).
     *
     * @param title           Título de la película
     * @param description     Sinopsis de la película
     * @param releaseYear     Año de estreno
     * @param languageId      ID del idioma (FK a tabla language)
     * @param rentalDuration  Días de renta permitidos
     * @param rentalRate      Precio de renta por período
     * @param length          Duración en minutos
     * @param replacementCost Costo de reposición
     * @param rating          Clasificación de edad
     */
    public Film(String title, String description, int releaseYear, int languageId,
                int rentalDuration, BigDecimal rentalRate, int length,
                BigDecimal replacementCost, String rating) {
        this.title           = title;           // Asigna el título recibido
        this.description     = description;     // Asigna la descripción
        this.releaseYear     = releaseYear;     // Asigna el año
        this.languageId      = languageId;      // Asigna FK de idioma
        this.rentalDuration  = rentalDuration;  // Asigna días de renta
        this.rentalRate      = rentalRate;      // Asigna precio de renta
        this.length          = length;          // Asigna duración
        this.replacementCost = replacementCost; // Asigna costo de reposición
        this.rating          = rating;          // Asigna clasificación
    }

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS — permiten acceso controlado a los atributos privados
    // -------------------------------------------------------------------------

    /** @return  ID único de la película (PK) */
    public int getFilmId() { return filmId; }
    /** @param filmId  Nuevo valor del ID (solo usar al mapear desde BD) */
    public void setFilmId(int filmId) { this.filmId = filmId; }

    /** @return  Título de la película */
    public String getTitle() { return title; }
    /** @param title  Nuevo título */
    public void setTitle(String title) { this.title = title; }

    /** @return  Descripción/sinopsis */
    public String getDescription() { return description; }
    /** @param description  Nueva descripción */
    public void setDescription(String description) { this.description = description; }

    /** @return  Año de estreno */
    public int getReleaseYear() { return releaseYear; }
    /** @param releaseYear  Nuevo año */
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    /** @return  ID del idioma (FK) */
    public int getLanguageId() { return languageId; }
    /** @param languageId  Nuevo FK de idioma */
    public void setLanguageId(int languageId) { this.languageId = languageId; }

    /** @return  ID del idioma original (FK, puede ser 0 si no aplica) */
    public int getOriginalLanguageId() { return originalLanguageId; }
    /** @param originalLanguageId  Nuevo FK de idioma original */
    public void setOriginalLanguageId(int originalLanguageId) { this.originalLanguageId = originalLanguageId; }

    /** @return  Días de renta */
    public int getRentalDuration() { return rentalDuration; }
    /** @param rentalDuration  Nuevos días de renta */
    public void setRentalDuration(int rentalDuration) { this.rentalDuration = rentalDuration; }

    /** @return  Precio de renta (BigDecimal para precisión monetaria) */
    public BigDecimal getRentalRate() { return rentalRate; }
    /** @param rentalRate  Nuevo precio de renta */
    public void setRentalRate(BigDecimal rentalRate) { this.rentalRate = rentalRate; }

    /** @return  Duración en minutos */
    public int getLength() { return length; }
    /** @param length  Nueva duración en minutos */
    public void setLength(int length) { this.length = length; }

    /** @return  Costo de reposición */
    public BigDecimal getReplacementCost() { return replacementCost; }
    /** @param replacementCost  Nuevo costo de reposición */
    public void setReplacementCost(BigDecimal replacementCost) { this.replacementCost = replacementCost; }

    /** @return  Clasificación (G, PG, PG-13, R, NC-17) */
    public String getRating() { return rating; }
    /** @param rating  Nueva clasificación */
    public void setRating(String rating) { this.rating = rating; }

    /** @return  Características especiales */
    public String getSpecialFeatures() { return specialFeatures; }
    /** @param specialFeatures  Nuevas características */
    public void setSpecialFeatures(String specialFeatures) { this.specialFeatures = specialFeatures; }

    /** @return  Timestamp de la última actualización */
    public Timestamp getLastUpdate() { return lastUpdate; }
    /** @param lastUpdate  Nuevo timestamp de actualización */
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    // -------------------------------------------------------------------------
    // toString — representación textual del objeto (útil para mostrar en consola)
    // -------------------------------------------------------------------------

    /**
     * Retorna una representación legible de la película.
     * Formato: [ID] Título (Año) | Rating: X | Rent: $0.00 | Duración min
     */
    @Override
    public String toString() {
        // String.format construye la cadena con los valores del objeto
        return String.format("[%d] %-35s (%d) | Rating: %-5s | Rent: $%.2f | %d min",
                filmId, title, releaseYear, rating, rentalRate, length);
    }
}
