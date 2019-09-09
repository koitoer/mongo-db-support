package com.koitoer.uni.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by mauricio.mena on 01/12/2015.
 * Connection with mongo driver to mongo DB.
 */
public class StackOverflowApp {

	public static void main (String[] args){

		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());

		String userId = "myUser2";
		String fileType = "fileType2";
		String fileName = "fileName";
		String fileSize = "fileSize";
		MongoCollection<Document> docs = db.getCollection("users");
		Bson filterQuery = new Document("userId", userId).append("fileType", fileType);
		Bson updateQuery = new Document("$set", new Document("fileType", fileType).append("fileName", fileName).append("fileSize", fileSize).append("userId", userId).append("lastModifiedTimestamp", new Date(System.currentTimeMillis())));
		((Document)updateQuery).append("$setOnInsert", new Document("creationTimestamp", new Date(System.currentTimeMillis())));
		docs.updateOne(filterQuery, updateQuery, (new UpdateOptions()).upsert(true));
	}

	private static void printJson(final Document document) {
		JsonWriter jsonWriter = new JsonWriter(new StringWriter(), new JsonWriterSettings(JsonMode.SHELL, false));
		new DocumentCodec().encode(jsonWriter, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
		System.out.println(jsonWriter.getWriter());
		System.out.flush();
	}

}
