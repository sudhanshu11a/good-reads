/**
 * 
 */
package org.kakia.goodreads.book;

import java.util.List;
import java.util.Optional;

import org.kakia.goodreads.search.SearchResultBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sudhanshu Sharma
 *
 */
@Controller
@RequestMapping()
public class BookController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BookController.class);

	private static String ROOT_COVER_URL = "https://covers.openlibrary.org/b/id/";

	private static String NO_COVER_IMAGE = "/images/no-cover-image.png";

	@Autowired
	private BookService bookService;

	@GetMapping("/book/{bookId}")
	public String getBookById(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principal) {
		LOGGER.info("getBookById called for book ID " + bookId);
		Optional<Book> optionalBook = bookService.loadBookById(bookId);
		if (optionalBook.isPresent()) {
			Book book = optionalBook.get();
			String coverImage = NO_COVER_IMAGE;
			if (book.getCoverImageList() != null && !book.getCoverImageList().isEmpty()) {
				coverImage = ROOT_COVER_URL + book.getCoverImageList().get(0) + "-M.jpg";
			}
			model.addAttribute("coverImage", coverImage);
			model.addAttribute("book", optionalBook.get());
			if (!(principal == null || principal.getAttribute("login") == null)) {
				model.addAttribute("loginId", principal.getAttribute("login"));
			}
			return "book";
		}

		return "book_not_found";
	}

	@GetMapping("/")
	public String getAllBooks(Model model) {
		List<SearchResultBook> books = bookService.loadAllBooks();
		model.addAttribute("searchResult", books);
		return "search_result";
	}

}
