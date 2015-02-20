/**
 * 
 */
package com.koitoer.mongodb;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;

import com.koitoer.mongodb.domain.Author;
import com.koitoer.mongodb.domain.Book;
import com.koitoer.mongodb.domain.Location;
import com.koitoer.mongodb.domain.Publisher;
import com.koitoer.mongodb.domain.Stock;
import com.koitoer.mongodb.domain.Store;
import com.mongodb.MongoClient;

import static org.fest.assertions.api.Assertions.*;
/**
 * @author Koitoer
 *
 */
public class MongoDBQueryTestSuite {

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
			
			Author author = new Author("Kristina","Chodorow");
			dataStore.save(author);
			Author author2 = new Author("Rick","Copeland");
			dataStore.save(author2);
			Author author3 = new Author("Kyle","Banker");
			dataStore.save(author3);
			Author author4 = new Author("Pramod J","Sadalage");
			dataStore.save(author4);
			
			Book book1 = new Book("MongoDB: The Definitive Guide");
			book1.setAuthors(Arrays.asList(author));
			book1.setPublishedDate(new LocalDate(2013, 5, 23).toDate());
			book1.setPublisher(new Publisher("O’Reilly"));
			book1.setCategories(new TreeSet<String>(Arrays.asList("Databases", "Programming", "NoSQL")));
			dataStore.save(book1);
			Book book2 = new Book("MongoDB Applied Design");
			book2.setAuthors(Arrays.asList(author2));
			book2.setPublishedDate(new LocalDate(2013, 5, 19).toDate());
			book2.setPublisher(new Publisher("O’Reilly"));
			book2.setCategories(new TreeSet<String>(Arrays.asList("Databases", "Programming", "NoSQL", "Patterns")));
			dataStore.save(book2);
			Book book3 = new Book("MongoDB in Action");
			book3.setAuthors(Arrays.asList(author3));
			book3.setPublishedDate(new LocalDate(2011, 12, 16).toDate());
			book3.setPublisher(new Publisher("Manning"));
			book3.setCategories(new TreeSet<String>(Arrays.asList("Databases", "Programming", "NoSQL")));
			dataStore.save(book3);
			Book book4 = new Book("NoSQL Distilled");
			book4.setAuthors(Arrays.asList(author4));
			book4.setPublishedDate(new LocalDate(2012, 8, 18).toDate());
			book4.setPublisher(new Publisher(" Addison Wesley "));
			book4.setCategories(new TreeSet<String>(Arrays.asList("Databases", "NoSQL")));
			dataStore.save(book4);
			
			
			Store store = new Store("Waterstones Piccadilly");
			store.setLocation(new Location(51.50957,-0.135484));
			store.setStock(Arrays.asList(new Stock(book1, 10), new Stock(book2, 45),new Stock(book3, 2),new Stock(book4, 0)));
			dataStore.save(store);
			
			Store store1 = new Store("Barnes & Noble");
			store1.setLocation(new Location(40.786277,-73.978693));
			store1.setStock(Arrays.asList(new Stock(book1, 7), new Stock(book2, 12),new Stock(book3, 15),new Stock(book4, 2)));
			dataStore.save(store1);
		}
	}
	
	
	@Test
	public void testFindBooksByName() {
	    final List< Book > books = dataStore.createQuery( Book.class )
	        .field( "title" ).containsIgnoreCase( "mongodb" )
	        .order( "title" )
	        .asList();
	         
	    assertThat( books ).hasSize( 3 );

	}
	
}
