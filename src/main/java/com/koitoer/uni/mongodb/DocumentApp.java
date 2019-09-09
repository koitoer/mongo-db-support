package com.koitoer.uni.mongodb;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;

import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

/**
 * Document and the print method.
 * Created by mauricio.mena on 01/12/2015.
 */
public class DocumentApp {

	public static void main (String[] args){

		Document document = new Document().append("str", "MongoDB, hello")
				.append("int", 42)
				.append("l", 1L)
				.append("double",1.1)
				.append("b",false)
				.append("date", new Date())
				.append("objectId", new ObjectId())
				.append("null", null)
				.append("embeddedDoc", new Document("x",0))
				.append("list", Arrays.asList(1, 2, 3));

		String string = (String)document.get("str");
		string = document.getString("str");

		printJson(document);
	}

	public static void printJson(final Document document) {
		JsonWriter jsonWriter = new JsonWriter(new StringWriter(), new JsonWriterSettings(JsonMode.SHELL, false));
		new DocumentCodec().encode(jsonWriter, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
		System.out.println(jsonWriter.getWriter());
		System.out.flush();
	}

}
