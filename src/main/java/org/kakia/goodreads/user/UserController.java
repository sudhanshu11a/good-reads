/**
 * 
 */
package org.kakia.goodreads.user;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.kakia.goodreads.search.SearchResultBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Sudhanshu Sharma
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserBookServiceImpl userBookService;

	@RequestMapping("/name")
	public String getUser(@AuthenticationPrincipal OAuth2User principal) {
		//authorization.expression('isAuthenticated()

		LOGGER.info("Login... " + principal);
		LOGGER.info(principal.getAttribute("name"));
		return "redirect:book";
	}

	@PostMapping("/book")
	public ModelAndView saveUserBook(@RequestBody MultiValueMap<String, String> formData,
			@AuthenticationPrincipal OAuth2User principal) {
		LOGGER.info("saveUserBookRating... " + principal);
		if (principal == null || principal.getAttribute("login") == null) {
			return null;
		}
		String bookId = formData.getFirst("bookId");
		LocalDate startDate = LocalDate.parse(formData.getFirst("startDate"));
		int rating = Integer.parseInt(formData.getFirst("bookRating"));
		LOGGER.info(bookId + startDate + rating);
		UserBook userBook = new UserBook();
		userBook.setUserId(principal.getAttribute("email"));
		userBook.setBookId(bookId);
		userBook.setTimeUuid(Uuids.timeBased());
		userBook.setStartDate(startDate);
		userBook.setRating(rating);
		userBook = userBookService.saveUserBookData(userBook);
		return new ModelAndView("redirect:/book/" + userBook.getBookId());
	}

	@GetMapping("/books")
	public String getAllUserBooks(Model model, @AuthenticationPrincipal OAuth2User principal) {
		LOGGER.info("getAllUserBooks... " + principal);
		if (principal == null || principal.getAttribute("login") == null) {
			return "redirect:book";
		}
		String userId = principal.getAttribute("email");
		List<SearchResultBook> books = userBookService.getAllUserBooks(userId);
		model.addAttribute("searchResult", books);
		LOGGER.info("Searched books By Title : " + books.size());
		return "search_result";
	}
}
