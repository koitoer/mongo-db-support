package com.koitoer.git.mongodb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.testng.annotations.Test;

/**
 * Created by mauricio.mena on 18/12/2015.
 */
public class Demo extends BaseTest{

	private SimpleDateFormat sfd = new SimpleDateFormat("MM-dd-yyyy");
	private GithubUser koitoer;
	private Date date;

	public Demo() throws ParseException {
		date = sfd.parse("07-29-1986");
	}

	@Test
	public void basicUser(){
		koitoer = new GithubUser("koitoer");
		koitoer.fullName = "Koitoer";
		koitoer.memberSince = date;
		koitoer.following = 1000;
		ds.save(koitoer);
	}

	@Test(dependsOnMethods = {"basicUser"})
	public void repositories(){
		Organization organization = new Organization("mongodb");
		ds.save(organization);

		Repository morphia1 = new Repository(organization, "morphia");
		Repository morphia2 = new Repository(koitoer, "morphia");

		ds.save(morphia1);
		ds.save(morphia2);

		koitoer.repositories.add(morphia1);
		koitoer.repositories.add(morphia2);

		ds.save(koitoer);
	}

	@Test(dependsOnMethods = {"repositories"})
	public void query(){

		Query<Repository> query = ds.createQuery(Repository.class);
		Repository repository = query.get(); //findOne
		List<Repository> repositoryList = query.asList(); //findAll

		Iterable<Repository> fetch = query.fetch();
		//((MorphiaIterator)fetch).close();

		Iterator<Repository> iterator = fetch.iterator();
		while(iterator.hasNext()){
			iterator.next();
		}

		//You can start iterator again
		iterator = fetch.iterator();
		while(iterator.hasNext()){
			iterator.next();
		}

		//Java field or mongodb field
		query.field("owner").equal(koitoer).get();

		GithubUser memberSince =  ds.createQuery(GithubUser.class).field("memberSince").equal(date).get();
		System.out.println("memberSince = " + memberSince);
		GithubUser since =  ds.createQuery(GithubUser.class).field("since").equal(date).get();
		System.out.println("since = " + since);
	}


	@Test(dependsOnMethods = {"repositories"})
	public void updates(){
		koitoer.following = 1;
		koitoer.followers = 1000;
		ds.save(koitoer);
	}

	/**
	 *
	 */
	@Test(dependsOnMethods = {"repositories"})
	public void massUpdates(){
		UpdateOperations<GithubUser> update = ds.createUpdateOperations(GithubUser.class).inc("followers").set("following", 42);
		Query<GithubUser> query = ds.createQuery(GithubUser.class).field("followers").equal(0);
		//Update records according to the update operations, bulk operations
		ds.update(query, update);
	}

	/**
	 * Unknown number of changes affect the data and throws the exception
	 */
	@Test(dependsOnMethods = {"repositories"}, expectedExceptions = { ConcurrentModificationException.class})
	public void errorOnVersion(){
		Organization organization =  ds.createQuery(Organization.class).get();
		Organization organization2 =  ds.createQuery(Organization.class).get();
		Assertions.assertThat(organization.version).isEqualTo(1L);
		ds.save(organization);
		Assertions.assertThat(organization.version).isEqualTo(2L);
		ds.save(organization);
		Assertions.assertThat(organization.version).isEqualTo(3L);
		ds.save(organization2);
	}
}
