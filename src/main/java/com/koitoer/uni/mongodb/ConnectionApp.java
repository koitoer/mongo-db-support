package com.koitoer.uni.mongodb;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;

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

/**
 * Created by mauricio.mena on 01/12/2015.
 * Connection with mongo driver to mongo DB.
 */
public class ConnectionApp {

	public static void main (String[] args){
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());

		//BsonDocument, Document are other Generic classes for MongoCollection
		MongoCollection<Document> collection = db.getCollection("test");

		Document document = new Document().append("str", "MongoDB, hello")
				.append("int", 42)
				.append("l", 1L)
				.append("double",1.1)
				.append("b",false)
				.append("date", new Date())
				.append("objectId", new ObjectId())
				.append("null", null)
				.append("embeddedDoc", new Document("x",0))
				.append("list", Arrays.asList(1,2,3));

		String string = (String)document.get("str");
		string = document.getString("str");

		printJson(document);
	}

	private static void printJson(final Document document) {
		JsonWriter jsonWriter = new JsonWriter(new StringWriter(), new JsonWriterSettings(JsonMode.SHELL, false));
		new DocumentCodec().encode(jsonWriter, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
		System.out.println(jsonWriter.getWriter());
		System.out.flush();
	}

}
