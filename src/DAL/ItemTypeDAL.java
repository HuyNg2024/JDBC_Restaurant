package DAL;

import DTO.ItemTypeDTO;

import java.sql.*;
import java.util.ArrayList;

public class ItemTypeDAL {
    public ArrayList<ItemTypeDTO> getAllTypes() {
        ArrayList<ItemTypeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Item_Type";

        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ItemTypeDTO type = new ItemTypeDTO(
                        rs.getInt("TypeID"),
                        rs.getString("TypeName"),
                        rs.getInt("staffID")
                );
                list.add(type);
            }

        } catch (SQLException e) {
            System.out.println("❌ ItemTypeDAL - getAllTypes: " + e.getMessage());
        }

        return list;
    }
}
