package com.koitoer.uni.mongodb.students;

import java.util.Arrays;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by mauricio.mena on 20/01/2016.
 */
@Entity(value = "students", noClassnameStored = true)
public class Student {

	@Id private int id;

	private String name;

	private List<Scores> scores;

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Scores> getScores() {
		return scores;
	}

	public void setScores(final List<Scores> scores) {
		this.scores = scores;
	}

	@Override public String toString() {
		return "Student{" +
				"id=" + id +
				", name='" + name + '\'' +
				", scores=" + scores +
				'}';
	}
}
