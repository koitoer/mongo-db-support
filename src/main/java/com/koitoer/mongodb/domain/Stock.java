/**
 * 
 */
package com.koitoer.mongodb.domain;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

/**
 * @author Koitoer
 *
 */
public class Stock {

	@Reference @Valid private Book book;
    @Property @Min( 0 ) private int quantity;
    
    
	/**
	 * 
	 */
	public Stock() {
		super();
	}


	/**
	 * @param book
	 * @param quantity
	 */
	public Stock(Book book, int quantity) {
		super();
		this.book = book;
		this.quantity = quantity;
	}


	/**
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}


	/**
	 * @param book the book to set
	 */
	public void setBook(Book book) {
		this.book = book;
	}


	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}


	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
	
	
    
}
