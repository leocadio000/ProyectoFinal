// Paquete de reportes del proyecto
package com.sakila.reports;

// Importamos los DAOs necesarios para obtener datos para los reportes
import com.sakila.data.FilmData;
import com.sakila.data.PaymentData;
import com.sakila.data.RentalData;
import com.sakila.data.CustomerData;

// Colecciones genéricas para estadísticas (requerimiento del proyecto)
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

// Para exportar archivos CSV y JSON
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * ReportManager: Clase de reportes y estadísticas del sistema sakila.
 * - Genera reportes de inventario, rentas, pagos, actores
 * - Exporta datos a CSV y JSON (requerimiento del proyecto)
 * - Calcula métricas: Total, Promedio, Aging de cuentas por cobrar
 * - Usa HashMap y ArrayList para almacenar estadísticas en memoria
 */
public class ReportManager {

    // DAOs necesarios para obtener datos de las distintas tablas
    private FilmData    filmData;     // Para reportes de películas e inventario
    private PaymentData paymentData;  // Para reportes financieros
    private RentalData  rentalData;   // Para reportes de rentas
    private CustomerData customerData; // Para reportes de clientes

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    /**
     * Constructor: inicializa todos los DAOs necesarios para los reportes.
     */
    public ReportManager() {
        filmData     = new FilmData();     // DAO de películas
        paymentData  = new PaymentData();  // DAO de pagos
        rentalData   = new RentalData();   // DAO de rentas
        customerData = new CustomerData(); // DAO de clientes
    }

    // =========================================================================
    // REPORTE 1: ESTADÍSTICAS DE PELÍCULAS EN INVENTARIO
    // =========================================================================

    /**
     * reporteInventario — Muestra y retorna estadísticas del inventario de películas.
     * Incluye: total de copias, distribución por tienda, películas más rentadas.
     *
     * @return  HashMap con métricas de inventario
     */
    public HashMap<String, Object> reporteInventario() {
        System.out.println("\n========== REPORTE DE INVENTARIO ==========");

        // Obtiene estadísticas generales de películas desde el DAO
        HashMap<String, Object> stats = filmData.getEstadisticas();

        // Muestra cada métrica del HashMap en la consola
        System.out.println("Total de películas en catálogo : " + stats.get("total"));
        System.out.println("Precio de renta promedio       : $" + stats.get("promedio_renta"));
        System.out.println("Precio de renta máximo         : $" + stats.get("max_renta"));
        System.out.println("Precio de renta mínimo         : $" + stats.get("min_renta"));
        System.out.println("Duración promedio (minutos)    : " + stats.get("promedio_duracion"));
        System.out.println("===========================================\n");

        return stats; // Retorna las estadísticas para uso en la UI
    }

    // =========================================================================
    // REPORTE 2: ACTORES Y PELÍCULAS EN QUE PARTICIPAN
    // =========================================================================

