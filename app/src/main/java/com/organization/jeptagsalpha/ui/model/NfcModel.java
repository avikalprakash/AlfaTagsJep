package com.organization.jeptagsalpha.ui.model;



public class NfcModel {
    private String EmployeeId;
    private String ProductId;
    private String DomesticId;
    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getDomesticId() {
        return DomesticId;
    }

    public void setDomesticId(String domesticId) {
        DomesticId = domesticId;
    }
}
