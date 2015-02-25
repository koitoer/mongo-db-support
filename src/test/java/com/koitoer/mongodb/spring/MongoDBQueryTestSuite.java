/**
 * 
 */
package com.koitoer.mongodb.spring;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.TreeSet;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.koitoer.mongodb.domain.Author;
import com.koitoer.mongodb.domain.Book;
import com.koitoer.mongodb.domain.BookTotal;
import com.koitoer.mongodb.domain.Location;
import com.koitoer.mongodb.domain.Publisher;
import com.koitoer.mongodb.domain.Stock;
import com.koitoer.mongodb.domain.Store;
import com.mongodb.AggregationOptions;
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
@ContextConfiguration(locations = { "classpath:com/koitoer/spring/test-applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class MongoDBQueryTestSuite {

	@Autowired
	private MongoTemplate mongoTemplate;
	

	@Before
	public void init() throws UnknownHostException{
		
		mongoTemplate.dropCollection(Author.class);
		mongoTemplate.dropCollection(Book.class);
		mongoTemplate.dropCollection(Store.class);
		
		Author author = new Author("Kristina","Chodorow");
		mongoTemplate.save(author);
		Author author2 = new Author("Rick","Copeland");
		mongoTemplate.save(author2);
		Author author3 = new Author("Kyle","Banker");
		mongoTemplate.save(author3);
		Author author4 = new Author("Pramod J","Sadalage");
		mongoTemplate.save(author4);
			
		Book book1 = new Book("MongoDB: The Definitive Guide");
		book1.setAuthors(Arrays.asList(author));
		book1.setPublishedDate(new LocalDate(2013, 5, 23).toDate());
		book1.setPublisher(new Publisher("O'Reilly"));
		book1.setCategories(new TreeSet<String>(Arrays.asList("Databases", "Programming", "NoSQL")));
		mongoTemplate.save(book1);
		Book book2 = new Book("MongoDB Applied Design Patterns");
		book2.setAuthors(Arrays.asList(author2));
		book2.setPublishedDate(new LocalDate(2013, 5, 19).toDate());
		book2.setPublisher(new Publisher("O'Reilly"));
		book2.setCategories(new TreeSet<String>(Arrays.asList("Databases", "Programming", "NoSQL", "Patterns")));
		mongoTemplate.save(book2);
		Book book3 = new Book("MongoDB in Action");
		book3.setAuthors(Arrays.asList(author3));
		book3.setPublishedDate(new LocalDate(2011, 12, 16).toDate());
		book3.setPublisher(new Publisher("Manning"));
		book3.setCategories(new TreeSet<String>(Arrays.asList("Databases", "Programming", "NoSQL")));
		mongoTemplate.save(book3);
		Book book4 = new Book("NoSQL Distilled");
		book4.setAuthors(Arrays.asList(author4));
		book4.setPublishedDate(new LocalDate(2012, 8, 18).toDate());
		book4.setPublisher(new Publisher("Addison Wesley"));
		book4.setCategories(new TreeSet<String>(Arrays.asList("Databases", "NoSQL")));
		mongoTemplate.save(book4);
		
			
		Store store = new Store("Waterstones Piccadilly");
		store.setLocation(new Location(51.50957,-0.135484));
		store.setStock(Arrays.asList(new Stock(book1, 10), new Stock(book2, 45),new Stock(book3, 2),new Stock(book4, 0)));
		mongoTemplate.save(store);
		
		Store store1 = new Store("Barnes & Noble");
		store1.setLocation(new Location(40.786277,-73.978693));
		store1.setStock(Arrays.asList(new Stock(book1, 7), new Stock(book2, 12),new Stock(book3, 15),new Stock(book4, 2)));
		mongoTemplate.save(store1);
		
		mongoTemplate.indexOps(Store.class).ensureIndex( new GeospatialIndex("location") );

	}
	
	
	@Test
	public void testFindBooksByName() {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("title").is("MongoDB Applied Design Patterns"));
	    List< Book > books = mongoTemplate.find(query, Book.class);
	    assertThat( books ).hasSize( 1 );
	    assertThat( books ).extracting( "title" )
        .containsExactly(
            "MongoDB Applied Design Patterns"
        );
	    
	    query = new Query();
		query.addCriteria(Criteria.where("title").regex("mongodb", "i"));
		query.with(new Sort(Direction.ASC, "title"));
	    books = mongoTemplate.find(query, Book.class);
	    
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
		Query query = new Query();
		query.addCriteria(Criteria.where("authors.lastName").is("Banker"));
		query.with(new Sort(Direction.ASC, "title"));
		
	    final List< Book > books = mongoTemplate.find(query, Book.class);
	 
	    assertThat( books ).hasSize( 1 );
	    assertThat( books ).extracting( "title" ).containsExactly( "MongoDB in Action" );        
	}
	
	
	@Test
	public void testFindBooksByCategoryAndPublishedDate() {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "title"));
		query.addCriteria(new Criteria().andOperator(
				Criteria.where("categories").all(Arrays.asList( "NoSQL", "Databases" )),
				Criteria.where("publishedDate").gt(new LocalDate( 2013, 01, 01 ).toDate())
				));
		
		final List< Book > books = mongoTemplate.find(query, Book.class);       
	     
	    assertThat( books ).hasSize( 2 );
	    assertThat( books ).extracting( "title" )
	        .containsExactly(
	            "MongoDB: The Definitive Guide",
	            "MongoDB Applied Design Patterns"
	        );
	}
	
	
	@Test
	public void testFindStoreClosestToLondon() {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("location").near(new Point( 51.508035, -0.128016))
				.maxDistance(1));
		final List< Store > stores = mongoTemplate.find(query, Store.class); 
		
	    assertThat( stores ).hasSize( 1 );
	    assertThat( stores ).extracting( "name" )
	        .containsExactly( "Waterstones Piccadilly" );
	}
	
	
	@Test
	public void testFindStoreWithEnoughBookQuantity() {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("title").is("MongoDB in Action"));
	    final Book book = mongoTemplate.findOne(query, Book.class);
	    
	    Query query2 = new Query();
	    query2.addCriteria (Criteria.where("stock").
	    		elemMatch(
	    				new Criteria()
	    					.andOperator(Criteria.where("quantity").gt(10),
	    								 Criteria.where("book").is(book))));
	    
	    final List< Store > stores = mongoTemplate.find(query2, Store.class);

	    assertThat( stores ).hasSize( 1 );        
	    assertThat( stores ).extracting( "name" ).containsExactly( "Barnes & Noble" );        
	}
	
	/*
	@Test
	public void testSaveStoreWithNewLocation() {
		
		final Store store = mongoTemplate.findOne(
				new Query().addCriteria(
						Criteria.where("name").is("Waterstones Piccadilly")), Store.class);
	         
	    assertThat( store.getVersion() ).isEqualTo( 1 );
	         
	    store.setLocation( new Location( 50.50957,-0.135484 ) );
	    mongoTemplate.save(store);
	    
	    mongoTemplate.findById(store, Store.class);
	    assertThat( store.getVersion() ).isEqualTo( 2 );        
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
	*/
	@Test
	public void testUpdateStoreLocation() {
		
		WriteResult writerResult = mongoTemplate.updateFirst(
				Query.query(Criteria.where("name").is("Waterstones Piccadilly")), 
				Update.update("location", new Location( 50.50957,-0.135484 )), Store.class);
	    assertThat( writerResult.getN() ).isEqualTo( 1 );        
	}
	

	@Test
	public void testFindStoreWithEnoughBookQuantity2() {
		
		
	    final Book book = mongoTemplate.findOne(Query.query(Criteria.where("title").is("MongoDB in Action")),Book.class);
	    
	    final Store store = mongoTemplate.findAndModify(
	    		Query.query(Criteria.where("stock")
	    				.elemMatch(
	    						new Criteria().andOperator(
	    								Criteria.where("book").is(book),
	    								Criteria.where("quantity").gt(10)
	    								))),
	    		Update.update("stock.$.quantity", -10), 
	    		new FindAndModifyOptions().returnNew(true),
	    		Store.class);
	         
	    assertThat( store ).isNotNull();   
	    assertThat(store.getStock().get(2).getQuantity()).isEqualTo(-10); 
	 
	}
	

	@Test
	public void testDeleteStore() {
	  final WriteResult result = mongoTemplate.remove(
			  	new Query(Criteria.where("name").is("Waterstones Piccadilly")), Store.class);
	  assertThat( result.getN() ).isEqualTo( 1 );      
	               
	}
	

	@Test
	public void testDeleteAllStores() {
	    final WriteResult result = mongoTemplate.remove(new Query(), Store.class);
	    assertThat( result.getN() ).isEqualTo( 2 );                
	}
	

	@Test
	public void testFindAndDeleteBook() {
		Book book = mongoTemplate.findAndRemove(Query.query(
				Criteria.where("title").is("MongoDB in Action")), Book.class);         
	    assertThat( book ).isNotNull();    
	    assertThat( mongoTemplate.count(new Query(), Book.class) ).isEqualTo( 3 );
	}
	

	/**
	 * https://github.com/spring-projects/spring-data-mongodb/blob/master/spring-data-mongodb/
	 * src/test/java/org/springframework/data/mongodb/core/mapreduce/GroupByTests.java
	 * **/
	@Test
	public void testGroupBooksByPublisher() {
	
		GroupByResults<Book> results = mongoTemplate.group(
				"book",
				GroupBy.key("publisher.name").initialDocument(new BasicDBObject("total", 0))
						.reduceFunction("function(curr, result) { result.total += 1 }"), Book.class);
		/*
		db.collection.group({ key, reduce, initial [, keyf] [, cond] [, finalize] })
		Executing Group with DBObject 
			[{ "group" : { 
						//Over what property you will group
						"key" : { "publisher.name" : 1} , 
						 //An aggregation function that operates on the documents during the grouping operation
						 "$reduce" : "function(curr, result) { result.total += 1 }" , 
						 //How to start any var in the query
						 "initial" : { "total" : 0} , 
						 "ns" : "book" , 
						 //If you want to add any conditional on the values
						 "cond" :  null }}]
		*/	
	    assertThat( results.getKeys() ).isEqualTo(3);
	    assertThat( (BasicDBList)results.getRawResults().get("retval")).containsExactly( 
	        new BasicDBObject( "publisher.name", "O'Reilly" ).append( "total", 2.0 ), 
	        new BasicDBObject( "publisher.name", "Manning" ).append( "total", 1.0 ),
	        new BasicDBObject( "publisher.name", "Addison Wesley" ).append( "total", 1.0 ) 
	    );
	}
	
	
	/**
	 * https://github.com/spring-projects/spring-data-mongodb/blob/master/spring-data-mongodb/src/test/java/
	 * org/springframework/data/mongodb/core/aggregation/AggregationTests.java
	 * **/
	@Test
	public void testGroupBooksByCategories() {
		
		Aggregation aggregation = Aggregation.newAggregation(
					Aggregation.project("categories"),
					Aggregation.unwind("categories"),
					Aggregation.group("categories").count().as("count"));
	
		/*		Executing aggregation: 
		{ "aggregate" : "book" , 
		  "pipeline" : [    
		  					//Passes along the documents with only the specified fields to the next stage in the pipeline
		  					{ "$project" : { "categories" : 1}} ,
		   					//Deconstructs an array field from the input documents to output a document for each element
							{ "$unwind" : "$categories"} , 
							{ "$group" : { "_id" : "$categories" , "count" : { "$sum" : 1}}}
						]}
		*/

		AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregation, "book", DBObject.class);

		   assertThat( results.getMappedResults()).containsExactly( 
				   new BasicDBObject( "_id", "Patterns" ).append( "count", 1 ),
			        new BasicDBObject( "_id", "Programming" ).append( "count", 3 ),
			        new BasicDBObject( "_id", "NoSQL" ).append( "count", 4 ),            
			        new BasicDBObject( "_id", "Databases" ).append( "count", 4 )  
			    );

	}
}
