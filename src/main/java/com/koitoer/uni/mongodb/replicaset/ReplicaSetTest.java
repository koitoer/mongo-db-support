package com.koitoer.uni.mongodb.replicaset;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

/**
 * Created by koitoer on 2/10/16.
 */
public class ReplicaSetTest {

    public static void main(String[] args) {
        MongoClient client = new MongoClient(Arrays.asList(new ServerAddress("localhost, 27017"),
                new ServerAddress("localhost, 27018"), new ServerAddress("localhost, 27019")),
                MongoClientOptions.builder().requiredReplicaSetName("replicaSetName").build());

        //If you leave a replica set node out of the seedlist within the driver,
        //The missing node will be discovered as long as you list at least one valid node

        //MongoSocketReadException
    }
}
