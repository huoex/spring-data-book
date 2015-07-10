package com.oreilly.springdata.jpa.core.mysql;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.oreilly.springdata.jpa.entity.Customer;
import com.oreilly.springdata.jpa.entity.EmailAddress;
import com.oreilly.springdata.jpa.repository.CustomerRepository;

/**
 * Integration test for the manual implementation ({@link JpaCustomerRepository}) of the {@link CustomerRepository}
 * interface.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = {"classpath:/spring/*.xml"})
public class JpaCustomerRepositoryIntegrationTest {

	@Autowired
	CustomerRepository repository;

	@Test
	public void insertsNewCustomerCorrectly() {

		Customer customer = new Customer("Alicia", "Keys");
		customer = repository.save(customer);

		assertThat(customer.getId(), is(notNullValue()));
	}

	@Test
	public void updatesCustomerCorrectly() {

		Customer dave = repository.findByEmailAddress(new EmailAddress("dave@dmband.com"));
		assertThat(dave, is(notNullValue()));

		dave.setLastname("Miller");
		dave = repository.save(dave);

		Customer reference = repository.findByEmailAddress(dave.getEmailAddress());
		assertThat(reference.getLastname(), is(dave.getLastname()));
	}
}