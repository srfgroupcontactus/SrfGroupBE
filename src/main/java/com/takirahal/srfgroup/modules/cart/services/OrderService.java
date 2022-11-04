package com.takirahal.srfgroup.modules.cart.services;

import com.takirahal.srfgroup.modules.cart.dto.OrderDTO;
import com.takirahal.srfgroup.modules.cart.dto.filter.OrderFilter;
import com.takirahal.srfgroup.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    /**
     *
     * @param userPrincipal
     * @return
     */
    OrderDTO save(UserPrincipal userPrincipal, OrderDTO orderDTO);

    /**
     *
     * @param orderFilter
     * @param pageable
     * @return
     */
    Page<OrderDTO> getOrdersByCurrentUser(OrderFilter orderFilter, Pageable pageable);
}
