package com.scc363.hospitalproject;


import com.scc363.hospitalproject.Customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HospitalControler {
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/add")
    public String addCustomer(@RequestParam String first, @RequestParam String last, @RequestParam String email, @RequestParam String username, @RequestParam String password){
        Customer customer = new Customer();
        customer.setFirstName(first);
        customer.setLastName(last);
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        customerRepository.save(customer);
        return "Add new customer to repo!";
    }

    @GetMapping("/listusers")
    public Iterable<Customer> getCustomer() {
        return customerRepository.findAll();
    }

    @GetMapping("/find/{id}")
    public Customer findCustomerById(@PathVariable Integer id) {
        return customerRepository.findCustomerById(id);
    }
}
