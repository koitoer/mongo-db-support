package com.koitoer.git.mongodb;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Version;
import org.mongodb.morphia.utils.IndexDirection;

/**
 * Created by mauricio.mena on 18/12/2015.
 */
@Entity("orgs")
public class Organization {

	@Id
	public String name;

	@Indexed(value = IndexDirection.ASC, name = "", unique = false, dropDups = false, expireAfterSeconds = -1,
		background = false, sparse = false	)
	public Date created;

	@Version("v") //long as the required value
	public long version;

	public Organization() {
	}

	public Organization(final String name) {
		this.name = name;
	}
}
