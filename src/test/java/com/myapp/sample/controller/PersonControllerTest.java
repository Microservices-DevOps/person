package com.myapp.sample.controller;

import com.myapp.sample.model.Person;
import com.myapp.sample.repositories.PersonRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonControllerTest {
  
  MockMvc mockMvc;
  
  @Mock
  private PersonController personController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  PersonRepository personRepository;
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    personRepository.deleteAll();
  }
  
  @Test
  public void testRegister() throws Exception {
    HttpEntity<Object> person = getHttpEntity(
        "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\"}");
    ResponseEntity<Person> response = template.postForEntity(
        "/api/person", person, Person.class);
    Assert.assertEquals("test 1", response.getBody().getName());
    Assert.assertEquals(200,response.getStatusCode().value());
  }

  @Test
  public void testGetAllPersons() throws Exception {
    HttpEntity<Object> person = getHttpEntity(
            "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\"}");
    ResponseEntity<Person> response = template.postForEntity(
            "/api/person", person, Person.class);

    ParameterizedTypeReference<List<Person>> responseType = new ParameterizedTypeReference<List<Person>>(){};
    ResponseEntity<List<Person>> response2 = template.exchange("/api/person", HttpMethod.GET, null, responseType);
    Assert.assertEquals(response2.getBody().size(), 1);
    Assert.assertEquals("test 1", response2.getBody().get(0).getName());
    Assert.assertEquals(200,response2.getStatusCode().value());
  }

  @Test
  public void testGetPersonById() throws Exception {
    HttpEntity<Object> person = getHttpEntity(
            "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\"}");
    ResponseEntity<Person> response = template.postForEntity(
            "/api/person", person, Person.class);

    ParameterizedTypeReference<List<Person>> responseType = new ParameterizedTypeReference<List<Person>>(){};
    ResponseEntity<List<Person>> response2 = template.exchange("/api/person", HttpMethod.GET, null, responseType);

    Long id = response2.getBody().get(0).getId();
    ResponseEntity<Person> response3 = template.getForEntity(
            "/api/person/" + id, Person.class);
    Assert.assertEquals("test 1", response3.getBody().getName());
    Assert.assertEquals(200,response3.getStatusCode().value());
  }

  @Test
  public void testGetPersonByNull() throws Exception {
    ResponseEntity<Person> response3 = template.getForEntity(
            "/api/person/1", Person.class);
    Assert.assertEquals(404,response3.getStatusCode().value());
  }

  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }

}
