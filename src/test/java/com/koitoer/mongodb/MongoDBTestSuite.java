/**
 * 
 */
package com.koitoer.mongodb;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;
import org.mongodb.morphia.VerboseJSR303ConstraintViolationException;

import com.koitoer.mongodb.domain.Author;
import com.koitoer.mongodb.domain.Book;
import com.koitoer.mongodb.domain.Location;
import com.koitoer.mongodb.domain.Publisher;
import com.koitoer.mongodb.domain.Stock;
import com.koitoer.mongodb.domain.Store;
import com.mongodb.MongoClient;

import static org.fest.assertions.api.Assertions.*;
import static org.junit.Assert.*;
/**
 * @author Koitoer
 *
 */
public class MongoDBTestSuite {
	
	Datastore dataStore = null;
	
	@Before
	public void init() throws UnknownHostException{
		if(dataStore == null){
			final Morphia morphia = new Morphia();
			new ValidationExtension( morphia );
			
			final MongoClient client = new MongoClient( "localhost", 27017 );
			dataStore = morphia
			    .map( Store.class, Book.class, Author.class )
			    .createDatastore( client, "bookstore" );
			
			dataStore.ensureIndexes();
			dataStore.ensureCaps();
			
			dataStore.delete(dataStore.createQuery(Author.class));
			dataStore.delete(dataStore.createQuery(Book.class));
			dataStore.delete(dataStore.createQuery(Store.class));
		}
	}

	@Test
	public void testCreateNewAuthor() {
		
	    assertEquals(0, dataStore.getCollection( Author.class ).count() );
	         
	    final Author author = new Author( "Kristina", "Chodorow" );
	    dataStore.save( author );
	         
	    assertEquals(1, dataStore.getCollection( Author.class ).count() );
	}
	
	
	@Test( expected = VerboseJSR303ConstraintViolationException.class )
	public void testCreateNewAuthorWithEmptyLastName() {
	    final Author author = new Author( "Kristina", "" );
	    dataStore.save( author );
	}
	
	@Test
	public void testCreateNewBook() {
	    assertThat( dataStore.getCollection( Author.class ).count() ).isEqualTo( 0 );
	    assertThat( dataStore.getCollection( Book.class ).count() ).isEqualTo( 0 );
	         
	    final Publisher publisher = new Publisher( "O'Reilly" );
	    final Author author = new Author( "Kristina", "Chodorow" );
	         
	    final Book book = new Book( "MongoDB: The Definitive Guide" );
	    book.getAuthors().add( author );
	    book.setPublisher( publisher );
	    book.setPublishedDate( new LocalDate( 2013, 05, 23 ).toDate());        
	         
	    dataStore.save( author );
	    dataStore.save( book );
	         
	    assertThat( dataStore.getCollection( Author.class ).count() ).isEqualTo( 1 );
	    assertThat( dataStore.getCollection( Book.class ).count() ).isEqualTo( 1 );        
	}
	
	@Test( expected = VerboseJSR303ConstraintViolationException.class )
	public void testCreateNewBookWithEmptyPublisher() {
	    final Author author = new Author( "Kristina", "Chodorow" );
	         
	    final Book book = new Book( "MongoDB: The Definitive Guide" );
	    book.getAuthors().add( author );
	    book.setPublishedDate( new LocalDate( 2013, 05, 23 ).toDate() );        
	         
	    dataStore.save( author );
	    dataStore.save( book );         
	}
	
	
	@Test
	public void testCreateNewStore() {
	    assertThat( dataStore.getCollection( Author.class ).count() ).isEqualTo( 0 );
	    assertThat( dataStore.getCollection( Book.class ).count() ).isEqualTo( 0 );        
	    assertThat( dataStore.getCollection( Store.class ).count() ).isEqualTo( 0 );
	 
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
	 
	    dataStore.save( author );
	    dataStore.save( book );      
	    dataStore.save( store );
	        
	    assertThat( dataStore.getCollection( Author.class ).count() ).isEqualTo( 1 );
	    assertThat( dataStore.getCollection( Book.class ).count() ).isEqualTo( 1 );        
	    assertThat( dataStore.getCollection( Store.class ).count() ).isEqualTo( 1 );
	}
}
