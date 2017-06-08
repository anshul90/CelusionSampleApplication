package com.anshul.celusiontestapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobiInventory {

    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("ContactName")
    @Expose
    private String contactName;
    @SerializedName("ContactTitle")
    @Expose
    private String contactTitle;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Region")
    @Expose
    private Object region;
    @SerializedName("PostalCode")
    @Expose
    private String postalCode;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Fax")
    @Expose
    private String fax;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getRegion() {
        return region;
    }

    public void setRegion(Object region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

}