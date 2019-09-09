package com.koitoer.git.mongodb;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by mauricio.mena on 18/12/2015.
 */
@Entity("repos")
public class Repository {

	@Id
	public String name;

	@Reference
	public Organization organization;

	@Reference
	public GithubUser owner;

	public Settings settings = new Settings();

	public Repository() {
	}

	public Repository(final String name, final Organization organization) {
		this.name = name;
		this.organization = organization;
	}

	public Repository(final GithubUser owner, final String name) {
		this.owner = owner;
		this.name = name;
	}

	public Repository(final Organization organization, final String name) {
		this.organization = organization;
		this.name = name;
	}
}
