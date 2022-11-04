package com.takirahal.srfgroup.modules.cart.mapper;

import com.takirahal.srfgroup.mapper.EntityMapper;
import com.takirahal.srfgroup.modules.cart.dto.OrderDTO;
import com.takirahal.srfgroup.modules.cart.entities.Order;
import com.takirahal.srfgroup.modules.user.mapper.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CartMapper.class})
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
}
