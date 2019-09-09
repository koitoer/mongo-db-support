/**
 * 
 */
package com.koitoer.mongodb.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.utils.IndexDirection;

/**
 * @author Koitoer
 *
 */
public class Publisher {

	 @Property @Indexed( IndexDirection.DESC ) @NotBlank private String name;

	 
	/**
	 * 
	 */
	public Publisher() {
		super();
	}

	/**
	 * @param name
	 */
	public Publisher(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	 
	 
}
