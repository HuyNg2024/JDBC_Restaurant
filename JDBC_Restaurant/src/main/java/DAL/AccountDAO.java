package DAL;

import DTO.AccountDTO;
import exceptions.DatabaseException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AccountDAO implements GenericDAO<AccountDTO> {

    @Override
    public List<AccountDTO> getAll() throws DatabaseException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from AccountDTO", AccountDTO.class).list();
        } catch (Exception e) {
            throw new DatabaseException("Error getting all accounts: " + e.getMessage());
        }
    }

    @Override
    public AccountDTO getById(int id) throws DatabaseException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AccountDTO.class, id);
        } catch (Exception e) {
            throw new DatabaseException("Error getting account by ID: " + e.getMessage());
        }
    }

    public AccountDTO getByUsername(String username) throws DatabaseException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AccountDTO> query = session.createQuery("from AccountDTO where username = :un", AccountDTO.class);
            query.setParameter("un", username);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DatabaseException("Error getting account by username: " + e.getMessage());
        }
    }

    @Override
    public boolean add(AccountDTO entity) throws DatabaseException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DatabaseException("Error adding account: " + e.getMessage());
        }
    }

    @Override
    public boolean update(AccountDTO entity) throws DatabaseException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DatabaseException("Error updating account: " + e.getMessage());
        }
    }

    public boolean updatePassword(int accountId, String newHash, String newSalt) throws DatabaseException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AccountDTO account = session.get(AccountDTO.class, accountId);
            if (account != null) {
                account.setPassword(newHash);
                account.setSalt(newSalt);
                session.update(account);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DatabaseException("Error updating password: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AccountDTO account = session.get(AccountDTO.class, id);
            if (account != null) {
                session.delete(account);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DatabaseException("Error deleting account: " + e.getMessage());
        }
    }

    @Override
    public List<AccountDTO> search(String keyword) throws DatabaseException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AccountDTO> query = session.createQuery("from AccountDTO where username like :kw or role like :kw", AccountDTO.class);
            query.setParameter("kw", "%" + keyword + "%");
            return query.list();
        } catch (Exception e) {
            throw new DatabaseException("Error searching accounts: " + e.getMessage());
        }
    }
}


