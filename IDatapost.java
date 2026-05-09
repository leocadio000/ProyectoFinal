// Paquete principal de datos del proyecto sakila ORM
package com.sakila.data;

// Importamos List para manejar colecciones genéricas de objetos
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Interfaz iDatapost: define el CONTRATO CRUD estándar que toda clase
 * de acceso a datos debe cumplir obligatoriamente.
 * CRUD = Create (Post), Read (Get), Update (Put), Delete (Delete)
 *
 * Usa genéricos <T> para ser reutilizable con cualquier modelo (Film, Actor, etc.)
 */
public interface IDatapost<T> {

    /**
     * POST — Inserta un nuevo registro en la base de datos.
     * Equivalente a SQL INSERT INTO.
     * @param entity  Objeto del modelo que se desea guardar
     * @return        true si la inserción fue exitosa, false si falló
     */
    boolean post(T entity); // Contrato de inserción — toda clase hija debe implementarlo

    /**
     * GET por ID — Busca y retorna UN registro por su llave primaria.
     * Equivalente a SQL SELECT * WHERE id = ?
     * @param id  Identificador único (PK) del registro buscado
     * @return    Objeto encontrado, o null si no existe
     */
    T get(int id); // Contrato de búsqueda por PK — debe ser implementado

    /**
     * GET ALL — Retorna TODOS los registros de la tabla.
     * Equivalente a SQL SELECT * FROM tabla
     * @return  Lista con todos los objetos del modelo
     */
    List<T> getAll(); // Contrato de listado completo

    /**
     * GET por campo — Sobrecarga: busca registros por nombre de columna y valor.
     * Permite búsquedas flexibles sin conocer el ID.
     * @param column  Nombre de la columna a filtrar (ej: "title", "last_name")
     * @param value   Valor a buscar en esa columna
     * @return        Lista de objetos que coinciden con el criterio
     */
    List<T> get(String column, Object value); // Sobrecarga de get para búsqueda por campo

    /**
     * PUT — Actualiza un registro existente en la base de datos.
     * Equivalente a SQL UPDATE SET WHERE id = ?
     * @param entity  Objeto con los nuevos datos (debe tener la PK asignada)
     * @return        true si la actualización fue exitosa, false si falló
     */
    boolean put(T entity); // Contrato de actualización — obligatorio para todas las entidades

    /**
     * DELETE — Elimina un registro de la base de datos por su PK.
     * Equivalente a SQL DELETE FROM tabla WHERE id = ?
     * @param id  Identificador único del registro a eliminar
     * @return    true si se eliminó correctamente, false si falló
     */
    boolean delete(int id); // Contrato de eliminación por llave primaria
}
