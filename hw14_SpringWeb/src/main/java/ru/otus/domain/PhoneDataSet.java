package ru.otus.domain;

public class PhoneDataSet {

    private Long id;

    private String phone;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String phone) {
        this.id = null;
        this.phone = phone;
    }

    public PhoneDataSet(Long id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                '}';
    }

}