    /**
     * reporteActoresPorFilm — Muestra los actores de una película específica.
     * Usa JOIN entre film, film_actor y actor.
     *
     * @param filmId    ID de la película
     * @param tituloFilm  Título de la película (para mostrar en el reporte)
     */
    public void reporteActoresPorFilm(int filmId, String tituloFilm) {
        System.out.println("\n===== ACTORES DE: " + tituloFilm + " =====");

        // Obtiene la lista de actores desde el DAO via JOIN
        List<String> actores = filmData.getActoresPorFilm(filmId);

        // Si no hay actores, informa al usuario
        if (actores.isEmpty()) {
            System.out.println("No se encontraron actores para esta película.");
            return;
        }

        // Muestra cada actor numerado
        for (int i = 0; i < actores.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, actores.get(i)); // Formato numerado
        }
        System.out.println("Total de actores: " + actores.size()); // Total al final
        System.out.println("==========================================\n");
    }

    // =========================================================================
    // REPORTE 3: RENTAS POR TIENDA, CIUDAD, PAÍS, CLIENTES
    // =========================================================================

    /**
     * reporteRentasPorTienda — Muestra el total de rentas agrupadas por tienda.
     * Usa HashMap para acumular los conteos.
     */
    public void reporteRentasPorTienda() {
        System.out.println("\n====== RENTAS POR TIENDA ======");

        // HashMap para acumular: storeId → cantidad de rentas
        HashMap<Integer, Integer> rentasPorTienda = new HashMap<>();

        try {
            // SQL con JOIN para obtener el conteo de rentas agrupado por tienda
            String sql = "SELECT i.store_id, COUNT(r.rental_id) as total_rentas " +
                         "FROM rental r " +
                         "JOIN inventory i ON r.inventory_id = i.inventory_id " +
                         "GROUP BY i.store_id " +
                         "ORDER BY total_rentas DESC"; // Ordena de mayor a menor

            // Ejecuta la consulta a través del DAO de rentas
            ResultSet rs = rentalData.executeQuery(sql);

            while (rs != null && rs.next()) {
                int storeId      = rs.getInt("store_id");    // ID de la tienda
                int totalRentas  = rs.getInt("total_rentas"); // Cantidad de rentas

                rentasPorTienda.put(storeId, totalRentas); // Guarda en el HashMap

                // Muestra el resultado en consola
                System.out.printf("  Tienda #%d : %d rentas%n", storeId, totalRentas);
            }
        } catch (SQLException e) {
            System.err.println("[ReportManager] reporteRentasPorTienda: " + e.getMessage());
        }
        System.out.println("===============================\n");
    }

    // =========================================================================
    // REPORTE 4: PAGOS Y COBRANZAS POR TIENDA
    // =========================================================================

    /**
     * reportePagosPorTienda — Muestra el total recaudado por tienda.
     * Usa HashMap de PaymentData para obtener los datos.
     */
    public void reportePagosPorTienda() {
        System.out.println("\n====== COBRANZAS POR TIENDA ======");

        // Obtiene el HashMap con ingresos por tienda desde el DAO de pagos
        HashMap<Integer, BigDecimal> pagos = paymentData.getPagosPorTienda();

        // Itera el HashMap para mostrar cada tienda y su total recaudado
        for (HashMap.Entry<Integer, BigDecimal> entry : pagos.entrySet()) {
            System.out.printf("  Tienda #%d : $%.2f recaudado%n",
                    entry.getKey(), entry.getValue()); // Formato monetario
        }

        // Calcula el gran total sumando todos los valores del HashMap
        BigDecimal granTotal = pagos.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Suma acumulativa

        System.out.printf("  TOTAL GENERAL: $%.2f%n", granTotal); // Muestra el total
        System.out.println("==================================\n");
    }

    // =========================================================================
    // REPORTE 5: AGING DE CUENTAS POR COBRAR
    // =========================================================================

    /**
     * reporteAging — Muestra las rentas pendientes de devolución (aging).
     * Agrupa por días de atraso para calcular el aging de cuentas por cobrar.
     * Usa HashMap para acumular rentas por rango de días.
     */
    public void reporteAging() {
        System.out.println("\n====== AGING CUENTAS POR COBRAR ======");

        // HashMap para categorizar las rentas por rango de días de atraso
        // Claves: "0-7 días", "8-15 días", "16-30 días", "+30 días"
        HashMap<String, Integer> agingMap = new HashMap<>();
        agingMap.put("0-7 dias",  0); // Inicializa contadores en 0
        agingMap.put("8-15 dias", 0);
        agingMap.put("16-30 dias",0);
        agingMap.put("+30 dias",  0);

        try {
            // Obtiene rentas no devueltas con el cálculo de días de atraso
            String sql = "SELECT rental_id, customer_id, rental_date, " +
                         "DATEDIFF(NOW(), rental_date) as dias_atraso " + // Calcula días desde renta
                         "FROM rental WHERE return_date IS NULL " +         // Solo las no devueltas
                         "ORDER BY dias_atraso DESC";                       // Las más atrasadas primero

            ResultSet rs = rentalData.executeQuery(sql); // Ejecuta la consulta

            int totalPendientes = 0; // Contador de rentas pendientes

            while (rs != null && rs.next()) {
                int dias = rs.getInt("dias_atraso"); // Días que lleva sin devolver
                totalPendientes++; // Incrementa el contador total

                // Clasifica la renta en el rango de días correspondiente
                if (dias <= 7) {
                    agingMap.put("0-7 dias", agingMap.get("0-7 dias") + 1); // +1 al rango 0-7
                } else if (dias <= 15) {
                    agingMap.put("8-15 dias", agingMap.get("8-15 dias") + 1); // +1 al rango 8-15
                } else if (dias <= 30) {
                    agingMap.put("16-30 dias", agingMap.get("16-30 dias") + 1); // +1 al rango 16-30
                } else {
                    agingMap.put("+30 dias", agingMap.get("+30 dias") + 1); // +1 a más de 30 días
                }
            }

            // Muestra el resultado del aging por rango
            System.out.println("  Rentas pendientes de devolución:");
            System.out.printf("  0-7  días : %d rentas%n", agingMap.get("0-7 dias"));
            System.out.printf("  8-15 días : %d rentas%n", agingMap.get("8-15 dias"));
            System.out.printf("  16-30 días: %d rentas%n", agingMap.get("16-30 dias"));
            System.out.printf("  +30  días : %d rentas%n", agingMap.get("+30 dias"));
            System.out.printf("  TOTAL PENDIENTES: %d%n", totalPendientes); // Total de no devueltas

        } catch (SQLException e) {
            System.err.println("[ReportManager] reporteAging: " + e.getMessage());
        }
        System.out.println("======================================\n");
    }

    // =========================================================================
    // EXPORTACIÓN A CSV (requerimiento 5a del proyecto)
    // =========================================================================

    /**
     * exportarFilmsCSV — Exporta el catálogo de películas a un archivo CSV.
     * Formato: film_id, title, release_year, rating, rental_rate, length
     *
     * @param rutaArchivo  Ruta y nombre del archivo CSV a crear (ej: "films.csv")
     */
    public void exportarFilmsCSV(String rutaArchivo) {
        System.out.println("[ReportManager] Exportando películas a CSV: " + rutaArchivo);

        // try-with-resources: cierra automáticamente el PrintWriter al terminar
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {

            // Escribe la línea de encabezados del CSV (primera fila)
            pw.println("film_id,title,release_year,rating,rental_rate,length");

            // Obtiene todos los films desde la BD
            String sql = "SELECT film_id, title, release_year, rating, rental_rate, length FROM film";
            ResultSet rs = filmData.executeQuery(sql); // Ejecuta el SELECT

            while (rs != null && rs.next()) {
                // Construye cada fila del CSV con los valores separados por comas
                // Pone el título entre comillas por si contiene comas
                pw.printf("%d,\"%s\",%d,%s,%.2f,%d%n",
                        rs.getInt("film_id"),           // ID de la película
                        rs.getString("title"),           // Título entre comillas
                        rs.getInt("release_year"),       // Año de estreno
                        rs.getString("rating"),          // Clasificación
                        rs.getBigDecimal("rental_rate"), // Precio
                        rs.getInt("length")              // Duración
                );
            }

            System.out.println("[ReportManager] CSV generado exitosamente: " + rutaArchivo);

        } catch (IOException | SQLException e) {
            // Captura tanto errores de escritura de archivo como de BD
            System.err.println("[ReportManager] Error al exportar CSV: " + e.getMessage());
        }
    }

    // =========================================================================
    // EXPORTACIÓN A JSON (requerimiento 5a del proyecto)
    // =========================================================================

    /**
     * exportarFilmsJSON — Exporta el catálogo de películas a un archivo JSON.
     * Genera un JSON válido con un array de objetos película.
     *
     * @param rutaArchivo  Ruta y nombre del archivo JSON (ej: "films.json")
     */
    public void exportarFilmsJSON(String rutaArchivo) {
        System.out.println("[ReportManager] Exportando películas a JSON: " + rutaArchivo);

        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {

            pw.println("["); // Abre el array JSON

            // SQL para obtener todos los films
            String sql = "SELECT film_id, title, release_year, rating, rental_rate, length FROM film";
            ResultSet rs = filmData.executeQuery(sql);

            boolean primero = true; // Controla si se agrega coma entre objetos

            while (rs != null && rs.next()) {
                if (!primero) pw.println(","); // Agrega coma entre objetos JSON (no al primero)
                primero = false; // A partir del segundo elemento se agrega coma antes

                // Construye el objeto JSON para cada película
                pw.println("  {");
                pw.printf("    \"film_id\"     : %d,%n",    rs.getInt("film_id"));
                pw.printf("    \"title\"       : \"%s\",%n", rs.getString("title").replace("\"", "\\\"")); // Escapa comillas
                pw.printf("    \"release_year\": %d,%n",    rs.getInt("release_year"));
                pw.printf("    \"rating\"      : \"%s\",%n", rs.getString("rating"));
                pw.printf("    \"rental_rate\" : %.2f,%n",  rs.getBigDecimal("rental_rate").doubleValue());
                pw.printf("    \"length\"      : %d%n",     rs.getInt("length"));
                pw.print("  }"); // Cierra el objeto JSON (sin coma aún)
            }

            pw.println("\n]"); // Cierra el array JSON

            System.out.println("[ReportManager] JSON generado exitosamente: " + rutaArchivo);

        } catch (IOException | SQLException e) {
            System.err.println("[ReportManager] Error al exportar JSON: " + e.getMessage());
        }
    }

    // =========================================================================
    // MÉTRICAS GENERALES
    // =========================================================================

    /**
     * reporteEstadisticasPagos — Muestra estadísticas financieras generales.
     * Total, promedio, máximo y mínimo de pagos.
     */
    public void reporteEstadisticasPagos() {
        System.out.println("\n====== ESTADÍSTICAS DE PAGOS ======");

        // Obtiene las estadísticas del DAO de pagos
        HashMap<String, Object> stats = paymentData.getEstadisticasPagos();

        // Muestra cada métrica del HashMap
        System.out.println("  Total de transacciones  : " + stats.get("total_pagos"));
        System.out.println("  Monto total recaudado   : $" + stats.get("monto_total"));
        System.out.println("  Pago promedio           : $" + stats.get("promedio"));
        System.out.println("  Pago máximo registrado  : $" + stats.get("max_pago"));
        System.out.println("  Pago mínimo registrado  : $" + stats.get("min_pago"));
        System.out.println("===================================\n");
    }

    /**
     * cerrar — Cierra todas las conexiones de los DAOs del ReportManager.
     * Debe llamarse al finalizar el uso de los reportes.
     */
    public void cerrar() {
        filmData.closeConnection();     // Cierra conexión del DAO de films
        paymentData.closeConnection();  // Cierra conexión del DAO de pagos
        rentalData.closeConnection();   // Cierra conexión del DAO de rentas
        customerData.closeConnection(); // Cierra conexión del DAO de clientes
    }
}
