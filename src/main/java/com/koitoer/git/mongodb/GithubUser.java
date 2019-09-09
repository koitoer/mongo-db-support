package com.koitoer.git.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.*;

/**
 * Created by mauricio.mena on 18/12/2015.
 */
@Entity(value = "users", noClassnameStored = true)
@Indexes({
		@Index(value = "userName, -followers", name = "popular"),
		@Index(value = "lastActive", name = "idle", expireAfterSeconds = 100000000) //TTL index against Date properties
})
public class GithubUser {

	@Id
	public String userName;
	public String fullName;
	@Property("since")
	public Date memberSince;
	public Date lastActive;
	@Reference(lazy = true)
	public List<Repository> repositories = new ArrayList<Repository>();
	public int followers = 0;
	public int following = 0;

	public GithubUser() {
	}

	public GithubUser(final String userName) {
		this.userName =  userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	public Date getMemberSince() {
		return memberSince;
	}

	public void setMemberSince(final Date memberSince) {
		this.memberSince = memberSince;
	}

	public Date getLastActive() {
		return lastActive;
	}

	public void setLastActive(final Date lastActive) {
		this.lastActive = lastActive;
	}

	public List<Repository> getRepositories() {
		return repositories;
	}

	public void setRepositories(final List<Repository> repositories) {
		this.repositories = repositories;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(final int followers) {
		this.followers = followers;
	}

	public int getFollowing() {
		return following;
	}

	public void setFollowing(final int following) {
		this.following = following;
	}
}
