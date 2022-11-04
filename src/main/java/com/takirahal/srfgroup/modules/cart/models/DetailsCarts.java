package com.takirahal.srfgroup.modules.cart.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsCarts {
    private int numberCarts; // number of carts selected
    private Double totalCarts; // total all carts withou tax
    private Double taxDelivery; // Tax for delivery
    private Double totalGlobalCarts; // For total global: total all carts + tax
}
