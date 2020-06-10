package Model;

public class Address {

    private int addressId;
    private String address;
    private int cityId;
    private String phone;

    public Address() {}

    public Address(int addressId, String address, int cityId, String phone) {
        this.addressId = addressId;
        this.address = address;
        this.cityId = cityId;
        this.phone = phone;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
