package com.scc363.hospitalproject;

import com.scc363.hospitalproject.Customer.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
    Customer findCustomerById(Integer id);
}
