/**
 * 
 */
package com.koitoer.mongodb;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;

import com.koitoer.mongodb.domain.Author;
import com.koitoer.mongodb.domain.Book;
import com.koitoer.mongodb.domain.Store;
import com.mongodb.MongoClient;

/**
 * @author Koitoer
 *
 */
public class InitMongoDB {
	
	public static void main(String args[]) throws UnknownHostException{
		
		final Morphia morphia = new Morphia();
		new ValidationExtension( morphia );
		
		final MongoClient client = new MongoClient( "localhost", 27017 );
		final Datastore dataStore = morphia
		    .map( Store.class, Book.class, Author.class )
		    .createDatastore( client, "bookstore" );
		
		dataStore.ensureIndexes();
		dataStore.ensureCaps();
	}
}
