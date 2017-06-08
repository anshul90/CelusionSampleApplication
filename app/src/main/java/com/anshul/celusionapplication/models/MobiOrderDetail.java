package com.anshul.celusionapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobiOrderDetail {

    @SerializedName("OrderID")
    @Expose
    private String orderID;
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("EmployeeID")
    @Expose
    private Integer employeeID;
    @SerializedName("OrderDate")
    @Expose
    private String orderDate;
    @SerializedName("RequiredDate")
    @Expose
    private String requiredDate;
    @SerializedName("ShippedDate")
    @Expose
    private String shippedDate;
    @SerializedName("ShipVia")
    @Expose
    private Integer shipVia;
    @SerializedName("Freight")
    @Expose
    private Double freight;
    @SerializedName("ShipName")
    @Expose
    private String shipName;
    @SerializedName("ShipAddress")
    @Expose
    private String shipAddress;
    @SerializedName("ShipCity")
    @Expose
    private String shipCity;
    @SerializedName("ShipRegion")
    @Expose
    private Object shipRegion;
    @SerializedName("ShipPostalCode")
    @Expose
    private String shipPostalCode;
    @SerializedName("ShipCountry")
    @Expose
    private String shipCountry;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Integer getShipVia() {
        return shipVia;
    }

    public void setShipVia(Integer shipVia) {
        this.shipVia = shipVia;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public Object getShipRegion() {
        return shipRegion;
    }

    public void setShipRegion(Object shipRegion) {
        this.shipRegion = shipRegion;
    }

    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }

}
