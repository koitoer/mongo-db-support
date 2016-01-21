package com.koitoer.uni.mongodb.students;

import org.mongodb.morphia.annotations.Entity;

/**
 * Created by mauricio.mena on 20/01/2016.
 */
public class Scores {

	private String type;

	private Double score;

	public Double getScore() {
		return score;
	}

	public void setScore(final Double score) {
		this.score = score;
	}

	public String getType() {

		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Override public String toString() {
		return "Scores{" +
				"type='" + type + '\'' +
				", score=" + score +
				'}';
	}
}
