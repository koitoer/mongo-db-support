package com.koitoer.uni.mongodb;

import javax.print.Doc;
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
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.expression.spel.ast.Projection;

/**
 * Created by mauricio.mena on 01/12/2015.
 */
public class FindWithSortSkipAndLimit {

	public static void main (String[] args) {

		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("course").withReadPreference(ReadPreference.secondary());

		//BsonDocument, Document are other Generic classes for MongoCollection
		MongoCollection<Document> collection = db.getCollection("test");
		collection.dropCollection();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				collection.insertOne(new Document().append("i",i).append("j",j));
			}
		}

		Bson projection = Projections.fields(Projections.include("i", "j"), Projections.excludeId());

		//Sort document
		Bson sort = new Document("i",1).append("j", -1);
		//Sort using Sorts.
		//Running query: query: {} sort: { i: 1, j: -1 } projection: { i: 1, j: 1, _id: -1 } skip: 0 limit:50
		Bson sort2 = Sorts.orderBy(Sorts.ascending("i"), Sorts.descending("j"));

		List <Document> all = collection.find().projection(projection).sort(sort).limit(50).into(new ArrayList<Document>());

		for(Document current : all){
			DocumentApp.printJson(current);
		}

	}
}
