// Paquete de acceso a datos
package com.sakila.data;

// Importamos el modelo Customer
import com.sakila.models.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * CustomerData: DAO CONCRETO y FINAL para la tabla 'customer'.
 * Hereda CRUD de DataContext. Es FINAL: sin subclases.
 */
public final class CustomerData extends DataContext<Customer> {

    /** Constructor: conecta con la BD sakila */
    public CustomerData() {
        super(); // Abre conexión a través del padre
    }

    /**
     * Mapea una fila de la tabla 'customer' a un objeto Customer.
     * Lee todas las columnas por nombre.
     */
    @Override
    protected Customer mapResultSet(ResultSet rs) throws SQLException {
        Customer c = new Customer(); // Objeto vacío para llenar con datos de la fila

        c.setCustomerId(rs.getInt("customer_id"));       // PK del cliente
        c.setStoreId(rs.getInt("store_id"));             // FK de la tienda
        c.setFirstName(rs.getString("first_name"));      // Nombre
        c.setLastName(rs.getString("last_name"));        // Apellido
        c.setEmail(rs.getString("email"));               // Email
        c.setAddressId(rs.getInt("address_id"));         // FK de dirección
        c.setActive(rs.getBoolean("active"));            // Estado activo/inactivo
        c.setCreateDate(rs.getDate("create_date"));      // Fecha de registro
        c.setLastUpdate(rs.getTimestamp("last_update")); // Última modificación

        return c; // Retorna el cliente con todos sus datos
    }

    @Override
    protected String getTableName() { return "customer"; } // Nombre de la tabla

    @Override
    protected String getPrimaryKeyColumn() { return "customer_id"; } // PK de customer

    /**
     * post — Inserta un nuevo cliente en la BD.
     * No incluye customer_id (autoincrement).
     */
    @Override
    public boolean post(Customer c) {
        // INSERT con todos los campos obligatorios del cliente
        String sql = "INSERT INTO customer (store_id, first_name, last_name, email, address_id, active) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        return executeUpdate(sql,
                c.getStoreId(),   // FK de tienda
                c.getFirstName(), // Nombre
                c.getLastName(),  // Apellido
                c.getEmail(),     // Email
                c.getAddressId(), // FK de dirección
                c.isActive()      // Estado activo (true/false → 1/0 en BD)
        );
    }

    /**
     * put — Actualiza los datos de un cliente existente.
     */
    @Override
    public boolean put(Customer c) {
        // UPDATE de todos los campos editables del cliente
        String sql = "UPDATE customer SET store_id=?, first_name=?, last_name=?, " +
                     "email=?, address_id=?, active=? WHERE customer_id=?";

        return executeUpdate(sql,
                c.getStoreId(),    // Nueva tienda
                c.getFirstName(),  // Nuevo nombre
                c.getLastName(),   // Nuevo apellido
                c.getEmail(),      // Nuevo email
                c.getAddressId(),  // Nueva dirección
                c.isActive(),      // Nuevo estado
                c.getCustomerId()  // WHERE customer_id = ? (identifica el registro)
        );
    }

    /**
     * buscarPorEmail — Busca clientes por email (búsqueda parcial).
     *
     * @param email  Texto a buscar en el campo email
     * @return       Lista de clientes que coinciden
     */
    public List<Customer> buscarPorEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email LIKE ?"; // Búsqueda parcial por email
        ResultSet rs = executeQuery(sql, "%" + email + "%"); // Agrega comodines
        List<Customer> lista = new ArrayList<>();
        try {
            while (rs != null && rs.next()) lista.add(mapResultSet(rs)); // Convierte cada fila
        } catch (SQLException e) {
            System.err.println("[CustomerData] buscarPorEmail: " + e.getMessage());
        }
        return lista;
    }
}
