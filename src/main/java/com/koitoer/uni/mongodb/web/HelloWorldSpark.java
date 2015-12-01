package com.koitoer.uni.mongodb.web;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by mauricio.mena on 01/12/2015.
 */
public class HelloWorldSpark {

	public static void main(String[] args) {
		Spark.get(new Route("/") {

			@Override public Object handle(final Request request, final Response response) {
				return "Hello World from Spark";
			}
		});

	}

}
