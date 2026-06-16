package BLL;

import DAL.TransactionDAO;
import DTO.TransactionDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import utils.Validator;
import java.util.List;

public class TransactionBLL {
    private final TransactionDAO transDAO = new TransactionDAO();

    public List<TransactionDTO> getAllTransactions() throws DatabaseException {
        return transDAO.getAll();
    }

    public TransactionDTO getTransactionById(int id) throws DatabaseException {
        return transDAO.getById(id);
    }

    public TransactionDTO getTransactionByOrder(int orderId) throws DatabaseException {
        return transDAO.getByOrder(orderId);
    }

    public boolean addTransaction(TransactionDTO trans) throws DatabaseException, ValidationException {
        validateTransaction(trans);
        return transDAO.add(trans);
    }

    public boolean updateTransaction(TransactionDTO trans) throws DatabaseException, ValidationException {
        validateTransaction(trans);
        return transDAO.update(trans);
    }

    public boolean deleteTransaction(int id) throws DatabaseException {
        return transDAO.delete(id);
    }

    public List<TransactionDTO> searchTransactions(String keyword) throws DatabaseException {
        if (Validator.isNullOrEmpty(keyword)) return getAllTransactions();
        return transDAO.search(keyword.trim());
    }

    private void validateTransaction(TransactionDTO trans) throws ValidationException {
        if (trans.getOrderId() <= 0) {
            throw new ValidationException("Đơn hàng", "Mã đơn hàng không hợp lệ!");
        }

        if (trans.getAmount() < 0) {
            throw new ValidationException("Số tiền", "Số tiền thanh toán không được âm!");
        }

        Validator.validateRequired(trans.getPaymentMethod(), "Phương thức thanh toán");
        Validator.validateRequired(trans.getStatus(), "Trạng thái");
    }
}
