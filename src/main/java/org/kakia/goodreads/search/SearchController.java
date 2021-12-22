/**
 * 
 */
package org.kakia.goodreads.search;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Sudhanshu Sharma
 *
 */
@Controller
@RequestMapping("/search")
public class SearchController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

	private String SEARCH_BASE_URL = "http://openlibrary.org/search.json?";

	private static String ROOT_COVER_URL = "https://covers.openlibrary.org/b/id/";

	private static String NO_COVER_IMAGE = "/images/no-cover-image.png";

	private static int SEARCH_RESULT_LIMIT = 20; 
	
	private final WebClient webClient;

	public SearchController(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl(SEARCH_BASE_URL)
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build())
				.build();
	}

	@GetMapping("/book")
	public String searchByBookTitle(@RequestParam String title, Model model) {
		LOGGER.info("Searching book By Title : " + title);
		SearchResult result = webClient.get().uri("?title=" + title).retrieve().bodyToMono(SearchResult.class).block();
		List<SearchResultBook> books = result.getDocs().stream().limit(SEARCH_RESULT_LIMIT).map(book -> {
			book.setKey(book.getKey().replace("/works/", ""));
			String coverId = NO_COVER_IMAGE;
			if (StringUtils.hasText(book.getCover_i())) {
				coverId = ROOT_COVER_URL + book.getCover_i() + "-M.jpg";
			} 
			book.setCover_i(coverId);
			return book;
		}).collect(Collectors.toList());
		model.addAttribute("searchResult", books);
		LOGGER.info("Searched books By Title : " + books.size());
		return "search_result";
	}
}
