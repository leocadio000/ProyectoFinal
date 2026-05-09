// Paquete raíz del proyecto sakila ORM
package com.sakila;

// Importamos la clase de interfaz de usuario de consola
import com.sakila.ui.ConsoleUI;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Main: Punto de entrada principal del sistema ORM sakila.
 *
 * Arquitectura del proyecto:
 *   com.sakila
 *   ├── data        → IDatapost (interfaz CRUD), DataContext (padre abstracto),
 *   │                 FilmData, ActorData, CustomerData, RentalData,
 *   │                 InventoryData, PaymentData  (DAOs concretos y finales)
 *   ├── models      → Film, Actor, Customer, Rental, Inventory, Payment, Store, Staff
 *   ├── controllers → FilmController, ActorController, CustomerController,
 *   │                 RentalController, InventoryController, PaymentController
 *   ├── reports     → ReportManager (estadísticas, CSV, JSON)
 *   ├── ui          → ConsoleUI (interfaz de consola con todos los menús)
 *   └── utils       → Validator (expresiones regulares para fechas, emails, cédulas)
 */
public class Main {

    /**
     * main — Método principal de la aplicación.
     * Crea la interfaz de consola y la inicia.
     *
     * @param args  Argumentos de línea de comandos (no utilizados en esta versión)
     */
    public static void main(String[] args) {
        // Muestra el banner de bienvenida al iniciar el sistema
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║    SAKILA ORM — Proyecto Final INF514          ║");
        System.out.println("║    Java Programming 2020                       ║");
        System.out.println("║    Universidad Autónoma de Santo Domingo       ║");
        System.out.println("║    Facultad de Ciencias — Escuela de Informática║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();

        // Crea la instancia de la interfaz de consola
        // El constructor de ConsoleUI inicializa todos los controladores
        ConsoleUI ui = new ConsoleUI();

        // Inicia el bucle principal de la aplicación
        // El programa correrá hasta que el usuario elija la opción "0 - Salir"
        ui.iniciar();
    }
}
