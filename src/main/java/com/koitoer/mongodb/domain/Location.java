/**
 * 
 */
package com.koitoer.mongodb.domain;

import org.mongodb.morphia.annotations.Property;

/**
 * @author Koitoer
 *
 */
public class Location {

	 @Property private double lon;
	 @Property private double lat;
	    
	    
		/**
		 * 
		 */
		public Location() {
			super();
		}


		/**
		 * @param lon
		 * @param lat
		 */
		public Location(double lon, double lat) {
			super();
			this.lon = lon;
			this.lat = lat;
		}


		/**
		 * @return the lon
		 */
		public double getLon() {
			return lon;
		}


		/**
		 * @param lon the lon to set
		 */
		public void setLon(double lon) {
			this.lon = lon;
		}


		/**
		 * @return the lat
		 */
		public double getLat() {
			return lat;
		}


		/**
		 * @param lat the lat to set
		 */
		public void setLat(double lat) {
			this.lat = lat;
		}
	 
	    
	    
}
