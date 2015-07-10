package com.oreilly.springdata.jpa.core.mysql;

import static com.oreilly.springdata.jpa.core.CoreMatchers.named;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.oreilly.springdata.jpa.entity.Product;
import com.oreilly.springdata.jpa.repository.ProductRepository;

/**
 * Integration tests for {@link ProductRepository}.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = {"classpath:/spring/*.xml"})
public class ProductRepositoryIntegrationTest {

	@Autowired
	ProductRepository repository;

	@Test
	public void createProduct() {

		Product product = new Product("Camera bag", new BigDecimal(49.99));
		product = repository.save(product);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void lookupProductsByDescription() {

		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "name");
		Page<Product> page = repository.findByDescriptionContaining("Apple", pageable);

		assertThat(page.getContent(), hasSize(1));
		assertThat(page, Matchers.<Product> hasItems(named("iPad")));
		assertThat(page.getTotalElements(), is(2L));
		assertThat(page.isFirstPage(), is(true));
		assertThat(page.isLastPage(), is(false));
		assertThat(page.hasNextPage(), is(true));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findsProductsByAttributes() {

		List<Product> products = repository.findByAttributeAndValue("connector", "plug");

		assertThat(products, Matchers.<Product> hasItems(named("Dock")));
	}
}