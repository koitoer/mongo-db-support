/**
 * 
 */
package com.koitoer.mongodb.spring;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.VerboseJSR303ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.koitoer.mongodb.domain.Author;
import com.koitoer.mongodb.domain.Book;
import com.koitoer.mongodb.domain.Location;
import com.koitoer.mongodb.domain.Publisher;
import com.koitoer.mongodb.domain.Stock;
import com.koitoer.mongodb.domain.Store;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
/**
 * @author Koitoer
 *
 */
@ContextConfiguration(locations = { "classpath:com/koitoer/spring/test-applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class MongoDBTestSuite {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Before
	public void init() throws UnknownHostException{
		mongoTemplate.dropCollection(Author.class);
		mongoTemplate.dropCollection(Book.class);
		mongoTemplate.dropCollection(Store.class);
	}

	@Test
	public void testCreateNewAuthor() {
		Query query = new Query();
	    assertEquals(0, mongoTemplate.count(query, Author.class));
	         
	    final Author author = new Author( "Kristina", "Chodorow" );
	    mongoTemplate.save( author );
	         
	    assertEquals(1, mongoTemplate.count(query, Author.class));

	}
	
	
	@Ignore
	@Test( expected = VerboseJSR303ConstraintViolationException.class )
	public void testCreateNewAuthorWithEmptyLastName() {
	    final Author author = new Author( "Kristina", "" );
	    mongoTemplate.save( author );
	}
	
	@Test
	public void testCreateNewBook() {
		Query query = new Query();
		
	    assertThat(  mongoTemplate.count(query, Author.class) ).isEqualTo( 0 );
	    assertThat(  mongoTemplate.count(query, Book.class) ).isEqualTo( 0 );
	         
	    final Publisher publisher = new Publisher( "O'Reilly" );
	    final Author author = new Author( "Kristina", "Chodorow" );
	         
	    final Book book = new Book( "MongoDB: The Definitive Guide" );
	    book.getAuthors().add( author );
	    book.setPublisher( publisher );
	    book.setPublishedDate( new LocalDate( 2013, 05, 23 ).toDate());        
	         
	    mongoTemplate.save( author );
	    mongoTemplate.save( book );
	         
	    assertThat(  mongoTemplate.count(query, Author.class) ).isEqualTo( 1 );
	    assertThat(  mongoTemplate.count(query, Book.class) ).isEqualTo( 1 );      
	}
	
	
	@Ignore
	@Test( expected = VerboseJSR303ConstraintViolationException.class )
	public void testCreateNewBookWithEmptyPublisher() {
	    final Author author = new Author( "Kristina", "Chodorow" );
	         
	    final Book book = new Book( "MongoDB: The Definitive Guide" );
	    book.getAuthors().add( author );
	    book.setPublishedDate( new LocalDate( 2013, 05, 23 ).toDate() );        
	         
	    mongoTemplate.save( author );
	    mongoTemplate.save( book );         
	}
	
	
	@Test
	public void testCreateNewStore() {
		Query query = new Query();
		
		 assertThat(  mongoTemplate.count(query, Author.class) ).isEqualTo( 0 );
		 assertThat(  mongoTemplate.count(query, Book.class) ).isEqualTo( 0 );
		 assertThat(  mongoTemplate.count(query, Store.class) ).isEqualTo( 0 );
	 
	    final Publisher publisher = new Publisher( "O'Reilly" );
	    final Author author = new Author( "Kristina", "Chodorow" );
	        
	    final Book book = new Book( "MongoDB: The Definitive Guide" );        
	    book.setPublisher( publisher );
	    book.setPublishedDate( new LocalDate( 2013, 05, 23 ).toDate() );
	    book.getAuthors().add( author );
	    book.getCategories().addAll( Arrays.asList( "Databases", "Programming", "NoSQL" ) );
	        
	    final Store store = new Store( "Waterstones Piccadilly" );
	    store.setLocation( new Location( -0.135484, 51.50957 ) );
	    store.getStock().add( new Stock( book, 10 ) );
	 
	    mongoTemplate.save( author );
	    mongoTemplate.save( book );      
	    mongoTemplate.save( store );
	        
		
		 assertThat(  mongoTemplate.count(query, Author.class) ).isEqualTo( 1 );
		 assertThat(  mongoTemplate.count(query, Book.class) ).isEqualTo( 1 );
		 assertThat(  mongoTemplate.count(query, Store.class) ).isEqualTo( 1 );
	}
}
