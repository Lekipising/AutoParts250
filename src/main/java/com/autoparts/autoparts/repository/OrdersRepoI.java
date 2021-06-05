// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.repository;
import java.time.LocalDate;
import com.autoparts.autoparts.classes.Orders;
import org.springframework.data.repository.CrudRepository;


public interface OrdersRepoI extends CrudRepository<Orders, LocalDate> {
}
