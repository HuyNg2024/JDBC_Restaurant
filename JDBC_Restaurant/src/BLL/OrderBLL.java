package BLL;

import DAL.OrderDAO;
import DAL.OrderItemDAO;
import DTO.OrderDTO;
import DTO.OrderItemDTO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.util.List;

public class OrderBLL {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    public List<OrderDTO> getAllOrders() throws DatabaseException {
        return orderDAO.getAll();
    }

    public OrderDTO getOrderById(int id) throws DatabaseException {
        return orderDAO.getById(id);
    }

    public List<OrderDTO> getOrdersByTable(int tableId) throws DatabaseException {
        return orderDAO.getByTable(tableId);
    }

    public List<OrderDTO> getOrdersByStatus(String status) throws DatabaseException {
        return orderDAO.getByStatus(status);
    }

    public boolean addOrder(OrderDTO order) throws DatabaseException, ValidationException {
        validateOrder(order);
        return orderDAO.add(order);
    }

    public boolean updateOrder(OrderDTO order) throws DatabaseException, ValidationException {
        validateOrder(order);
        return orderDAO.update(order);
    }

    public boolean updateStatus(int orderId, String status) throws DatabaseException {
        return orderDAO.updateStatus(orderId, status);
    }

    public boolean deleteOrder(int id) throws DatabaseException {
        return orderDAO.delete(id);
    }

    public List<OrderDTO> searchOrders(String keyword) throws DatabaseException {
        if (keyword == null || keyword.trim().isEmpty()) return getAllOrders();
        return orderDAO.search(keyword.trim());
    }

    // --- ORDER ITEM METHODS ---

    public List<OrderItemDTO> getOrderItems(int orderId) throws DatabaseException {
        return orderItemDAO.getByOrderId(orderId);
    }

    public boolean addOrderItem(OrderItemDTO item) throws DatabaseException, ValidationException {
        if (item.getOrderId() <= 0) throw new ValidationException("Đơn hàng", "Mã đơn hàng không hợp lệ!");
        if (item.getItemId() <= 0) throw new ValidationException("Món ăn", "Vui lòng chọn món ăn!");
        if (item.getQuantity() <= 0) throw new ValidationException("Số lượng", "Số lượng phải lớn hơn 0!");
        
        boolean success = orderItemDAO.addItem(item);
        if (success) {
            recalculateOrderTotal(item.getOrderId());
        }
        return success;
    }

    public boolean removeOrderItem(int orderId, int itemId) throws DatabaseException {
        boolean success = orderItemDAO.removeItem(orderId, itemId);
        if (success) {
            recalculateOrderTotal(orderId);
        }
        return success;
    }

    /**
     * Recalculates the total price of an order based on its items
     */
    private void recalculateOrderTotal(int orderId) throws DatabaseException {
        List<OrderItemDTO> items = orderItemDAO.getByOrderId(orderId);
        double total = 0;
        for (OrderItemDTO item : items) {
            total += item.getUnitPrice() * item.getQuantity();
        }
        
        OrderDTO order = getOrderById(orderId);
        if (order != null) {
            order.setTotalPrice(total);
            orderDAO.update(order);
        }
    }

    private void validateOrder(OrderDTO order) throws ValidationException {
        if (order.getTableId() <= 0) {
            throw new ValidationException("Bàn ăn", "Vui lòng chọn bàn ăn!");
        }
        
        if (order.getTotalPrice() < 0) {
            throw new ValidationException("Tổng tiền", "Tổng tiền không được âm!");
        }
    }
}
