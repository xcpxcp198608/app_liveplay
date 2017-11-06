package com.wiatec.bplay.pay;

public class PayResultInfo {

    private int id;
    private int payerId;
    private int publisherId;
    private String channelName;
    private String auth;
    private String paymentId;
    private String state;
    private String cart;
    private String paymentMethod;
    private String paymentStatus;
    private String email;
    private String firstName;
    private String lastName;
    private String payPalPayerId;
    private String phone;
    private String countryCode;
    private float price;
    private String currency;
    private String description;
    private float transactionFee;
    private String createTime;
    private String updateTime;
    private String time;

    public PayResultInfo() {
    }

    public PayResultInfo(int payerId, int publisherId) {
        this.payerId = payerId;
        this.publisherId = publisherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPayPalPayerId() {
        return payPalPayerId;
    }

    public void setPayPalPayerId(String payPalPayerId) {
        this.payPalPayerId = payPalPayerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(float transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PayResultInfo{" +
                "id=" + id +
                ", payerId=" + payerId +
                ", publisherId=" + publisherId +
                ", channelName='" + channelName + '\'' +
                ", auth='" + auth + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", state='" + state + '\'' +
                ", cart='" + cart + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", payPalPayerId='" + payPalPayerId + '\'' +
                ", phone='" + phone + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", transactionFee=" + transactionFee +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
