package com.koitoer.uni.mongodb;

import javax.print.Doc;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by mauricio.mena on 01/12/2015.
 */
public class FindTest {

	public static void main (String[] args) {
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("course").withReadPreference(ReadPreference.secondary());

		//BsonDocument, Document are other Generic classes for MongoCollection
		MongoCollection<Document> collection = db.getCollection("test");
		collection.dropCollection();

		for (int i = 0; i < 10; i++) {
			collection.insertOne(new Document("x", i));
		}

		System.out.println("Find one");
		Document first = collection.find().first();
		DocumentApp.printJson(first);

		System.out.println("Find all with into:");
		List<Document> all = collection.find().into(new ArrayList<Document>());
		for (Document current : all){
			DocumentApp.printJson(current);
		}

		System.out.println("Find with iteration");
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document current = cursor.next();
				DocumentApp.printJson(current);
			}
		}finally {
			cursor.close();
		}

		System.out.println("Count");
		Long numberOfDocuments = collection.count();
		System.out.println(numberOfDocuments);
	}
}
