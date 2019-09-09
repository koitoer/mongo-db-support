package com.koitoer.uni.mongodb.aggregation;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bson.Document.*;

/**
 * Created by koitoer on 2/7/16.
 */
public class ZipCodeAggregation {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database =  client.getDatabase("course");
        MongoCollection<Document> collection = database.getCollection("zipcodes");

        List<Document> pipeline ;

        pipeline = Arrays.asList(
                    new Document("group", new Document("_id", "$state"))
                            .append("totalPop", new Document("$sum", "$pop")),
                    new Document("$match", new Document("totalPop", new Document("$gte", 10000000))));

        //pipeline = Arrays.asList(
        //            Document.parse()
        //);

        List<Document> results = collection.find(new Document()).into(new ArrayList<Document>());

        for(Document cur : results){
            System.out.println(cur.toString());
        }
    }
}
