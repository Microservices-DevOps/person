package com.myapp.sample.service;

import com.myapp.sample.model.Person;
import java.util.List;

public interface PersonService {
  public List<Person> getAll();
  
  public Person save(Person p);
  
  public Person findById(Long ids);
}
