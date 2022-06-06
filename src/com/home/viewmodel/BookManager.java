package com.home.viewmodel;

import java.io.File;

import com.home.model.Book;

public class BookManager {
	static int curId = 0;
	
	public static Book createBook(String title, String author, String genre, String pages, int rate, String description, File address) {
		int pagesInt = 0;
		
		try {
			pagesInt = Integer.parseInt(pages);
		} catch(Exception ex) {
			pagesInt = 0;
		}
		
		Book book = new Book(curId++, title, author, genre, pagesInt, description, rate, address);
	
		return book;
	}
	
	public static Book createBookForReplace(Book book, String title, String author, String genre, String pages, int rate, String description, File address) {
		int pagesInt = 0;
		
		try {
			pagesInt = Integer.parseInt(pages);
		} catch(Exception ex) {
			pagesInt = 0;
		}
		
		Book resBook = new Book(book.getId(), title, author, genre, pagesInt, description, rate, address);
		
		return resBook;
	}
	
	public static Book createBook(int id, String title, String author, String genre, int pages, int rate, String description, File address) {
		Book book = new Book(id, title, author, genre, pages, description, rate, address);
		return book;
	}
	
	public static int getIdFromRecord(String record) {
		return Integer.parseInt(record.substring(0, record.indexOf(" ")));
	}
	
	public static void getDeleted() {
		curId--;
	}
	
	public static String getAddress(File file) {
		String adr = "";
		if(file != null) {
			adr = file.getAbsolutePath();
		}
		return adr;
	}
}
