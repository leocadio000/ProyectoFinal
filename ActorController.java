// Paquete de controladores MVC
package com.sakila.controllers;

import com.sakila.data.ActorData;
import com.sakila.models.Actor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * ActorController: Controlador MVC para gestionar actores del catálogo sakila.
 */
public class ActorController {

    private ActorData actorData;       // DAO de actores para acceso a BD
    private List<Actor> listaActores;  // Lista en memoria de actores cargados

    /** Constructor: inicializa DAO y carga actores */
    public ActorController() {
        actorData   = new ActorData();    // Crea el DAO con conexión BD
        listaActores = new ArrayList<>(); // Lista vacía en memoria
        cargarTodos();                    // Carga todos los actores desde BD
    }

    /**
     * agregar — Agrega un nuevo actor validando que tenga nombre y apellido.
     *
     * @param actor  Actor a insertar
     * @return       true si fue insertado correctamente
     */
    public boolean agregar(Actor actor) {
        // Valida que el nombre no esté vacío
        if (actor.getFirstName() == null || actor.getFirstName().trim().isEmpty()) {
            System.err.println("[ActorController] El nombre no puede estar vacío.");
            return false;
        }
        // Valida que el apellido no esté vacío
        if (actor.getLastName() == null || actor.getLastName().trim().isEmpty()) {
            System.err.println("[ActorController] El apellido no puede estar vacío.");
            return false;
        }

        boolean resultado = actorData.post(actor); // Inserta en BD

        if (resultado) {
            listaActores.add(actor); // Agrega a la lista en memoria
            System.out.println("[ActorController] Actor agregado: " + actor.getFirstName());
        }
        return resultado;
    }

    /**
     * actualizar — Actualiza nombre o apellido de un actor.
     */
    public boolean actualizar(Actor actor) {
        if (actor.getActorId() <= 0) { // ID debe ser válido
            System.err.println("[ActorController] ID de actor inválido.");
            return false;
        }
        return actorData.put(actor); // Delega al DAO
    }

    /**
     * eliminar — Elimina un actor por su ID.
     */
    public boolean eliminar(int actorId) {
        boolean resultado = actorData.delete(actorId); // DELETE en BD
        if (resultado) listaActores.removeIf(a -> a.getActorId() == actorId); // Limpia memoria
        return resultado;
    }

    /**
     * buscarPorId — Busca un actor por su ID, primero en memoria luego en BD.
     *
     * @param actorId  ID del actor a buscar
     * @return         Actor encontrado o null
     */
    public Actor buscarPorId(int actorId) {
        // Busca primero en la lista en memoria para evitar consulta innecesaria a la BD
        for (Actor a : listaActores) {
            if (a.getActorId() == actorId) return a; // Retorna si lo encuentra en memoria
        }
        return actorData.get(actorId); // Consulta la BD si no está en memoria
    }

    /**
     * buscarPorApellido — Busca actores por apellido.
     */
    public List<Actor> buscarPorApellido(String apellido) {
        return actorData.buscarPorApellido(apellido); // Delega al DAO
    }

    /**
     * getFilmsDeActor — Obtiene las películas de un actor.
     */
    public List<String> getFilmsDeActor(int actorId) {
        return actorData.getFilmsPorActor(actorId); // JOIN en el DAO
    }

    /** @return  Lista completa de actores */
    public List<Actor> getTodos() { return listaActores; }

    /** Recarga actores desde la BD */
    public void cargarTodos() {
        listaActores.clear();
        listaActores = actorData.getAll();
    }

    /** Cierra la conexión */
    public void cerrar() { actorData.closeConnection(); }
}
