/**
 * 
 */
package com.koitoer.mongodb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Version;
import org.mongodb.morphia.utils.IndexDirection;


/**
 * @author Koitoer
 *
 */
@Entity( value = "stores", noClassnameStored = true )
public class Store {

	 @Id private ObjectId id;
	 
	     @Version private long version;
	    @Property @Indexed( IndexDirection.DESC ) @NotEmpty private String name;
	    @Embedded @Valid private List< Stock > stock = new ArrayList<Stock>();
	    @Embedded @Indexed( IndexDirection.GEO2D ) private Location location;
		
	    
	    /**
		 * 
		 */
		public Store() {
			super();
		}
		/**
		 * @param name
		 */
		public Store(String name) {
			super();
			this.name = name;
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
		/**
		 * @return the stock
		 */
		public List<Stock> getStock() {
			return stock;
		}
		/**
		 * @param stock the stock to set
		 */
		public void setStock(List<Stock> stock) {
			this.stock = stock;
		}
		/**
		 * @return the location
		 */
		public Location getLocation() {
			return location;
		}
		/**
		 * @param location the location to set
		 */
		public void setLocation(Location location) {
			this.location = location;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Store [id=" + id + ", version=" + version + ", name="
					+ name + ", stock=" + stock + ", location=" + location
					+ "]";
		}
	     
	    
	    
}
