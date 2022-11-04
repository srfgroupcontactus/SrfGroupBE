package com.takirahal.srfgroup.modules.cart.entities;

import com.takirahal.srfgroup.modules.offer.entities.SellOffer;
import com.takirahal.srfgroup.modules.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sg_cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_name_cart", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total")
    private Double total;

    @Column(name = "status")
    private String status;

    @OneToOne
    private SellOffer sellOffer;

    @OneToOne
    private User user;
}
