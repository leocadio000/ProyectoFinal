// Paquete de controladores MVC
package com.sakila.controllers;

import com.sakila.data.CustomerData;
import com.sakila.models.Customer;
import java.util.ArrayList;
import java.util.List;
// Regex para validar formatos de email y teléfono (requerimiento del proyecto)
import java.util.regex.Pattern;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * CustomerController: Controlador MVC para gestión de clientes.
 * Incluye validación con expresiones regulares para email (requerimiento normas).
 */
public class CustomerController {

    // DAO de clientes — acceso a la tabla customer
    private CustomerData customerData;

    // Lista en memoria de clientes cargados
    private List<Customer> listaClientes;

    // Patrón de expresión regular para validar emails
    // Formato: algo@algo.dominio — cumple requisito de regex del proyecto
    private static final Pattern PATRON_EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Patrón de expresión regular para validar teléfonos (formato general)
    // Acepta: +1234567890, (123) 456-7890, 123-456-7890
    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^[+]?[(]?[0-9]{1,4}[)]?[-\\s./0-9]{7,15}$");

    /** Constructor: crea el DAO y carga los clientes */
    public CustomerController() {
        customerData  = new CustomerData();  // DAO con conexión BD
        listaClientes = new ArrayList<>();   // Lista vacía en memoria
        cargarTodos();                       // Carga todos los clientes desde BD
    }

    /**
     * registrar — Registra un nuevo cliente con validación de email.
     * Usa expresión regular para validar el formato del correo.
     *
     * @param cliente  Objeto Customer a insertar
     * @return         true si fue registrado correctamente
     */
    public boolean registrar(Customer cliente) {
        // Validación de nombre (no puede estar vacío)
        if (cliente.getFirstName() == null || cliente.getFirstName().trim().isEmpty()) {
            System.err.println("[CustomerController] El nombre no puede estar vacío.");
            return false;
        }

        // Validación de email usando expresión regular
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            // Verifica que el email coincida con el patrón definido arriba
            if (!PATRON_EMAIL.matcher(cliente.getEmail()).matches()) {
                System.err.println("[CustomerController] Email inválido: " + cliente.getEmail());
                return false; // Rechaza si el email no tiene formato válido
            }
        }

        boolean resultado = customerData.post(cliente); // Inserta en BD

        if (resultado) {
            listaClientes.add(cliente); // Agrega a la lista en memoria
            System.out.println("[CustomerController] Cliente registrado: " + cliente.getFirstName());
        }
        return resultado;
    }

    /**
     * actualizar — Actualiza datos del cliente con validación de email.
     *
     * @param cliente  Cliente con datos actualizados
     * @return         true si fue actualizado
     */
    public boolean actualizar(Customer cliente) {
        if (cliente.getCustomerId() <= 0) { // El ID debe ser válido
            System.err.println("[CustomerController] ID de cliente no válido.");
            return false;
        }

        // Valida el nuevo email si se está cambiando
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            if (!PATRON_EMAIL.matcher(cliente.getEmail()).matches()) {
                System.err.println("[CustomerController] Email inválido: " + cliente.getEmail());
                return false; // Rechaza si el email tiene formato incorrecto
            }
        }

        return customerData.put(cliente); // Delega la actualización al DAO
    }

    /**
     * eliminar — Elimina un cliente por su ID.
     *
     * @param customerId  ID del cliente a eliminar
     * @return            true si fue eliminado
     */
    public boolean eliminar(int customerId) {
        boolean resultado = customerData.delete(customerId); // DELETE en BD
        if (resultado) {
            listaClientes.removeIf(c -> c.getCustomerId() == customerId); // Limpia de memoria
        }
        return resultado;
    }

    /**
     * buscarPorId — Busca un cliente por ID, primero en memoria luego en BD.
     */
    public Customer buscarPorId(int id) {
        for (Customer c : listaClientes) {
            if (c.getCustomerId() == id) return c; // Retorna si está en memoria
        }
        return customerData.get(id); // Consulta BD si no está en memoria
    }

    /**
     * buscarPorEmail — Busca clientes por email (búsqueda parcial).
     */
    public List<Customer> buscarPorEmail(String email) {
        return customerData.buscarPorEmail(email); // Delega al DAO
    }

    /** @return  Lista completa de clientes en memoria */
    public List<Customer> getTodos() { return listaClientes; }

    /** Recarga todos los clientes desde la BD */
    public void cargarTodos() {
        listaClientes.clear();
        listaClientes = customerData.getAll(); // Obtiene todos desde BD
    }

    /** Cierra la conexión del DAO */
    public void cerrar() { customerData.closeConnection(); }
}
