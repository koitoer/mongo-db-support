package com.koitoer.uni.mongodb.web;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by mauricio.mena on 05/01/2016.
 */
public class SparkFreemarkerStyle {

		public static void main(String[] args) {

			final Configuration configuration =  new Configuration();
			configuration.setClassForTemplateLoading(HelloWorldFreeMarker.class, "/");

			Spark.get(new Route("/") {

				@Override
				public Object handle(final Request request, final Response response) {

					StringWriter writer = new StringWriter();

					try {
						Template helloTemplate = configuration.getTemplate("uni/mongodb/web/hello.flt");
						Map<String, Object> helloMap = new HashMap<String, Object>();
						helloMap.put("name", "Koitoer");
						helloTemplate.process(helloMap, writer);
					}
					catch (IOException e) {
						e.printStackTrace();
						halt(500);
					}
					catch (TemplateException e) {
						e.printStackTrace();
						halt(500);
					}

					return writer;
				}
			});


			//http://localhost:4567/fruits
			Spark.get(new Route("/fruits") {

				@Override public Object handle(final Request request, final Response response) {
					StringWriter writer = new StringWriter();
					try {
						Template helloTemplate = configuration.getTemplate("uni/mongodb/web/fruit.flt");
						Map<String, Object> helloMap = new HashMap<String, Object>();
						helloMap.put("fruits", Arrays.asList("apple", "orange", "banana", "peach"));
						helloTemplate.process(helloMap, writer);
					}
					catch (Exception e) {
						e.printStackTrace();
						halt(500);
					}
					return writer;
				}
			});

			//http://localhost:4567/favorite_fruit
			Spark.post(new Route("/favorite_fruit") {

				@Override public Object handle(final Request request, final Response response) {
					final String fruit = request.queryParams("fruit");
					if(fruit == null)
						return "Need to pick one fruit";
					else
						return "Your favorite fruit is " + fruit;
				}
			});

		}

}
