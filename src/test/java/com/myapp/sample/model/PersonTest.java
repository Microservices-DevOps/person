package com.myapp.sample.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonTest {

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        List<Person> list = entityManager.getEntityManager().createQuery("from Person").getResultList();
        for(Person person:list) {
            entityManager.remove(person);
        }
    }

    @Test
    public void testCRUD()
    {
        Person p1 = new Person();
        p1.setName("test person 1");
        p1.setEmail("test@person1.com");

        entityManager.persist(p1);

        List<Person> list = entityManager.getEntityManager().createQuery("from Person").getResultList();
        Assert.assertEquals(1L, list.size());

        Person p2 = list.get(0);
        Assert.assertEquals("test person 1", p2.getName());
        Assert.assertEquals("test@person1.com", p2.getEmail());

        Assert.assertEquals(p2.hashCode(), p2.hashCode());
        Assert.assertTrue(p2.equals(p2));
    }
}
