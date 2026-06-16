package DAL;

import exceptions.DatabaseException;
import java.util.List;

/**
 * Generic Data Access Object interface for basic CRUD operations.
 * @param <T> The DTO type this DAO manages.
 */
public interface GenericDAO<T> {
    
    /**
     * Retrieve all records.
     */
    List<T> getAll() throws DatabaseException;
    
    /**
     * Retrieve a specific record by its ID.
     */
    T getById(int id) throws DatabaseException;
    
    /**
     * Add a new record.
     */
    boolean add(T entity) throws DatabaseException;
    
    /**
     * Update an existing record.
     */
    boolean update(T entity) throws DatabaseException;
    
    /**
     * Delete a record by its ID.
     */
    boolean delete(int id) throws DatabaseException;
    
    /**
     * Search records using a keyword.
     */
    List<T> search(String keyword) throws DatabaseException;
}
