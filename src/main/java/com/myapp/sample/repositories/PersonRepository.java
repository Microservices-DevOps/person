package com.myapp.sample.repositories;

import com.myapp.sample.model.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported=false)
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
}
