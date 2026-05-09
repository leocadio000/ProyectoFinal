// Paquete de acceso a datos del proyecto
package com.sakila.data;

// Importaciones necesarias para manejo de base de datos
import java.sql.Connection;        // Representa la conexión activa con MySQL
import java.sql.DriverManager;     // Clase que gestiona los drivers JDBC
import java.sql.PreparedStatement; // Sentencia SQL segura con parámetros (evita SQL Injection)
import java.sql.ResultSet;         // Objeto que contiene los resultados de un SELECT
import java.sql.SQLException;      // Excepción general de SQL
import java.sql.Statement;         // Para obtener claves generadas (autoincrement)
import java.util.ArrayList;        // Implementación de lista dinámica
import java.util.List;             // Interfaz de colección ordenada

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * DataContext: Clase PADRE ABSTRACTA HÍBRIDA.
 * - Es abstracta porque no puede instanciarse directamente.
 * - Es híbrida porque tiene métodos CONCRETOS (implementados) y ABSTRACTOS.
 * - Los métodos concretos son FINAL: los hijos NO pueden sobrescribirlos (override).
 * - Implementa IDatapost para garantizar el contrato CRUD.
 * - Usa genéricos <T> para funcionar con cualquier modelo del sistema.
 */
public abstract class DataContext<T> implements IDatapost<T> {

    // -------------------------------------------------------------------------
    // CONSTANTES DE CONEXIÓN A LA BASE DE DATOS
    // Cambia estos valores según tu configuración de MySQL
    // -------------------------------------------------------------------------

    // URL de conexión JDBC: protocolo + host + puerto + nombre de BD
    protected static final String URL = "jdbc:mysql://localhost:3306/sakila"
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // Usuario de MySQL (cambiar si es diferente en tu entorno)
    protected static final String USER = "root";

    // Contraseña de MySQL (cambiar según tu configuración)
    protected static final String PASSWORD = "1234";

    // -------------------------------------------------------------------------
    // ATRIBUTO DE CONEXIÓN
    // -------------------------------------------------------------------------

    // Objeto Connection compartido por todos los métodos de la clase
    protected Connection connection;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    /**
     * Constructor de DataContext.
     * Registra el driver JDBC y abre la conexión a la base de datos.
     * Se ejecuta automáticamente al crear cualquier objeto hijo.
     */
    public DataContext() {
        try {
            // Carga el driver JDBC de MySQL en memoria — necesario para conectar
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Abre la conexión usando las constantes definidas arriba
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Mensaje de confirmación en consola
            System.out.println("[DataContext] Conexión establecida con sakila DB.");

        } catch (ClassNotFoundException e) {
            // Error: el JAR del conector MySQL no está en el classpath
            System.err.println("[DataContext] Driver no encontrado. ¿Agregaste mysql-connector.jar? " + e.getMessage());
        } catch (SQLException e) {
            // Error: credenciales incorrectas o MySQL no está corriendo
            System.err.println("[DataContext] Error al conectar con la BD: " + e.getMessage());
        }
    }

    // =========================================================================
    // MÉTODOS CONCRETOS Y FINALES — Los hijos NO pueden sobrescribir estos métodos
    // =========================================================================

