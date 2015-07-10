package com.oreilly.springdata.jpa.order.mysql;

import static com.oreilly.springdata.jpa.core.CoreMatchers.named;
import static com.oreilly.springdata.jpa.core.CoreMatchers.with;
import static com.oreilly.springdata.jpa.order.OrderMatchers.LineItem;
import static com.oreilly.springdata.jpa.order.OrderMatchers.Product;
import static com.oreilly.springdata.jpa.order.OrderMatchers.containsOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matcher;
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
import com.oreilly.springdata.jpa.entity.LineItem;
import com.oreilly.springdata.jpa.entity.Order;
import com.oreilly.springdata.jpa.entity.Product;
import com.oreilly.springdata.jpa.repository.CustomerRepository;
import com.oreilly.springdata.jpa.repository.OrderRepository;
import com.oreilly.springdata.jpa.repository.ProductRepository;

/**
 * Integration tests for {@link OrderRepository}.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = {"classpath:/spring/*.xml"})
public class OrderRepositoryIntegrationTest {

	@Autowired
	OrderRepository repository;

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ProductRepository productRepository;

	@Test
	public void createOrder() {

		Customer dave = customerRepository.findByEmailAddress(new EmailAddress("dave@dmband.com"));
		Product iPad = productRepository.findOne(1L);

		Order order = new Order(dave, dave.getAddresses().iterator().next());
		order.add(new LineItem(iPad));

		order = repository.save(order);
		assertThat(order.getId(), is(notNullValue()));
	}

	@Test
	public void readOrder() {

		Customer dave = customerRepository.findByEmailAddress(new EmailAddress("dave@dmband.com"));
		List<Order> orders = repository.findByCustomer(dave);
		Matcher<Iterable<? super Order>> hasOrderForiPad = containsOrder(with(LineItem(with(Product(named("iPad"))))));

		assertThat(orders, hasSize(1));
		assertThat(orders, hasOrderForiPad);
	}
}