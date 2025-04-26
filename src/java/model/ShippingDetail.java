package model;

import jakarta.persistence.*;

@Entity
@Table(name = "SHIPPINGDETAIL")
public class ShippingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shippingId")
    private int shippingId;

    @ManyToOne
    @JoinColumn(name = "buyerId", nullable = false)
    private BuyerDetail buyer;

    @ManyToOne
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    // Constructors
    public ShippingDetail() {}

    public ShippingDetail(BuyerDetail buyer, Address address) {
        this.buyer = buyer;
        this.address = address;
    }

    // Getters and Setters
    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public BuyerDetail getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerDetail buyer) {
        this.buyer = buyer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
