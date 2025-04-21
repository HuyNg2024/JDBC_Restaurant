package DTO;

public class ItemTypeDTO {

    private int typeID;
    private String typeName;
    private int staffID;

    // Constructors
    public ItemTypeDTO() {
    }

    public ItemTypeDTO(int typeID, String typeName, int staffID) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.staffID = staffID;
    }

    // Getters and Setters
    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    // toString (optional)
    @Override
    public String toString() {
        return "ItemTypeDTO{" +
                "typeID=" + typeID +
                ", typeName='" + typeName + '\'' +
                ", staffID=" + staffID +
                '}';
    }
}
