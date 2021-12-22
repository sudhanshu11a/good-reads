/**
 * 
 */
package org.kakia.goodreads.user;

import java.util.List;
import java.util.stream.Collectors;

import org.kakia.goodreads.book.BookService;
import org.kakia.goodreads.search.SearchResultBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.stereotype.Service;

/**
 * @author Sudhanshu Sharma
 *
 */
@Service
public class UserBookServiceImpl {
	
	@Autowired
	private UserBookRepository userBookRepository;
	
	@Autowired
	private BookService bookService;

	public UserBook saveUserBookData(UserBook userBook) {
		userBook = userBookRepository.save(userBook);
		return userBook;
	}
	
	public List<SearchResultBook> getAllUserBooks(String id){
		//List<UserBook> userBooks = userBookRepository.findAllByIdUserId(id, CassandraPageRequest.of(0, 20)).getContent();
		List<UserBook> userBooks = userBookRepository.findAllByUserId(id);
		List<String> bookIds = userBooks.stream().map(book -> book.getBookId()).collect(Collectors.toList());
		List<SearchResultBook> resultBooks = bookService.loadAllUserBooks(bookIds);
		return resultBooks;
	}
}
