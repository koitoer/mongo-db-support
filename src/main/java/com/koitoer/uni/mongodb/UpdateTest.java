package com.koitoer.uni.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by mauricio.mena on 01/12/2015.
 */
public class UpdateTest {

	public static void main (String[] args) {

		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("course").withReadPreference(ReadPreference.secondary());
		MongoCollection<Document> collection = db.getCollection("test");
		collection.dropCollection();

		for (int i = 0; i < 10; i++) {
				collection.insertOne(new Document().append("_id",i).append("x",i));
		}

		List<Document> all = collection.find().into(new ArrayList<Document>());

		for(Document current : all){
			DocumentApp.printJson(current);
		}

		collection.replaceOne(Filters.eq("x",5), new Document("_id",5).append("x",20).append("update",true));
		all = collection.find().into(new ArrayList<Document>());

		for(Document current : all){
			DocumentApp.printJson(current);
		}

		//Remember upsert will insert if does not exists.
		//command course.$cmd command: update { update: "test", ordered: true,
		// 		updates: [ { q: { _id: 19 }, u: { $set: {x: 20 } }, upsert: true } ]
		collection.updateOne(Filters.eq("_id",19), new Document("$set", new Document("x",20)), new UpdateOptions().upsert(true));
		all = collection.find().into(new ArrayList<Document>());

		for(Document current : all){
			DocumentApp.printJson(current);
		}

		//Update many documents at once, with increment
		collection.updateMany(Filters.gte("_id",5),new Document("$inc", new Document("x",10)));
		all = collection.find().into(new ArrayList<Document>());

		for(Document current : all){
			DocumentApp.printJson(current);
		}

		//Delete the updated elements.
		collection.deleteOne(Filters.eq("_id",9));
		all = collection.find().into(new ArrayList<Document>());

		for(Document current : all){
			DocumentApp.printJson(current);
		}

		//Delete many documents with a filter condition.
		collection.deleteMany(Filters.gte("_id",4));
		all = collection.find().into(new ArrayList<Document>());

		for(Document current : all){
			DocumentApp.printJson(current);
		}


	}
}
