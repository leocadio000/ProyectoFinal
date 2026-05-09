// Paquete de interfaz de usuario del proyecto
package com.sakila.ui;

// Importamos todos los controladores del sistema MVC
import com.sakila.controllers.ActorController;
import com.sakila.controllers.CustomerController;
import com.sakila.controllers.FilmController;
import com.sakila.controllers.InventoryController;
import com.sakila.controllers.PaymentController;
import com.sakila.controllers.RentalController;

// Importamos los modelos necesarios para crear objetos desde la consola
import com.sakila.models.Actor;
import com.sakila.models.Customer;
import com.sakila.models.Film;

// Importamos el gestor de reportes
import com.sakila.reports.ReportManager;

// Importamos el validador de datos (expresiones regulares)
import com.sakila.utils.Validator;

// Scanner para leer la entrada del usuario desde la consola
import java.util.Scanner;
// BigDecimal para los valores monetarios
import java.math.BigDecimal;
// List para manejar colecciones de resultados
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * ConsoleUI: Interfaz de usuario por CONSOLA del sistema ORM sakila.
 * Implementa todos los menús del sistema con operaciones CRUD para cada entidad.
 * Patrón MVC: esta clase es la VISTA — solo muestra datos y captura entradas,
 * nunca accede directamente a la base de datos (usa los Controladores).
 *
 * Menús disponibles:
 *   1. Gestión de Películas (Film)
 *   2. Gestión de Actores (Actor)
 *   3. Gestión de Clientes (Customer)
 *   4. Gestión de Rentas (Rental)
 *   5. Gestión de Inventario (Inventory)
 *   6. Gestión de Pagos (Payment)
 *   7. Reportes y Estadísticas
 */
public class ConsoleUI {

    // -------------------------------------------------------------------------
    // ATRIBUTOS — Controladores y herramientas de la UI
    // -------------------------------------------------------------------------

    // Controladores MVC — la UI solo interactúa con estos, nunca con los DAOs
    private FilmController      filmCtrl;      // Controlador de películas
    private ActorController     actorCtrl;     // Controlador de actores
    private CustomerController  customerCtrl;  // Controlador de clientes
    private RentalController    rentalCtrl;    // Controlador de rentas
    private InventoryController inventoryCtrl; // Controlador de inventario
    private PaymentController   paymentCtrl;   // Controlador de pagos

    // Gestor de reportes y estadísticas
    private ReportManager reportManager;

    // Scanner compartido para leer toda la entrada del usuario
    private Scanner scanner;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    /**
     * Constructor: inicializa todos los controladores y el scanner de consola.
     * Al crearse, todos los controladores abren su conexión a la BD.
     */
    public ConsoleUI() {
        System.out.println("Iniciando sistema sakila ORM..."); // Mensaje de inicio

        // Inicializa cada controlador — cada uno abre su conexión a la BD
        filmCtrl      = new FilmController();      // Carga todas las películas
        actorCtrl     = new ActorController();     // Carga todos los actores
        customerCtrl  = new CustomerController();  // Carga todos los clientes
        rentalCtrl    = new RentalController();    // Inicializa sin pre-cargar rentas
        inventoryCtrl = new InventoryController(); // Inicializa el inventario
        paymentCtrl   = new PaymentController();   // Inicializa pagos

        reportManager = new ReportManager(); // Inicializa el gestor de reportes

        // Scanner System.in lee la entrada estándar (teclado del usuario)
        scanner = new Scanner(System.in);

        System.out.println("¡Sistema listo! Conectado a sakila DB.\n");
    }

    // =========================================================================
    // MÉTODO PRINCIPAL — Inicia y controla el flujo del programa
    // =========================================================================

