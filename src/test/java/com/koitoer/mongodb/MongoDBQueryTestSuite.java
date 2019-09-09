/**
 * 
 */
package com.koitoer.mongodb;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.TreeSet;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateResults;

import com.koitoer.mongodb.domain.Author;
import com.koitoer.mongodb.domain.Book;
import com.koitoer.mongodb.domain.Location;
import com.koitoer.mongodb.domain.Publisher;
import com.koitoer.mongodb.domain.Stock;
import com.koitoer.mongodb.domain.Store;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

import static org.assertj.core.api.Assertions.*;
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
			book1.setPublisher(new Publisher("O'Reilly"));
			book1.setCategories(new TreeSet<String>(Arrays.asList("Databases", "Programming", "NoSQL")));
			dataStore.save(book1);
			Book book2 = new Book("MongoDB Applied Design Patterns");
			book2.setAuthors(Arrays.asList(author2));
			book2.setPublishedDate(new LocalDate(2013, 5, 19).toDate());
			book2.setPublisher(new Publisher("O'Reilly"));
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
			book4.setPublisher(new Publisher("Addison Wesley"));
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
	    assertThat( books ).extracting( "title" )
	        .containsExactly(
	            "MongoDB Applied Design Patterns",
	            "MongoDB in Action",
	            "MongoDB: The Definitive Guide"
	        );
	}
	
	@Test
	public void testFindBooksByAuthor() {
	    final Author author = dataStore.createQuery( Author.class )
	        .filter( "lastName =", "Banker" )
	        .get();
	         
	    final List< Book > books = dataStore.createQuery( Book.class )
	        .field( "authors" ).hasThisElement( author )
	        .order( "title" )
	        .asList();
	         
	    assertThat( books ).hasSize( 1 );
	    assertThat( books ).extracting( "title" ).containsExactly( "MongoDB in Action" );        
	}
	
	
	@Test
	public void testFindBooksByCategoryAndPublishedDate() {
	    final Query< Book > query = dataStore.createQuery( Book.class ).order( "-title" );        
	         
	    query.and(
	        query.criteria( "categories" )
	            .hasAllOf( Arrays.asList( "NoSQL", "Databases" ) ),
	        query.criteria( "published" )
	            .greaterThan( new LocalDate( 2013, 01, 01 ).toDate() )
	    );
	             
	    final List< Book > books =  query.asList();        
	    assertThat( books ).hasSize( 2 );
	    assertThat( books ).extracting( "title" )
	        .containsExactly(
	            "MongoDB: The Definitive Guide",
	            "MongoDB Applied Design Patterns"
	        );
	}
	
	@Test
	public void testFindStoreClosestToLondon() {
	    final List< Store > stores = dataStore                 
	        .createQuery( Store.class )
	        .field( "location" ).near( 51.508035, -0.128016, 1.0 )
	        .asList();
	 
	    assertThat( stores ).hasSize( 1 );
	    assertThat( stores ).extracting( "name" )
	        .containsExactly( "Waterstones Piccadilly" );
	}
	
	@Test
	public void testFindStoreWithEnoughBookQuantity() {
	    final Book book = dataStore.createQuery( Book.class )
	        .field( "title" ).equal( "MongoDB in Action" )
	        .get();
	             
	    final List< Store > stores = dataStore
	        .createQuery( Store.class )
	        .field( "stock" ).hasThisElement( 
	            dataStore.createQuery( Stock.class )
	                .field( "quantity" ).greaterThan( 10 )
	                .field( "book" ).equal( book )
	                .getQueryObject() )
	        .retrievedFields( true, "name" )
	        .asList();
	             
	    assertThat( stores ).hasSize( 1 );        
	    assertThat( stores ).extracting( "name" ).containsExactly( "Barnes & Noble" );        
	}
	
	
	@Test
	public void testSaveStoreWithNewLocation() {
	    final Store store = dataStore
	        .createQuery( Store.class )
	        .field( "name" ).equal( "Waterstones Piccadilly" )
	        .get();
	         
	    assertThat( store.getVersion() ).isEqualTo( 1 );
	         
	    store.setLocation( new Location( 50.50957,-0.135484 ) );
	    final Key< Store > key = dataStore.save( store );               
	         
	    final Store updated = dataStore.getByKey( Store.class, key );
	    assertThat( updated.getVersion() ).isEqualTo( 2 );        
	}
	
	@Test( expected = ConcurrentModificationException.class )
	public void testSaveStoreWithConcurrentUpdates() {
	    final Query< Store > query = dataStore
	        .createQuery( Store.class )
	        .filter( "name =", "Waterstones Piccadilly" );
	         
	    final Store store1 = query.get();        
	    final Store store2 = query.cloneQuery().get();
	         
	    store1.setName( "New Store 1" );
	    dataStore.save( store1 );    
	    assertThat( store1.getName() ).isEqualTo( "New Store 1" );   
	         
	    store2.setName( "New Store 2" );
	    dataStore.save( store2 );                              
	}
	
	@Test
	public void testUpdateStoreLocation() {
	    final UpdateResults results = dataStore.update(
	        dataStore
	            .createQuery( Store.class )
	            .field( "name" ).equal( "Waterstones Piccadilly" ),             
	        dataStore
	            .createUpdateOperations( Store.class )
	            .set( "location", new Location( 50.50957,-0.135484 ) )
	    );
	         
	    assertThat( results.getUpdatedCount() ).isEqualTo( 1 );        
	}
	
	@Test
	public void testFindStoreWithEnoughBookQuantity2() {
	    final Book book = dataStore.createQuery( Book.class )
	        .field( "title" ).equal( "MongoDB in Action" )
	        .get();
	         
	    final Store store = dataStore.findAndModify( 
	        dataStore
	            .createQuery( Store.class )
	            .field( "stock" ).hasThisElement( 
	                dataStore.createQuery( Stock.class )
	                    .field( "quantity" ).greaterThan( 10 )
	                    .field( "book" ).equal( book )
	                    .getQueryObject() ),             
	        dataStore
	            .createUpdateOperations( Store.class )
	            .disableValidation()
	            .inc( "stock.$.quantity", -10 ),
	        false
	    );
	         
	    assertThat( store ).isNotNull();       
	}
	
	
	@Test
	public void testDeleteStore() {
	    final Store store = dataStore
	        .createQuery( Store.class )
	        .field( "name" ).equal( "Waterstones Piccadilly" )
	        .get();
	         
	    final WriteResult result = dataStore.delete( store );
	    assertThat( result.getN() ).isEqualTo( 1 );                
	}
	
	
	@Test
	public void testDeleteAllStores() {
	    final WriteResult result = dataStore.delete( dataStore.createQuery( Store.class ) );
	    assertThat( result.getN() ).isEqualTo( 2 );                
	}
	
	@Test
	public void testFindAndDeleteBook() {
	    final Book book = dataStore.findAndDelete( 
	        dataStore
	            .createQuery( Book.class )
	            .field( "title" ).equal( "MongoDB in Action" )
	        );
	         
	    assertThat( book ).isNotNull();                
	    assertThat( dataStore.getCollection( Book.class ).count() ).isEqualTo( 3 );
	}
	
	@Test
	public void testGroupBooksByPublisher() {
	    final DBObject result = dataStore
	        .getCollection( Book.class )
	        .group(
	            new BasicDBObject( "publisher.name", "1" ),
	            new BasicDBObject(),
	            new BasicDBObject( "total", 0 ),
	            "function ( curr, result ) { result.total += 1 }"
	        );        
	    assertThat( result ).isInstanceOf( BasicDBList.class );
	                          
	    final BasicDBList groups = ( BasicDBList )result;
	    assertThat( groups ).hasSize( 3 );        
	    assertThat( groups ).containsExactly( 
	        new BasicDBObject( "publisher.name", "O'Reilly" ).append( "total", 2.0 ), 
	        new BasicDBObject( "publisher.name", "Manning" ).append( "total", 1.0 ),
	        new BasicDBObject( "publisher.name", "Addison Wesley" ).append( "total", 1.0 ) 
	    );
	}
	
	@Test
	public void testGroupBooksByCategories() {
	    final DBCollection collection = dataStore.getCollection( Book.class );
	         
	    final AggregationOutput output = collection
	        .aggregate(
	            Arrays.< DBObject >asList(
	                new BasicDBObject( "$project", new BasicDBObject( "title", 1 )
	                   .append( "categories", 1 ) ),
	                new BasicDBObject( "$unwind", "$categories"),
	                new BasicDBObject( "$group", new BasicDBObject( "_id", "$categories" )
	                   .append( "count", new BasicDBObject( "$sum", 1 ) ) )
	            )               
	        );
	         
	    assertThat( output.results() ).hasSize( 4 );
	    assertThat( output.results() ).containsExactly(              
	        new BasicDBObject( "_id", "Patterns" ).append( "count", 1 ),
	        new BasicDBObject( "_id", "Programming" ).append( "count", 3 ),
	        new BasicDBObject( "_id", "NoSQL" ).append( "count", 4 ),            
	        new BasicDBObject( "_id", "Databases" ).append( "count", 4 )               
	    );
	}
}
