package com.cmhit.demo.domain.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cmhit.demo.domain.bean.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    //public Customer findByFirstName(String firstName);
    //public List<Customer> findByLastName(String lastName);

}