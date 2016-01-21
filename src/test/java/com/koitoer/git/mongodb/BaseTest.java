package com.koitoer.git.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by mauricio.mena on 18/12/2015.
 */
public class BaseTest {

	MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).build();
	MongoClient client = new MongoClient(new ServerAddress(), options);
	public Datastore ds = new Morphia().createDatastore(client, "school");
}
