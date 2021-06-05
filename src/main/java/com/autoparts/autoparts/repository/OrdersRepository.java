// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.repository;

import com.autoparts.autoparts.classes.Orders;

import org.springframework.data.repository.CrudRepository;

public interface OrdersRepository extends CrudRepository<Orders, Long> {
}

