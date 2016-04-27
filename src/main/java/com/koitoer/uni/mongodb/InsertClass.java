package com.koitoer.uni.mongodb;

import javax.print.Doc;

import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by mauricio.mena on 01/12/2015.
 */
public class InsertClass {


	//C:\Program Files\MongoDB\Server\3.0\bin>mongod.exe -dbpath C:\Users\XXXX\Documents\github\mongo\dbdata
	//C:\Program Files\MongoDB\Server\3.0\bin>mongo.exe course
	public static void main (String[] args) {
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("course").withReadPreference(ReadPreference.secondary());

		//BsonDocument, Document are other Generic classes for MongoCollection
		MongoCollection<Document> collection = db.getCollection("test");
		//collection.dropCollection();


		Document smith = new Document("name", "Smith").append("age", 30).append("profession", "programmer");
		DocumentApp.printJson(smith);
		//collection.insertOne(smith);

		Document john = new Document("name", "John").append("age", 33).append("profession", "hacker");
		collection.insertMany(Arrays.asList(smith, john));
		DocumentApp.printJson(smith);


	}
}