    /**
     * executeUpdate — Ejecuta sentencias SQL de escritura: INSERT, UPDATE, DELETE.
     * Es FINAL: ningún hijo puede cambiar su comportamiento.
     * Usa PreparedStatement para prevenir inyección SQL.
     *
     * @param sql     Sentencia SQL con marcadores '?' como parámetros
     * @param params  Valores que reemplazarán los '?' en orden
     * @return        true si se afectó al menos una fila en la BD
     */
    protected final boolean executeUpdate(String sql, Object... params) {
        // try-with-resources: cierra automáticamente el PreparedStatement al terminar
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            // Asigna los parámetros al PreparedStatement antes de ejecutar
            setParameters(ps, params);

            // Ejecuta la sentencia y verifica si afectó alguna fila
            return ps.executeUpdate() > 0; // Devuelve true si hay cambios en la BD

        } catch (SQLException e) {
            // Captura errores de SQL (constraint violation, tabla no existe, etc.)
            System.err.println("[DataContext] executeUpdate error: " + e.getMessage());
            return false; // Retorna false para indicar que falló
        }
    }

    /**
     * executeQuery — Ejecuta sentencias SELECT y retorna el ResultSet.
     * Es FINAL: no puede ser modificado por clases hijas.
     * IMPORTANTE: El ResultSet queda abierto; el llamante debe usarlo antes de
     * abrir otra consulta con la misma conexión.
     *
     * @param sql     Sentencia SELECT con marcadores '?'
     * @param params  Valores opcionales para los parámetros
     * @return        ResultSet con las filas resultado, o null si hubo error
     */
    public final ResultSet executeQuery(String sql, Object... params) {
        try {
            // Crea el PreparedStatement (sin try-with-resources porque el RS lo necesita abierto)
            PreparedStatement ps = connection.prepareStatement(sql);

            // Asigna los parámetros de búsqueda
            setParameters(ps, params);

            // Ejecuta el SELECT y retorna el ResultSet para que el llamante lo procese
            return ps.executeQuery();

        } catch (SQLException e) {
            // Error en la consulta SELECT
            System.err.println("[DataContext] executeQuery error: " + e.getMessage());
            return null; // Retorna null; los métodos que llaman deben verificar esto
        }
    }

    /**
     * executeInsertReturnKey — Ejecuta un INSERT y retorna la PK generada.
     * Útil para tablas con autoincrement (film_id, actor_id, etc.).
     * Es FINAL: garantiza que la PK siempre se maneja de forma segura.
     * Respeta la regla de llave primaria autoincrement de la BD.
     *
     * @param sql     Sentencia INSERT con marcadores '?'
     * @param params  Valores para los campos del INSERT
     * @return        ID generado automáticamente, o -1 si falló
     */
    protected final int executeInsertReturnKey(String sql, Object... params) {
        try (
            // RETURN_GENERATED_KEYS indica a JDBC que capture el ID generado
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            // Asigna los valores de los campos del nuevo registro
            setParameters(ps, params);

            // Ejecuta el INSERT en la base de datos
            ps.executeUpdate();

            // Obtiene el ResultSet con la(s) clave(s) generada(s)
            ResultSet rs = ps.getGeneratedKeys();

            // Si se generó al menos una clave, la retorna
            if (rs.next()) return rs.getInt(1); // Columna 1 = el ID generado

        } catch (SQLException e) {
            // Error al insertar o al obtener la clave generada
            System.err.println("[DataContext] executeInsertReturnKey error: " + e.getMessage());
        }
        return -1; // Retorna -1 para indicar que el INSERT falló
    }

    /**
     * closeConnection — Cierra la conexión activa con la base de datos.
     * Es FINAL: el comportamiento de cierre no puede alterarse.
     * Debe llamarse al terminar de usar el objeto para liberar recursos.
     */
    public final void closeConnection() {
        try {
            // Verifica que la conexión no sea null y esté abierta antes de cerrar
            if (connection != null && !connection.isClosed()) {
                connection.close(); // Libera la conexión del pool de MySQL
                System.out.println("[DataContext] Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("[DataContext] closeConnection error: " + e.getMessage());
        }
    }

    /**
     * isConnected — Verifica si la conexión con la BD está activa.
     * Es FINAL: la verificación de conexión no puede modificarse.
     *
     * @return  true si la conexión existe y está abierta
     */
    public final boolean isConnected() {
        try {
            // Retorna true si connection no es null Y no está cerrada
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false; // Si hay excepción al verificar, asume desconectado
        }
    }

    // =========================================================================
    // MÉTODO AUXILIAR PRIVADO
    // =========================================================================

    /**
     * setParameters — Asigna dinámicamente los parámetros a un PreparedStatement.
     * Es privado: solo lo usan los métodos internos de DataContext.
     * Usa setObject que acepta cualquier tipo Java (String, int, Date, etc.)
     *
     * @param ps      PreparedStatement al que se le asignarán los valores
     * @param params  Arreglo de valores (pueden ser de cualquier tipo)
     */
    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        // Itera sobre cada parámetro recibido
        for (int i = 0; i < params.length; i++) {
            // setObject convierte automáticamente el tipo Java al tipo SQL correcto
            // Los índices en JDBC empiezan en 1, por eso i + 1
            ps.setObject(i + 1, params[i]);
        }
    }

    // =========================================================================
    // MÉTODOS ABSTRACTOS — Cada clase hija DEBE implementarlos obligatoriamente
    // =========================================================================

    /**
     * mapResultSet — Convierte una fila del ResultSet en un objeto del modelo.
     * Cada hijo sabe cómo mapear sus propias columnas (film, actor, etc.)
     *
     * @param rs  ResultSet posicionado en la fila actual
     * @return    Objeto del modelo con los datos de esa fila
     */
    protected abstract T mapResultSet(ResultSet rs) throws SQLException;

    /**
     * getTableName — Retorna el nombre exacto de la tabla en MySQL sakila.
     * Ejemplo: "film", "actor", "customer", "rental"
     *
     * @return  Nombre de la tabla como String
     */
    protected abstract String getTableName();

    /**
     * getPrimaryKeyColumn — Retorna el nombre de la columna PK de la tabla.
     * Ejemplo: "film_id", "actor_id", "customer_id"
     *
     * @return  Nombre de la columna PK como String
     */
    protected abstract String getPrimaryKeyColumn();

    // =========================================================================
    // IMPLEMENTACIONES BASE DE IDatapost
    // Proveen comportamiento por defecto usando los métodos finales de arriba.
    // Los hijos heredan estas implementaciones sin necesidad de repetir código.
    // =========================================================================

    /**
     * get(int id) — Implementación base: busca un registro por su PK.
     * Construye dinámicamente el SELECT usando getTableName() y getPrimaryKeyColumn().
     *
     * @param id  PK del registro a buscar
     * @return    Objeto del modelo, o null si no se encuentra
     */
    @Override
    public T get(int id) {
        // Construye: SELECT * FROM <tabla> WHERE <pk_columna> = ?
        String sql = "SELECT * FROM " + getTableName()
                   + " WHERE " + getPrimaryKeyColumn() + " = ?";

        // Ejecuta el SELECT con el id como parámetro
        ResultSet rs = executeQuery(sql, id);

        try {
            // Si hay resultados, mapea la primera fila al objeto del modelo
            if (rs != null && rs.next()) {
                return mapResultSet(rs); // Delega al hijo el mapeo concreto
            }
        } catch (SQLException e) {
            System.err.println("[DataContext] get(id) error: " + e.getMessage());
        }
        return null; // No se encontró ningún registro con ese ID
    }

    /**
     * getAll() — Implementación base: obtiene todos los registros de la tabla.
     * Retorna una lista vacía si no hay datos (nunca retorna null).
     *
     * @return  ArrayList con todos los objetos del modelo
     */
    @Override
    public List<T> getAll() {
        // Construye: SELECT * FROM <tabla>
        String sql = "SELECT * FROM " + getTableName();

        // Ejecuta el SELECT sin condiciones
        ResultSet rs = executeQuery(sql);

        // Lista que acumulará todos los objetos encontrados
        List<T> list = new ArrayList<>();

        try {
            // Itera fila por fila mientras haya datos en el ResultSet
            while (rs != null && rs.next()) {
                list.add(mapResultSet(rs)); // Convierte cada fila y la agrega a la lista
            }
        } catch (SQLException e) {
            System.err.println("[DataContext] getAll() error: " + e.getMessage());
        }
        return list; // Retorna la lista (puede estar vacía si la tabla está vacía)
    }

    /**
     * get(String column, Object value) — Búsqueda por columna y valor específico.
     * Permite filtrar por cualquier campo sin conocer el ID.
     *
     * @param column  Nombre de la columna a filtrar (ej: "title", "rating")
     * @param value   Valor a buscar en esa columna
     * @return        Lista de objetos que coinciden con el filtro
     */
    @Override
    public List<T> get(String column, Object value) {
        // Construye: SELECT * FROM <tabla> WHERE <columna> LIKE ?
        // LIKE permite búsquedas parciales (ej: buscar "ACT%" en title)
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + column + " LIKE ?";

        // Ejecuta con el valor de búsqueda
        ResultSet rs = executeQuery(sql, value);

        // Lista de resultados
        List<T> list = new ArrayList<>();

        try {
            // Recorre cada fila del resultado y la convierte en objeto
            while (rs != null && rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DataContext] get(column, value) error: " + e.getMessage());
        }
        return list; // Retorna los resultados encontrados
    }

    /**
     * delete(int id) — Implementación base: elimina un registro por su PK.
     * Construye el DELETE dinámicamente usando el nombre de tabla y PK.
     *
     * @param id  PK del registro a eliminar
     * @return    true si se eliminó correctamente
     */
    @Override
    public boolean delete(int id) {
        // Construye: DELETE FROM <tabla> WHERE <pk_columna> = ?
        String sql = "DELETE FROM " + getTableName()
                   + " WHERE " + getPrimaryKeyColumn() + " = ?";

        // Ejecuta el DELETE y retorna el resultado
        return executeUpdate(sql, id);
    }
}
