package com.koitoer.uni.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by mauricio.mena on 01/12/2015.
 * Connection with mongo driver to mongo DB.
 */
public class ExamApp {

	public static void main (String[] args){
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("test");

		//BsonDocument, Document are other Generic classes for MongoCollection
		MongoCollection<Document> animals = db.getCollection("animals");

		Document animal = new Document("animal", "monkey");

		animals.insertOne(animal);
		animal.remove("animal");
		animal.append("animal", "cat");
		animals.insertOne(animal);
		animal.remove("animal");
		animal.append("animal", "lion");
		animals.insertOne(animal);
	}

	private static void printJson(final Document document) {
		JsonWriter jsonWriter = new JsonWriter(new StringWriter(), new JsonWriterSettings(JsonMode.SHELL, false));
		new DocumentCodec().encode(jsonWriter, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
		System.out.println(jsonWriter.getWriter());
		System.out.flush();
	}

}
