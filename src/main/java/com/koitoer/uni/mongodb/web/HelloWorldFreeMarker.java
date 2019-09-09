package com.koitoer.uni.mongodb.web;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Created by mauricio.mena on 05/01/2016.
 */
public class HelloWorldFreeMarker {

	public static void main(String[] args) {
		Configuration configuration =  new Configuration();
		configuration.setClassForTemplateLoading(HelloWorldFreeMarker.class, "/");

		try {
			Template helloTemplate = configuration.getTemplate("uni/mongodb/web/hello.flt");
			StringWriter writer = new StringWriter();
			Map<String, Object> helloMap = new HashMap<String, Object>();
			helloMap.put("name", "Koitoer");
			helloTemplate.process(helloMap, writer);
			System.out.println(writer);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (TemplateException e) {
			e.printStackTrace();
		}
	}

}
