╔══════════════════════════════════════════════════════════════════╗
║        SAKILA ORM — Proyecto Final INF514                       ║
║        Java Programming 2020                                    ║
║        Universidad Autónoma de Santo Domingo                    ║
╚══════════════════════════════════════════════════════════════════╝

DESCRIPCIÓN
───────────
Sistema ORM (Object-Relational Mapping) sobre la base de datos
sakila de MySQL. Implementa el patrón MVC con acceso a datos
mediante clases DAO concretas y una interfaz de usuario por consola.

link del video de la explicación https://youtu.be/MyIi3tTrKX4

════════════════════════════════════════════════════════════════════
 ESTRUCTURA DEL PROYECTO
════════════════════════════════════════════════════════════════════

SakilaORM/
├── lib/
│   └── mysql-connector-java-8.0.28.jar   ← DEBES COLOCAR ESTE JAR
├── src/
│   └── com/sakila/
│       ├── Main.java                      ← PUNTO DE ENTRADA
│       ├── data/
│       │   ├── IDatapost.java             ← Interfaz CRUD genérica
│       │   ├── DataContext.java           ← Padre abstracto híbrido
│       │   ├── FilmData.java              ← DAO de películas (final)
│       │   ├── ActorData.java             ← DAO de actores (final)
│       │   ├── CustomerData.java          ← DAO de clientes (final)
│       │   ├── RentalData.java            ← DAO de rentas (final)
│       │   ├── InventoryData.java         ← DAO de inventario (final)
│       │   └── PaymentData.java           ← DAO de pagos (final)
│       ├── models/
│       │   ├── Film.java                  ← Modelo tabla film
│       │   ├── Actor.java                 ← Modelo tabla actor
│       │   ├── Customer.java              ← Modelo tabla customer
│       │   ├── Rental.java                ← Modelo tabla rental
│       │   ├── Inventory.java             ← Modelo tabla inventory
│       │   ├── Payment.java               ← Modelo tabla payment
│       │   ├── Store.java                 ← Modelo tabla store
│       │   └── Staff.java                 ← Modelo tabla staff
│       ├── controllers/
│       │   ├── FilmController.java        ← MVC controller films
│       │   ├── ActorController.java       ← MVC controller actores
│       │   ├── CustomerController.java    ← MVC controller clientes
│       │   ├── RentalController.java      ← MVC controller rentas
│       │   ├── InventoryController.java   ← MVC controller inventario
│       │   └── PaymentController.java     ← MVC controller pagos
│       ├── reports/
│       │   └── ReportManager.java         ← Estadísticas, CSV, JSON
│       ├── ui/
│       │   └── ConsoleUI.java             ← Interfaz de consola completa
│       └── utils/
│           └── Validator.java             ← Validaciones con regex

════════════════════════════════════════════════════════════════════
 FUNCIONALIDADES IMPLEMENTADAS
════════════════════════════════════════════════════════════════════

✓ Interfaz IDatapost<T>       — Contrato CRUD genérico
✓ Clase DataContext<T>        — Padre abstracto híbrido, métodos FINAL
✓ DAOs concretos y FINALES    — Film, Actor, Customer, Rental, Inventory, Payment
✓ Controladores MVC           — Uno por cada entidad del modelo
✓ Interfaz de consola         — Menús CRUD para cada módulo
✓ Reportes y estadísticas     — HashMap, ArrayList, métricas, aging
✓ Exportación CSV             — Catálogo de películas a archivo .csv
✓ Exportación JSON            — Catálogo de películas a archivo .json
✓ Expresiones regulares       — Email, fecha, cédula, teléfono, SSN, nombre
✓ Colecciones genéricas       — HashMap, ArrayList en estadísticas y búsquedas
✓ Respeto de PK autoincrement — executeInsertReturnKey()
✓ Respeto de FKs              — Validación antes de insertar relaciones
✓ Wildcards <?>               — Genérico <T> en DataContext e IDatapost
✓ JavaDoc en cada clase       — @author, @version, @param, @return

════════════════════════════════════════════════════════════════════
 CREDITOS
════════════════════════════════════════════════════════════════════

Asignatura : INF514 Programación II Java — Sección Z06
Profesor   :  Silverio Del Orbe
Institución: Universidad Autónoma de Santo Domingo (UASD)  
Estudiante : Luis Enrique Leocadio Aquino
Matricula  : 100457430        
Año        : 2026 ©  Santo Domingo, República Dominicana

NOTA: Agregue los el video junto con el Programa y un Capture de Pantalla de la Base de dato MYSQL con la Pelicula en la misma.