    /**
     * iniciar — Arranca la aplicación mostrando el menú principal.
     * Bucle principal que se ejecuta hasta que el usuario elige salir.
     */
    public void iniciar() {
        int opcion = 0; // Almacena la opción elegida por el usuario

        // Bucle principal — continúa hasta que el usuario elija la opción 0 (salir)
        do {
            mostrarMenuPrincipal(); // Muestra las opciones del menú principal

            opcion = leerEntero("Seleccione una opción: "); // Lee la opción del usuario

            // Estructura switch para dirigir al submenú correspondiente
            switch (opcion) {
                case 1: menuFilms();      break; // Va al menú de películas
                case 2: menuActores();    break; // Va al menú de actores
                case 3: menuClientes();   break; // Va al menú de clientes
                case 4: menuRentas();     break; // Va al menú de rentas
                case 5: menuInventario(); break; // Va al menú de inventario
                case 6: menuPagos();      break; // Va al menú de pagos
                case 7: menuReportes();   break; // Va al menú de reportes
                case 0:
                    System.out.println("\n¡Hasta luego! Cerrando conexiones...");
                    cerrarTodo(); // Cierra todas las conexiones antes de salir
                    break;
                default:
                    // Opción fuera de rango — informa al usuario
                    System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 0); // Repite el menú hasta que el usuario elija salir
    }

    // =========================================================================
    // MENÚ PRINCIPAL
    // =========================================================================

    /**
     * mostrarMenuPrincipal — Imprime el menú principal en la consola.
     * Muestra todas las opciones disponibles con un formato claro.
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║    SAKILA ORM - SISTEMA DE RENTAS    ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Gestión de Películas             ║"); // Módulo Film
        System.out.println("║  2. Gestión de Actores               ║"); // Módulo Actor
        System.out.println("║  3. Gestión de Clientes              ║"); // Módulo Customer
        System.out.println("║  4. Gestión de Rentas                ║"); // Módulo Rental
        System.out.println("║  5. Gestión de Inventario            ║"); // Módulo Inventory
        System.out.println("║  6. Gestión de Pagos                 ║"); // Módulo Payment
        System.out.println("║  7. Reportes y Estadísticas          ║"); // Módulo Reports
        System.out.println("║  0. Salir                            ║"); // Salir del sistema
        System.out.println("╚══════════════════════════════════════╝");
    }

    // =========================================================================
    // MENÚ DE PELÍCULAS (FILM)
    // =========================================================================

    /**
     * menuFilms — Submenú completo de gestión de películas.
     * Permite listar, buscar, agregar, editar y eliminar películas.
     */
    private void menuFilms() {
        int opcion = -1; // Inicializa la opción para entrar al bucle

        while (opcion != 0) { // Repite el submenú hasta que el usuario elija volver
            // Muestra el submenú de películas
            System.out.println("\n--- GESTIÓN DE PELÍCULAS ---");
            System.out.println("  1. Listar todas las películas");
            System.out.println("  2. Buscar película por ID");
            System.out.println("  3. Buscar película por título");
            System.out.println("  4. Buscar películas por rating");
            System.out.println("  5. Agregar nueva película");
            System.out.println("  6. Editar película");
            System.out.println("  7. Eliminar película");
            System.out.println("  8. Ver actores de una película");
            System.out.println("  0. Volver al menú principal");

            opcion = leerEntero("Opción: "); // Lee la selección del usuario

            switch (opcion) {
                case 1: listarFilms();        break; // Muestra todo el catálogo
                case 2: buscarFilmPorId();    break; // Búsqueda por PK
                case 3: buscarFilmPorTitulo();break; // Búsqueda parcial por título
                case 4: buscarFilmPorRating();break; // Filtro por clasificación
                case 5: agregarFilm();        break; // Formulario de nueva película
                case 6: editarFilm();         break; // Formulario de edición
                case 7: eliminarFilm();       break; // Confirmación y eliminación
                case 8: verActoresDeFilm();   break; // Consulta de actores del film
                case 0: break; // Vuelve al menú principal
                default: System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * listarFilms — Lista todas las películas del catálogo con paginación simple.
     * Muestra máximo 20 películas para no saturar la consola.
     */
    private void listarFilms() {
        System.out.println("\n=== CATÁLOGO DE PELÍCULAS ===");

        // Obtiene la lista completa desde el controlador (en memoria)
        List<Film> films = filmCtrl.getTodos();

        if (films.isEmpty()) { // Verifica si hay películas cargadas
            System.out.println("No hay películas en el catálogo.");
            return; // Sale si no hay datos
        }

        // Muestra el encabezado de la tabla
        System.out.printf("%-6s %-36s %-6s %-7s %-8s %-5s%n",
                "ID", "TÍTULO", "AÑO", "RATING", "PRECIO", "MIN");
        System.out.println("─".repeat(72)); // Línea separadora

        // Limita la visualización a 20 películas para no saturar la pantalla
        int limite = Math.min(films.size(), 20); // Máximo 20 registros en pantalla
        for (int i = 0; i < limite; i++) {
            System.out.println(films.get(i)); // Usa el toString() de Film
        }

        // Informa cuántas películas hay en total y cuántas se muestran
        System.out.printf("Mostrando %d de %d películas.%n", limite, films.size());
    }

    /**
     * buscarFilmPorId — Busca y muestra una película por su ID.
     */
    private void buscarFilmPorId() {
        int id = leerEntero("Ingrese el ID de la película: "); // Lee el ID a buscar
        Film film = filmCtrl.buscarPorId(id); // Busca en el controlador

        if (film != null) {
            System.out.println("\n--- Película encontrada ---");
            System.out.println("ID          : " + film.getFilmId());       // Muestra la PK
            System.out.println("Título      : " + film.getTitle());        // Muestra el título
            System.out.println("Descripción : " + film.getDescription());  // Muestra la sinopsis
            System.out.println("Año         : " + film.getReleaseYear());  // Muestra el año
            System.out.println("Rating      : " + film.getRating());       // Muestra la clasificación
            System.out.println("Precio/día  : $" + film.getRentalRate());  // Muestra el precio
            System.out.println("Duración    : " + film.getLength() + " min"); // Muestra la duración
        } else {
            System.out.println("No se encontró película con ID: " + id); // No existe
        }
    }

    /**
     * buscarFilmPorTitulo — Busca películas por texto parcial en el título.
     */
    private void buscarFilmPorTitulo() {
        String titulo = leerTexto("Ingrese el título a buscar: "); // Lee el texto de búsqueda
        List<Film> resultados = filmCtrl.buscarPorTitulo(titulo);  // Busca en el controlador

        System.out.println("\n--- Resultados para: \"" + titulo + "\" ---");

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron películas con ese título."); // Sin resultados
        } else {
            // Muestra cada película encontrada
            resultados.forEach(System.out::println); // Usa el toString() de cada Film
            System.out.println("Total encontradas: " + resultados.size()); // Cantidad total
        }
    }

    /**
     * buscarFilmPorRating — Filtra películas por clasificación de edad.
     */
    private void buscarFilmPorRating() {
        // Muestra los ratings disponibles para que el usuario sepa qué ingresar
        System.out.println("Ratings válidos: G | PG | PG-13 | R | NC-17");
        String rating = leerTexto("Ingrese el rating: ").toUpperCase(); // Convierte a mayúsculas

        List<Film> resultados = filmCtrl.buscarPorRating(rating); // Filtra en el controlador

        System.out.println("\n--- Películas con rating " + rating + " ---");

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron películas con rating: " + rating);
        } else {
            resultados.forEach(System.out::println); // Muestra cada resultado
            System.out.println("Total: " + resultados.size()); // Cantidad encontrada
        }
    }

    /**
     * agregarFilm — Formulario para agregar una nueva película al catálogo.
     * Captura todos los campos necesarios con validación básica.
     */
    private void agregarFilm() {
        System.out.println("\n--- NUEVA PELÍCULA ---");

        Film film = new Film(); // Crea el objeto Film vacío para llenarlo

        // Captura el título (campo obligatorio)
        film.setTitle(leerTexto("Título         : "));

        // Captura la descripción (campo opcional)
        film.setDescription(leerTexto("Descripción    : "));

        // Captura el año de estreno como número
        film.setReleaseYear(leerEntero("Año de estreno : "));

        // El languageId 1 = Inglés en la BD sakila (valor por defecto)
        film.setLanguageId(leerEntero("ID Idioma (1=EN): "));

        // Captura la duración del período de renta
        film.setRentalDuration(leerEntero("Días de renta  : "));

        // Captura el precio de renta y lo convierte a BigDecimal
        film.setRentalRate(leerDecimal("Precio de renta: $"));

        // Captura la duración de la película en minutos
        film.setLength(leerEntero("Duración (min) : "));

        // Captura el costo de reposición como BigDecimal
        film.setReplacementCost(leerDecimal("Costo reposición: $"));

        // Captura la clasificación de edad
        System.out.println("Rating: G / PG / PG-13 / R / NC-17");
        film.setRating(leerTexto("Rating         : ").toUpperCase()); // Convierte a mayúsculas

        // Intenta agregar la película a través del controlador (con validación)
        if (filmCtrl.agregar(film)) {
            System.out.println("✓ Película agregada exitosamente: " + film.getTitle());
        } else {
            System.out.println("✗ Error al agregar la película. Verifique los datos.");
        }
    }

    /**
     * editarFilm — Formulario para editar los datos de una película existente.
     * Primero busca la película y luego permite modificar sus campos.
     */
    private void editarFilm() {
        int id = leerEntero("Ingrese el ID de la película a editar: "); // Lee el ID
        Film film = filmCtrl.buscarPorId(id); // Busca la película existente

        if (film == null) { // Si no existe, informa y sale
            System.out.println("No se encontró película con ID: " + id);
            return;
        }

        // Muestra los datos actuales para que el usuario sepa qué hay
        System.out.println("Datos actuales -> " + film);

        System.out.println("\nIngrese los nuevos datos (ENTER para mantener el actual):");

        // Captura nuevo título (si el usuario presiona ENTER, mantiene el actual)
        String nuevoTitulo = leerTextoOpcional("Nuevo título [" + film.getTitle() + "]: ");
        if (!nuevoTitulo.isEmpty()) film.setTitle(nuevoTitulo); // Actualiza solo si cambió

        // Captura nuevo año si se ingresa un valor
        String anioStr = leerTextoOpcional("Nuevo año [" + film.getReleaseYear() + "]: ");
        if (!anioStr.isEmpty()) film.setReleaseYear(Integer.parseInt(anioStr)); // Parsea si hay dato

        // Captura nuevo precio de renta
        String precioStr = leerTextoOpcional("Nuevo precio [$" + film.getRentalRate() + "]: ");
        if (!precioStr.isEmpty()) film.setRentalRate(new BigDecimal(precioStr)); // Parsea si hay dato

        // Captura nuevo rating
        String nuevoRating = leerTextoOpcional("Nuevo rating [" + film.getRating() + "]: ");
        if (!nuevoRating.isEmpty()) film.setRating(nuevoRating.toUpperCase()); // Actualiza si cambió

        // Actualiza en la BD a través del controlador
        if (filmCtrl.actualizar(film)) {
            System.out.println("✓ Película actualizada: " + film.getTitle());
        } else {
            System.out.println("✗ Error al actualizar la película.");
        }
    }

    /**
     * eliminarFilm — Pide confirmación y elimina una película por su ID.
     */
    private void eliminarFilm() {
        int id = leerEntero("Ingrese el ID de la película a eliminar: "); // Lee el ID
        Film film = filmCtrl.buscarPorId(id); // Verifica que existe

        if (film == null) {
            System.out.println("No se encontró película con ID: " + id);
            return;
        }

        // Muestra la película encontrada y pide confirmación antes de eliminar
        System.out.println("Película a eliminar: " + film);
        String confirm = leerTexto("¿Confirma la eliminación? (s/n): "); // Pide confirmación

        if (confirm.equalsIgnoreCase("s")) { // Solo elimina si el usuario confirma con "s"
            if (filmCtrl.eliminar(id)) {
                System.out.println("✓ Película eliminada correctamente.");
            } else {
                System.out.println("✗ Error al eliminar. ¿Tiene rentas activas?");
            }
        } else {
            System.out.println("Eliminación cancelada."); // El usuario canceló
        }
    }

    /**
     * verActoresDeFilm — Muestra los actores que participan en una película.
     */
    private void verActoresDeFilm() {
        int filmId = leerEntero("Ingrese el ID de la película: "); // Lee el ID
        Film film  = filmCtrl.buscarPorId(filmId); // Busca la película

        if (film == null) {
            System.out.println("No se encontró película con ID: " + filmId);
            return;
        }

        // Obtiene la lista de actores del film a través del controlador
        List<String> actores = filmCtrl.getActoresDePelicula(filmId);

        System.out.println("\n--- Actores de: " + film.getTitle() + " ---");

        if (actores.isEmpty()) {
            System.out.println("No se encontraron actores registrados.");
        } else {
            // Muestra cada actor numerado con formato ordenado
            for (int i = 0; i < actores.size(); i++) {
                System.out.printf("  %2d. %s%n", i + 1, actores.get(i)); // Numerado y alineado
            }
            System.out.println("Total de actores: " + actores.size()); // Total al final
        }
    }

    // =========================================================================
    // MENÚ DE ACTORES
    // =========================================================================

    /**
     * menuActores — Submenú completo de gestión de actores.
     */
    private void menuActores() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE ACTORES ---");
            System.out.println("  1. Listar todos los actores");
            System.out.println("  2. Buscar actor por ID");
            System.out.println("  3. Buscar actor por apellido");
            System.out.println("  4. Agregar nuevo actor");
            System.out.println("  5. Editar actor");
            System.out.println("  6. Eliminar actor");
            System.out.println("  7. Ver películas de un actor");
            System.out.println("  0. Volver");

            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1: // Lista todos los actores
                    System.out.println("\n=== LISTADO DE ACTORES ===");
                    List<com.sakila.models.Actor> actores = actorCtrl.getTodos(); // Obtiene la lista
                    int limA = Math.min(actores.size(), 30); // Máximo 30 en pantalla
                    for (int i = 0; i < limA; i++) System.out.println(actores.get(i)); // Imprime
                    System.out.printf("Mostrando %d de %d actores.%n", limA, actores.size());
                    break;

                case 2: // Busca actor por ID
                    int actId = leerEntero("ID del actor: ");
                    com.sakila.models.Actor a = actorCtrl.buscarPorId(actId); // Busca en controlador
                    System.out.println(a != null ? a : "Actor no encontrado con ID: " + actId);
                    break;

                case 3: // Busca por apellido con búsqueda parcial
                    String ap = leerTexto("Apellido a buscar: ");
                    List<com.sakila.models.Actor> porAp = actorCtrl.buscarPorApellido(ap);
                    porAp.forEach(System.out::println); // Imprime cada resultado
                    System.out.println("Total: " + porAp.size());
                    break;

                case 4: // Agrega nuevo actor
                    System.out.println("\n--- NUEVO ACTOR ---");
                    String fn = leerTexto("Nombre   : ");
                    String ln = leerTexto("Apellido : ");
                    // Valida el nombre con el Validator de expresiones regulares
                    if (!Validator.validarNombre(fn) || !Validator.validarNombre(ln)) {
                        System.out.println("✗ Nombre o apellido inválido (solo letras y espacios).");
                        break; // Sale si la validación falla
                    }
                    Actor nuevoActor = new Actor(fn, ln); // Crea el objeto Actor
                    System.out.println(actorCtrl.agregar(nuevoActor) ?
                            "✓ Actor agregado: " + fn + " " + ln :
                            "✗ Error al agregar actor.");
                    break;

                case 5: // Edita actor existente
                    int eidA = leerEntero("ID del actor a editar: ");
                    com.sakila.models.Actor editA = actorCtrl.buscarPorId(eidA);
                    if (editA == null) { System.out.println("Actor no encontrado."); break; }
                    String nfn = leerTextoOpcional("Nuevo nombre [" + editA.getFirstName() + "]: ");
                    String nln = leerTextoOpcional("Nuevo apellido [" + editA.getLastName() + "]: ");
                    if (!nfn.isEmpty()) editA.setFirstName(nfn); // Actualiza si se ingresó dato
                    if (!nln.isEmpty()) editA.setLastName(nln);  // Actualiza si se ingresó dato
                    System.out.println(actorCtrl.actualizar(editA) ? "✓ Actor actualizado." : "✗ Error.");
                    break;

                case 6: // Elimina actor
                    int delA = leerEntero("ID del actor a eliminar: ");
                    String confA = leerTexto("¿Confirmar eliminación? (s/n): ");
                    if (confA.equalsIgnoreCase("s")) { // Solo elimina si confirma
                        System.out.println(actorCtrl.eliminar(delA) ?
                                "✓ Actor eliminado." : "✗ Error al eliminar.");
                    }
                    break;

                case 7: // Muestra películas de un actor
                    int actFilmId = leerEntero("ID del actor: ");
                    List<String> peliculas = actorCtrl.getFilmsDeActor(actFilmId); // Obtiene películas
                    System.out.println("\n--- Películas del actor ---");
                    if (peliculas.isEmpty()) { System.out.println("Sin películas registradas."); break; }
                    peliculas.forEach(t -> System.out.println("  • " + t)); // Muestra con viñeta
                    System.out.println("Total: " + peliculas.size());
                    break;

                case 0: break; // Vuelve al menú principal
                default: System.out.println("Opción no válida.");
            }
        }
    }

    // =========================================================================
    // MENÚ DE CLIENTES
    // =========================================================================

    /**
     * menuClientes — Submenú completo de gestión de clientes.
     */
    private void menuClientes() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE CLIENTES ---");
            System.out.println("  1. Listar todos los clientes");
            System.out.println("  2. Buscar cliente por ID");
            System.out.println("  3. Buscar cliente por email");
            System.out.println("  4. Registrar nuevo cliente");
            System.out.println("  5. Editar cliente");
            System.out.println("  6. Dar de baja cliente (eliminar)");
            System.out.println("  0. Volver");

            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1: // Lista clientes con límite de pantalla
                    System.out.println("\n=== LISTADO DE CLIENTES ===");
                    List<Customer> clientes = customerCtrl.getTodos();
                    int limC = Math.min(clientes.size(), 20); // Máximo 20 en pantalla
                    for (int i = 0; i < limC; i++) System.out.println(clientes.get(i));
                    System.out.printf("Mostrando %d de %d clientes.%n", limC, clientes.size());
                    break;

                case 2: // Busca por ID
                    int cid = leerEntero("ID del cliente: ");
                    Customer c = customerCtrl.buscarPorId(cid);
                    System.out.println(c != null ? c : "Cliente no encontrado con ID: " + cid);
                    break;

                case 3: // Busca por email con validación regex
                    String email = leerTexto("Email a buscar: ");
                    List<Customer> porEmail = customerCtrl.buscarPorEmail(email);
                    porEmail.forEach(System.out::println);
                    System.out.println("Total: " + porEmail.size());
                    break;

                case 4: // Registra nuevo cliente con validación de email
                    System.out.println("\n--- NUEVO CLIENTE ---");
                    String cfn = leerTexto("Nombre     : ");
                    String cln = leerTexto("Apellido   : ");
                    String cem = leerTexto("Email      : ");

                    // Valida el email con expresión regular antes de crear el cliente
                    if (!Validator.validarEmail(cem)) {
                        System.out.println("✗ Email inválido: " + cem + " — use formato usuario@dominio.ext");
                        break; // No continúa si el email no es válido
                    }

                    int storeId   = leerEntero("ID Tienda  : "); // FK de tienda
                    int addressId = leerEntero("ID Dirección: "); // FK de dirección

                    // Crea el objeto Customer con los datos ingresados
                    Customer nuevoCliente = new Customer(storeId, cfn, cln, cem, addressId, true);

                    System.out.println(customerCtrl.registrar(nuevoCliente) ?
                            "✓ Cliente registrado: " + cfn + " " + cln :
                            "✗ Error al registrar el cliente.");
                    break;

                case 5: // Edita cliente existente
                    int editCid = leerEntero("ID del cliente a editar: ");
                    Customer editC = customerCtrl.buscarPorId(editCid);
                    if (editC == null) { System.out.println("Cliente no encontrado."); break; }
                    System.out.println("Datos actuales: " + editC); // Muestra datos actuales
                    String nuevofn = leerTextoOpcional("Nuevo nombre [" + editC.getFirstName() + "]: ");
                    String nuevoln = leerTextoOpcional("Nuevo apellido [" + editC.getLastName() + "]: ");
                    String nuevoem = leerTextoOpcional("Nuevo email [" + editC.getEmail() + "]: ");
                    if (!nuevofn.isEmpty()) editC.setFirstName(nuevofn);
                    if (!nuevoln.isEmpty()) editC.setLastName(nuevoln);
                    if (!nuevoem.isEmpty()) {
                        // Valida el nuevo email con regex antes de actualizar
                        if (!Validator.validarEmail(nuevoem)) {
                            System.out.println("✗ Email inválido. No se actualizó el email.");
                        } else {
                            editC.setEmail(nuevoem); // Actualiza solo si el email es válido
                        }
                    }
                    System.out.println(customerCtrl.actualizar(editC) ?
                            "✓ Cliente actualizado." : "✗ Error al actualizar.");
                    break;

                case 6: // Da de baja al cliente
                    int delCid = leerEntero("ID del cliente a eliminar: ");
                    String confC = leerTexto("¿Confirmar baja? (s/n): ");
                    if (confC.equalsIgnoreCase("s")) {
                        System.out.println(customerCtrl.eliminar(delCid) ?
                                "✓ Cliente dado de baja." : "✗ Error. ¿Tiene rentas activas?");
                    }
                    break;

                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    // =========================================================================
    // MENÚ DE RENTAS
    // =========================================================================

    /**
     * menuRentas — Submenú para gestión de rentas de películas.
     */
    private void menuRentas() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE RENTAS ---");
            System.out.println("  1. Registrar nueva renta");
            System.out.println("  2. Registrar devolución");
            System.out.println("  3. Ver rentas de un cliente");
            System.out.println("  4. Ver rentas pendientes (no devueltas)");
            System.out.println("  0. Volver");

            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1: // Registra una nueva renta de película
                    System.out.println("\n--- NUEVA RENTA ---");
                    int invId  = leerEntero("ID Inventario (ítem): "); // FK de inventario
                    int custId = leerEntero("ID Cliente          : "); // FK de cliente
                    int stfId  = leerEntero("ID Empleado         : "); // FK de empleado
                    System.out.println(rentalCtrl.registrarRenta(invId, custId, stfId) ?
                            "✓ Renta registrada exitosamente." :
                            "✗ Error al registrar la renta. Verifique los IDs.");
                    break;

                case 2: // Registra la devolución de una película
                    int rentalId = leerEntero("ID de la renta a devolver: ");
                    System.out.println(rentalCtrl.registrarDevolucion(rentalId) ?
                            "✓ Devolución registrada correctamente." :
                            "✗ Error al registrar la devolución.");
                    break;

                case 3: // Ver historial de rentas de un cliente
                    int cidR = leerEntero("ID del cliente: ");
                    List<com.sakila.models.Rental> rentas = rentalCtrl.getRentasPorCliente(cidR);
                    System.out.println("\n--- Rentas del cliente #" + cidR + " ---");
                    if (rentas.isEmpty()) {
                        System.out.println("Sin rentas registradas para este cliente.");
                    } else {
                        rentas.forEach(System.out::println); // Muestra cada renta
                        System.out.println("Total: " + rentas.size()); // Cantidad total
                    }
                    break;

                case 4: // Ver rentas sin fecha de devolución (pendientes)
                    List<com.sakila.models.Rental> pendientes = rentalCtrl.getRentasPendientes();
                    System.out.println("\n--- RENTAS PENDIENTES DE DEVOLUCIÓN ---");
                    System.out.println("Total pendientes: " + pendientes.size()); // Total primero
                    int limR = Math.min(pendientes.size(), 20); // Máximo 20 en pantalla
                    for (int i = 0; i < limR; i++) System.out.println(pendientes.get(i));
                    break;

                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    // =========================================================================
    // MENÚ DE INVENTARIO
    // =========================================================================

    /**
     * menuInventario — Submenú de gestión del inventario de películas.
     */
    private void menuInventario() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE INVENTARIO ---");
            System.out.println("  1. Ver inventario por tienda");
            System.out.println("  2. Ver copias de una película");
            System.out.println("  3. Agregar copia al inventario");
            System.out.println("  4. Eliminar copia del inventario");
            System.out.println("  0. Volver");

            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1: // Lista inventario de una tienda
                    int storeId = leerEntero("ID de la tienda (1 o 2): ");
                    List<com.sakila.models.Inventory> invTienda =
                            inventoryCtrl.getInventarioPorTienda(storeId);
                    System.out.println("\n--- Inventario Tienda #" + storeId + " ---");
                    System.out.println("Total de ítems: " + invTienda.size()); // Total primero
                    int limI = Math.min(invTienda.size(), 20); // Máximo 20
                    for (int i = 0; i < limI; i++) System.out.println(invTienda.get(i));
                    break;

                case 2: // Lista copias de una película
                    int filmId = leerEntero("ID de la película: ");
                    List<com.sakila.models.Inventory> copias =
                            inventoryCtrl.getCopiasDisponibles(filmId);
                    System.out.println("\n--- Copias de película #" + filmId + " ---");
                    if (copias.isEmpty()) {
                        System.out.println("No hay copias en inventario para esta película.");
                    } else {
                        copias.forEach(System.out::println); // Muestra cada copia
                        System.out.println("Total copias: " + copias.size());
                    }
                    break;

                case 3: // Agrega nueva copia al inventario
                    int fid = leerEntero("ID de la película: ");
                    int sid = leerEntero("ID de la tienda  : ");
                    System.out.println(inventoryCtrl.agregarCopia(fid, sid) ?
                            "✓ Copia agregada al inventario." :
                            "✗ Error al agregar copia. Verifique los IDs.");
                    break;

                case 4: // Elimina una copia del inventario
                    int invDelId = leerEntero("ID del ítem a eliminar: ");
                    String confInv = leerTexto("¿Confirmar eliminación? (s/n): ");
                    if (confInv.equalsIgnoreCase("s")) {
                        System.out.println(inventoryCtrl.eliminarCopia(invDelId) ?
                                "✓ Copia eliminada del inventario." :
                                "✗ Error. ¿Tiene rentas activas?");
                    }
                    break;

                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    // =========================================================================
    // MENÚ DE PAGOS
    // =========================================================================

    /**
     * menuPagos — Submenú de gestión de pagos y cobranzas.
     */
    private void menuPagos() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- GESTIÓN DE PAGOS ---");
            System.out.println("  1. Registrar nuevo pago");
            System.out.println("  2. Buscar pago por ID");
            System.out.println("  3. Ver pagos de un cliente");
            System.out.println("  4. Total pagado por un cliente");
            System.out.println("  5. Anular pago");
            System.out.println("  0. Volver");

            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1: // Registra nuevo pago
                    System.out.println("\n--- NUEVO PAGO ---");
                    int pcid  = leerEntero("ID Cliente  : "); // FK cliente
                    int psid  = leerEntero("ID Empleado : "); // FK empleado
                    int prid  = leerEntero("ID Renta    : "); // FK renta
                    BigDecimal monto = leerDecimal("Monto $     : "); // Monto del pago
                    System.out.println(paymentCtrl.registrarPago(pcid, psid, prid, monto) ?
                            "✓ Pago registrado: $" + monto :
                            "✗ Error al registrar el pago.");
                    break;

                case 2: // Busca pago por ID
                    int payId = leerEntero("ID del pago: ");
                    com.sakila.models.Payment pay = paymentCtrl.buscarPago(payId);
                    System.out.println(pay != null ? pay : "Pago no encontrado con ID: " + payId);
                    break;

                case 3: // Ver historial de pagos de un cliente
                    int histCid = leerEntero("ID del cliente: ");
                    List<com.sakila.models.Payment> pagos =
                            paymentCtrl.getPagosPorCliente(histCid);
                    System.out.println("\n--- Pagos del cliente #" + histCid + " ---");
                    if (pagos == null || pagos.isEmpty()) {
                        System.out.println("Sin pagos registrados para este cliente.");
                    } else {
                        pagos.forEach(System.out::println);
                        System.out.println("Total de transacciones: " + pagos.size());
                    }
                    break;

                case 4: // Calcula total acumulado de pagos de un cliente
                    int totCid = leerEntero("ID del cliente: ");
                    BigDecimal total = paymentCtrl.getTotalPagado(totCid); // Suma todos sus pagos
                    System.out.printf("Total pagado por cliente #%d: $%.2f%n", totCid, total);
                    break;

                case 5: // Anula un pago
                    int anulId = leerEntero("ID del pago a anular: ");
                    String confP = leerTexto("¿Confirmar anulación? (s/n): ");
                    if (confP.equalsIgnoreCase("s")) {
                        System.out.println(paymentCtrl.anularPago(anulId) ?
                                "✓ Pago anulado." : "✗ Error al anular el pago.");
                    }
                    break;

                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    // =========================================================================
    // MENÚ DE REPORTES Y ESTADÍSTICAS
    // =========================================================================

    /**
     * menuReportes — Submenú con todos los reportes y exportaciones del sistema.
     */
    private void menuReportes() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- REPORTES Y ESTADÍSTICAS ---");
            System.out.println("  1.  Estadísticas de películas en inventario");
            System.out.println("  2.  Actores por película");
            System.out.println("  3.  Rentas pendientes (Aging cuentas x cobrar)");
            System.out.println("  4.  Rentas por tienda");
            System.out.println("  5.  Cobranzas por tienda");
            System.out.println("  6.  Estadísticas generales de pagos");
            System.out.println("  7.  Exportar películas a CSV");
            System.out.println("  8.  Exportar películas a JSON");
            System.out.println("  9.  Estadísticas de películas del controlador");
            System.out.println("  0.  Volver");

            opcion = leerEntero("Opción: ");

            switch (opcion) {
                case 1: // Reporte de inventario de películas
                    reportManager.reporteInventario(); // Muestra estadísticas de catálogo
                    break;

                case 2: // Actores por película específica
                    int rfid = leerEntero("ID de la película: ");
                    Film rf = filmCtrl.buscarPorId(rfid); // Obtiene el título para mostrar
                    String titulo = rf != null ? rf.getTitle() : "Film #" + rfid;
                    reportManager.reporteActoresPorFilm(rfid, titulo); // Muestra actores
                    break;

                case 3: // Aging de cuentas por cobrar (rentas no devueltas)
                    reportManager.reporteAging(); // Agrupa por días de atraso
                    break;

                case 4: // Total de rentas agrupadas por tienda
                    reportManager.reporteRentasPorTienda(); // Muestra conteo por tienda
                    break;

                case 5: // Ingresos agrupados por tienda
                    reportManager.reportePagosPorTienda(); // Muestra monto recaudado
                    break;

                case 6: // Estadísticas financieras generales
                    reportManager.reporteEstadisticasPagos(); // Total, promedio, max, min
                    break;

                case 7: // Exporta catálogo a CSV
                    String csvFile = leerTexto("Nombre del archivo CSV (ej: films.csv): ");
                    if (!csvFile.endsWith(".csv")) csvFile += ".csv"; // Agrega extensión si falta
                    reportManager.exportarFilmsCSV(csvFile); // Genera el archivo CSV
                    break;

                case 8: // Exporta catálogo a JSON
                    String jsonFile = leerTexto("Nombre del archivo JSON (ej: films.json): ");
                    if (!jsonFile.endsWith(".json")) jsonFile += ".json"; // Agrega extensión si falta
                    reportManager.exportarFilmsJSON(jsonFile); // Genera el archivo JSON
                    break;

                case 9: // Estadísticas del controlador usando HashMap
                    java.util.HashMap<String, Object> stats = filmCtrl.getEstadisticas();
                    System.out.println("\n=== ESTADÍSTICAS CATÁLOGO (HashMap) ===");
                    // Itera el HashMap y muestra cada métrica
                    stats.forEach((k, v) -> System.out.printf("  %-22s: %s%n", k, v));
                    break;

                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    // =========================================================================
    // MÉTODOS AUXILIARES DE LECTURA DE CONSOLA
    // Centralizan la lectura de input del usuario con manejo de errores
    // =========================================================================

    /**
     * leerEntero — Lee un número entero de la consola con manejo de error.
     * Si el usuario ingresa texto en lugar de número, pide que intente de nuevo.
     *
     * @param mensaje  Texto que se muestra antes de esperar el input
     * @return         Número entero ingresado por el usuario
     */
    private int leerEntero(String mensaje) {
        while (true) { // Repite hasta que el usuario ingrese un número válido
            System.out.print(mensaje); // Muestra el mensaje de solicitud
            try {
                String input = scanner.nextLine().trim(); // Lee la línea completa y elimina espacios
                return Integer.parseInt(input); // Convierte a entero — lanza excepción si no es número
            } catch (NumberFormatException e) {
                // Se ejecuta si el usuario ingresó texto en lugar de número
                System.out.println("  Por favor ingrese un número entero válido.");
            }
        }
    }

    /**
     * leerDecimal — Lee un número decimal (BigDecimal) de la consola.
     * Usado para capturar montos de dinero con precisión exacta.
     *
     * @param mensaje  Texto que se muestra antes de esperar el input
     * @return         BigDecimal ingresado por el usuario
     */
    private BigDecimal leerDecimal(String mensaje) {
        while (true) { // Repite hasta obtener un número decimal válido
            System.out.print(mensaje);
            try {
                String input = scanner.nextLine().trim(); // Lee y limpia espacios
                return new BigDecimal(input); // Crea BigDecimal — lanza excepción si es inválido
            } catch (NumberFormatException e) {
                System.out.println("  Por favor ingrese un valor numérico válido (ej: 4.99).");
            }
        }
    }

    /**
     * leerTexto — Lee una línea de texto de la consola.
     * El campo no puede estar vacío — pide de nuevo si está en blanco.
     *
     * @param mensaje  Texto que se muestra antes de esperar el input
     * @return         Texto ingresado (nunca vacío ni null)
     */
    private String leerTexto(String mensaje) {
        String input = ""; // Inicializa vacío para entrar al bucle
        while (input.isEmpty()) { // Repite hasta que el usuario ingrese algo
            System.out.print(mensaje);
            input = scanner.nextLine().trim(); // Lee y elimina espacios al inicio/final
            if (input.isEmpty()) {
                System.out.println("  Este campo no puede estar vacío."); // Informa si está vacío
            }
        }
        return input; // Retorna el texto ingresado
    }

    /**
     * leerTextoOpcional — Lee una línea de texto que puede estar vacía.
     * Usado para campos opcionales donde el usuario puede presionar ENTER.
     *
     * @param mensaje  Texto que se muestra antes de esperar el input
     * @return         Texto ingresado (puede ser vacío si el usuario presionó ENTER)
     */
    private String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje); // Muestra el mensaje
        return scanner.nextLine().trim(); // Lee y retorna aunque esté vacío
    }

    // =========================================================================
    // CIERRE DEL SISTEMA
    // =========================================================================

    /**
     * cerrarTodo — Cierra todas las conexiones de los controladores y reportes.
     * Debe llamarse antes de salir del programa para liberar recursos MySQL.
     */
    private void cerrarTodo() {
        filmCtrl.cerrar();       // Cierra conexión del controlador de películas
        actorCtrl.cerrar();      // Cierra conexión del controlador de actores
        customerCtrl.cerrar();   // Cierra conexión del controlador de clientes
        rentalCtrl.cerrar();     // Cierra conexión del controlador de rentas
        inventoryCtrl.cerrar();  // Cierra conexión del controlador de inventario
        paymentCtrl.cerrar();    // Cierra conexión del controlador de pagos
        reportManager.cerrar();  // Cierra todas las conexiones del gestor de reportes
        scanner.close();         // Cierra el Scanner para liberar System.in
        System.out.println("Sistema cerrado correctamente. ¡Hasta pronto!");
    }
}
