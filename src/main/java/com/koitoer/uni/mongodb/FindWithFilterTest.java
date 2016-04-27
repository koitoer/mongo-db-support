package com.koitoer.uni.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by mauricio.mena on 01/12/2015.
 */
public class FindWithFilterTest {

	public static void main (String[] args) {
		Logger mongoLogger = Logger.getLogger( "com.mongodb" );
		mongoLogger.setLevel(Level.INFO);

		System.setProperty("DEBUG.MONGO", "true");
		System.setProperty("DB.TRACE", "true");

		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("course").withReadPreference(ReadPreference.secondary());

		//BsonDocument, Document are other Generic classes for MongoCollection
		MongoCollection<Document> collection = db.getCollection("test");
		collection.drop();

		for (int i = 0; i < 10; i++) {
			collection.insertOne(new Document()
						.append("x", new Random().nextInt(2))
					 	.append("y", new Random().nextInt(100))
						.append("i",i));
		}


		//Filter using document
		Bson filter = new Document("x",0)
				.append("y", new Document("$gt", 10).append("$lt", 50));

		//Filter using filter
		filter = Filters.and(Filters.eq("x",0), Filters.gt("y",10), Filters.lt("y",50));

		//Filter documents with projections (field, true/false)
		Bson projection = new Document("x",1).append("_id",0);

		//Projections
		//Running query: query: { x: 0, y:{ $gt: 10, $lt: 50 } } sort: {} projection: { y: -1 } skip: 0 limit: 0
		//db.test.find({ x: 0, y: { $gt: 10, $lt: 50 } }, { y: 1 })
		//Bson projection2 = Projections.exclude("y");

		//Running query: { x: 0, y: { $gt: 10, $lt: 50 } } sort: {} projection: { y: 1 } skip: 0 limit: 0
		//db.test.find({ x: 0, y: { $gt: 10, $lt: 50 } }, { y: -1 })
		//Bson projection2 = Projections.include("y");

		//Running query: query: { x: 0, y: { $gt: 10, $lt: 50 } } sort: {}
		// 		projection: { y: -1, x: -1, i: 1, _id: -1 } skip: 0 limit: 0
		//Bson projection2 = Projections.fields(Projections.exclude("y", "x"), Projections.include("i"), Projections.excludeId());
		Bson projection2 = Projections.fields(Projections.exclude("i", "x"));

		List<Document> all = collection.find(filter).projection(projection).into(new ArrayList<Document>());


		for(Document current : all){
			DocumentApp.printJson(current);
		}

		//long count = collection.count(filter);
		System.out.println();
		//System.out.println(count);

	}
}
