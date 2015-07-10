package com.oreilly.springdata.jpa.core.mysql;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.oreilly.springdata.jpa.entity.Address;
import com.oreilly.springdata.jpa.entity.Customer;
import com.oreilly.springdata.jpa.entity.EmailAddress;
import com.oreilly.springdata.jpa.repository.CustomerRepository;

/**
 * Integration tests for {@link CustomerRepository}.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = {"classpath:/spring/*.xml"})
public class CustomerRepositoryIntegrationTest  {

	@Autowired
	CustomerRepository repository;

	@Test
	public void savesCustomerCorrectly() {

		EmailAddress email = new EmailAddress("alicia@keys.com");

		Customer dave = new Customer("Alicia", "Keys");
		dave.setEmailAddress(email);
		dave.add(new Address("27 Broadway", "New York", "United States"));

		Customer result = repository.save(dave);
		assertThat(result.getId(), is(notNullValue()));
	}

	@Test
	public void readsCustomerByEmail() {

		EmailAddress email = new EmailAddress("alicia@keys.com");
		Customer alicia = new Customer("Alicia", "Keys");
		alicia.setEmailAddress(email);

		repository.save(alicia);

		Customer result = repository.findByEmailAddress(email);
		assertThat(result, is(alicia));
	}

	@Test
	public void preventsDuplicateEmail() {

		Customer dave = repository.findByEmailAddress(new EmailAddress("dave@dmband.com"));

		Customer anotherDave = new Customer("Dave", "Matthews");
		anotherDave.setEmailAddress(dave.getEmailAddress());

		repository.save(anotherDave);
	}
}