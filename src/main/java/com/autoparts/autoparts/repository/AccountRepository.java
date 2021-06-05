// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts.repository;


import com.autoparts.autoparts.classes.Account;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String>{
    Account findByConfirmationToken(String confirmationToken);
    

}
