/**
 * 
 */
package com.koitoer.mongodb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Version;
import org.mongodb.morphia.utils.IndexDirection;

/**
 * @author Koitoer
 *
 */
@Entity( value = "books", noClassnameStored = true )
public class Book {
	 @Id private ObjectId id;
	    @Version private long version;    
	    @Property @Indexed( IndexDirection.DESC ) @NotEmpty private String title;
	    @Reference @Valid private List< Author > authors = new ArrayList<Author>();
	    @Property( "published" ) @NotNull private Date publishedDate;            
	    @Property( concreteClass = TreeSet.class ) @Indexed
	    private Set< String > categories = new TreeSet<String>();
	    @Embedded @Valid @NotNull private Publisher publisher;
		
	   
	    /**
		 * 
		 */
		public Book() {
			super();
		}



		/**
		 * @param title
		 */
		public Book(String title) {
			super();
			this.title = title;
		}



		/**
		 * @return the id
		 */
		public ObjectId getId() {
			return id;
		}



		/**
		 * @param id the id to set
		 */
		public void setId(ObjectId id) {
			this.id = id;
		}



		/**
		 * @return the version
		 */
		public long getVersion() {
			return version;
		}



		/**
		 * @param version the version to set
		 */
		public void setVersion(long version) {
			this.version = version;
		}



		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}



		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}



		/**
		 * @return the authors
		 */
		public List<Author> getAuthors() {
			return authors;
		}



		/**
		 * @param authors the authors to set
		 */
		public void setAuthors(List<Author> authors) {
			this.authors = authors;
		}



		/**
		 * @return the publishedDate
		 */
		public Date getPublishedDate() {
			return publishedDate;
		}



		/**
		 * @param publishedDate the publishedDate to set
		 */
		public void setPublishedDate(Date publishedDate) {
			this.publishedDate = publishedDate;
		}



		/**
		 * @return the categories
		 */
		public Set<String> getCategories() {
			return categories;
		}



		/**
		 * @param categories the categories to set
		 */
		public void setCategories(Set<String> categories) {
			this.categories = categories;
		}



		/**
		 * @return the publisher
		 */
		public Publisher getPublisher() {
			return publisher;
		}



		/**
		 * @param publisher the publisher to set
		 */
		public void setPublisher(Publisher publisher) {
			this.publisher = publisher;
		}
	    
	    
	    
}
