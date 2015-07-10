package com.oreilly.springdata.jpa.core.mysql;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.Predicate;
import com.oreilly.springdata.jpa.entity.Product;
import com.oreilly.springdata.jpa.entity.QProduct;
import com.oreilly.springdata.jpa.repository.ProductRepository;

/**
 * Integration test showing the usage of Querydsl {@link Predicate} to query repositories implementing
 * {@link QueryDslPredicateExecutor}.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = {"classpath:/spring/*.xml"})
public class QuerydslProductRepositoryIntegrationTest {

	static final QProduct product = QProduct.product;

	@Autowired
	ProductRepository repository;

	@Test
	public void findProductsByQuerydslPredicate() {

		Product iPad = repository.findOne(product.name.eq("iPad"));
		Predicate tablets = product.description.contains("tablet");

		Iterable<Product> result = repository.findAll(tablets);
		assertThat(result, is(Matchers.<Product> iterableWithSize(1)));
		assertThat(result, hasItem(iPad));
	}
}