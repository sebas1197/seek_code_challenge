package com.seek.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.seek.dao.CustomerRepository;
import com.seek.dto.AgeMetrics;
import com.seek.dto.CustomerEventProjection;
import com.seek.dto.CustomerRequest;
import com.seek.dto.CustomerResponse;
import com.seek.model.Customer;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> listCustomers() throws Exception {
        List<Customer> all = customerRepository.findAll();
        if (all.isEmpty()) {
            throw new Exception("No records");
        }
        return all;
    }

    public CustomerResponse create(CustomerRequest customerRequest)
            throws Exception {
        Customer customer = new Customer();

        customer.setName(customerRequest.getName());
        customer.setLastname(customerRequest.getLastname());
        customer.setBirthday(customerRequest.getBirthday());
        customer.setAge(calculateAge(customerRequest.getBirthday()));

        this.customerRepository.save(customer);

        return new CustomerResponse(
                customer.getName(),
                customer.getLastname(),
                customer.getAge(),
                customer.getBirthday(),
                "Customer created");
    }

    public CustomerResponse update(Long id, CustomerRequest customerRequest)
            throws Exception {
        Customer existingCustomer = this.customerRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Customer not found"));

        existingCustomer.setName(customerRequest.getName());
        existingCustomer.setLastname(customerRequest.getLastname());
        existingCustomer.setBirthday(customerRequest.getBirthday());
        existingCustomer.setAge(calculateAge(customerRequest.getBirthday()));

        this.customerRepository.save(existingCustomer);

        return new CustomerResponse(
                existingCustomer.getName(),
                existingCustomer.getLastname(),
                existingCustomer.getAge(),
                existingCustomer.getBirthday()
                "Updated customer");
    }

    private int calculateAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public String delete(Long id) throws Exception {

        Customer existingCustomer = this.customerRepository
                .findById(id)
                .orElseThrow(() -> new Exception("Customer not found"));

        this.customerRepository.delete(existingCustomer);

        return "The customer with id: " + id + " has been deleted";
    }

    public AgeMetrics getAgeMetrics() throws Exception {
        AgeMetrics metrics = customerRepository.getAgeMetrics();
        if (metrics == null) {
            throw new Exception("No age metrics available");
        }
        return metrics;
    }

    public List<CustomerEventProjection> listCustomersWithEstimatedEventDate() throws Exception {
        List<CustomerEventProjection> list = customerRepository.findAllWithEstimatedEventDate();
        if (list.isEmpty()) {
            throw new Exception("No customers available");
        }
        return list;
    }

}
