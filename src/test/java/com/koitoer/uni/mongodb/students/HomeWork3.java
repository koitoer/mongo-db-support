package com.koitoer.uni.mongodb.students;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import com.koitoer.git.mongodb.BaseTest;
import com.koitoer.git.mongodb.GithubUser;
import com.koitoer.git.mongodb.Repository;
import org.mongodb.morphia.query.Query;
import org.testng.annotations.Test;

/**
 * Created by mauricio.mena on 20/01/2016.
 */
public class HomeWork3 extends BaseTest {

	/**
	 * Remove for each student the minimum score in the homework type.
	 */
	@Test
	public void homeWork31(){
		Query<Student> query = ds.createQuery(Student.class);
		Iterator<Student> iterator =  query.fetch().iterator();
		while(iterator.hasNext()){
			Student student1 = iterator.next();
			List<Scores> scoresPerStudent = student1.getScores();

			//Filter to get only the homework type
			Predicate<Scores> predicate = new Predicate<Scores>() {
				@Override
				public boolean apply(Scores input) {
					if(input.getType().equals("homework"))
						return true;
					else
						return false;
				}
			};

			Collection<Scores> result = Collections2.filter(scoresPerStudent, predicate);

			//Order to find the minimum and remove it
			Ordering<Scores> o = new Ordering<Scores>() {
				@Override
				public int compare(Scores left, Scores right) {
					return Doubles.compare(left.getScore(), right.getScore());
				}
			};
			Scores minimum = o.min(result);
			scoresPerStudent.remove(minimum);
			ds.save(student1);
		}

	}
}
