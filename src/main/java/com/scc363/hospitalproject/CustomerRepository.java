package com.scc363.hospitalproject;

import com.scc363.hospitalproject.Customer.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
    List<Customer> findByLastName(String lastName);
    Customer findCustomerById(Integer id);
}
