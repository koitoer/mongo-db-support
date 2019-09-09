package com.koitoer.uni.mongodb;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

/**
 * Created by mauricio.mena on 01/12/2015.
 * Connection with mongo driver to mongo DB.
 */
public class MongoHomeWork1 {

	public static void main (String[] args){
		MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
		MongoClient client = new MongoClient(new ServerAddress(), options);
		MongoDatabase db = client.getDatabase("students").withReadPreference(ReadPreference.secondary());

		//BsonDocument, Document are other Generic classes for MongoCollection
		MongoCollection<Document> collection = db.getCollection("grades");
		Bson sort = new Document("student_id",1).append("score", 1);
		Bson sort2 = Sorts.orderBy(Sorts.ascending("student_id"), Sorts.descending("score"));
		Bson filter = new Document("type","homework");
		ArrayList<Document> documents = collection.find(filter).sort(sort2).into(new ArrayList<Document>());
		Document lastDocument = null;
		int currentId = -1;
		for(Document document : documents) {
			/* This will always any elements before the first one, in that case works as there always two elements.
			if(currentId == document.getInteger("student_id").intValue()){
				lastDocument = document;
				MongoHomeWork1.printJson(document);
				collection.deleteOne(document);
			}else{
				currentId = document.getInteger("student_id");
			}*/
			MongoHomeWork1.printJson(document);
		}
	}

	private static void printJson(final Document document) {
		JsonWriter jsonWriter = new JsonWriter(new StringWriter(), new JsonWriterSettings(JsonMode.SHELL, false));
		new DocumentCodec().encode(jsonWriter, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
		System.out.println(jsonWriter.getWriter());
		System.out.flush();
	}

}
