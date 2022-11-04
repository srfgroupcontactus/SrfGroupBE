package com.takirahal.srfgroup.modules.cart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.takirahal.srfgroup.modules.cart.models.DetailsCarts;
import com.takirahal.srfgroup.modules.offer.entities.OfferImages;
import com.takirahal.srfgroup.modules.offer.entities.SellOffer;
import com.takirahal.srfgroup.modules.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sg_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_name_order", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "number_carts")
    private int numberCarts; // number of carts selected

    @Column(name = "total_carts")
    private Double totalCarts; // total all carts withou tax

    @Column(name = "tax_delivery")
    private Double taxDelivery; // Tax for delivery

    @Column(name = "total_globalCarts")
    private Double totalGlobalCarts; // For total global: total all carts + tax

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "passed_date")
    private Instant passedDate;

    @Column(name = "status")
    private String status;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "order_cart",
            joinColumns = { @JoinColumn(name = "order_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "cart_id", referencedColumnName = "id") }
    )
    private Set<Cart> carts = new HashSet<>();

    @ManyToOne
    private User user;
}
