package com.koitoer.uni.mongodb.web;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Just a hello world to introduce spark framework
 * Created by mauricio.mena on 01/12/2015.
 */
public class HelloWorldSpark {

	//http://localhost:4567/
	public static void main(String[] args) {
		Spark.get(new Route("/") {

			@Override public Object handle(final Request request, final Response response) {
				return "Hello World from Spark";
			}
		});

	}

}
