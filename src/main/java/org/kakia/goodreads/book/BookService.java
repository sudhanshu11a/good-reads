/**
 * 
 */
package org.kakia.goodreads.book;

import java.util.List;
import java.util.Optional;

import org.kakia.goodreads.search.SearchResultBook;

/**
 * @author Sudhanshu Sharma
 *
 */
public interface BookService {
	
	List<SearchResultBook> loadAllBooks();
	
	Optional<Book> loadBookById(String id);
	
	List<SearchResultBook> loadAllUserBooks(List<String> bookIds);

}
