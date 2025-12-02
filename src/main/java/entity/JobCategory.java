// src/entity/JobCategory.java
package entity;

public class JobCategory {
    private Integer categoryId;    // 类别ID
    private String categoryName;   // 类别名称（如：家教、促销、服务员等）
    private String description;    // 类别描述

    // Getter和Setter
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}