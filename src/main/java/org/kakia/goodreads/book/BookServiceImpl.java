/**
 * 
 */
package org.kakia.goodreads.book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kakia.goodreads.author.AuthorServiceImpl;
import org.kakia.goodreads.search.SearchResultBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Sudhanshu Sharma
 *
 */
@Service
public class BookServiceImpl implements BookService {

	private final static Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorServiceImpl authorService;

	private final WebClient webClient;

	private String SEARCH_BASE_URL = "https://openlibrary.org";
	
	private static String ROOT_COVER_URL = "https://covers.openlibrary.org/b/id/";

	private static String NO_COVER_IMAGE = "/images/no-cover-image.png";

	public BookServiceImpl(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl(SEARCH_BASE_URL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@Override
	public List<SearchResultBook> loadAllBooks() {
		LOGGER.info("Loading all Books called");
		
		Slice<Book> sliceBooks = bookRepository.findAll(CassandraPageRequest.of(0, 100));
		List<Book> books = sliceBooks.getContent();
		List<SearchResultBook> resultBooks = new ArrayList<>(books.size());
		books.forEach(book -> resultBooks.add(convertToSearchReasultBook(book)));
		LOGGER.info("Books : " + books.size());
		return resultBooks;
	}

	private SearchResultBook convertToSearchReasultBook(Book book) {
		SearchResultBook resultBook = new SearchResultBook();
		resultBook.setKey(book.getId());
		resultBook.setTitle(book.getTitle());
		resultBook.setFirst_publish_year(book.getPublishedDate().toString());
		resultBook.setAuthor_name(book.getAuthorNameList());
		String cover_i = book.getCoverImageList()==null?null:book.getCoverImageList().get(0);
		String coverId = NO_COVER_IMAGE;
		if (StringUtils.hasText(cover_i)) {
			coverId = ROOT_COVER_URL + cover_i + "-M.jpg";
		} 
		resultBook.setCover_i(coverId);
		return resultBook;
	}

	private List<SearchResultBook> convertToSearchReasultBook(List<Book> books){
		List<SearchResultBook> searchResultBooks = Lists.newArrayList();
		books.stream().forEach(book -> searchResultBooks.add(convertToSearchReasultBook(book)));
		return searchResultBooks;
	}

	@Override
	public Optional<Book> loadBookById(String id) {
		LOGGER.info("Loading Book for Id : " + id);
		Optional<Book> optionalBook = bookRepository.findById(id);
		if (!optionalBook.isPresent()) {
			try {
				String result = webClient.get().uri("/works/" + id + ".json").retrieve().bodyToMono(String.class)
						.block();
				Book book = loadWorkData(new JSONObject(result));
				bookRepository.save(book);
				LOGGER.info("Book Entity Saved - " + book.getId());
				return Optional.of(book);
			} catch (Exception e) {
				LOGGER.info("Book Entity Failed - " + id);
				LOGGER.error(e.getMessage());
			}
		}
		return optionalBook;
	}

	private Book loadWorkData(JSONObject jsonWork) throws Exception {
		if (jsonWork.optString("key") == null) {
			throw new Exception("Fetched JSON does not have key");
		}
		Book book = new Book();
		book.setId(jsonWork.optString("key").replace("/works/", ""));
		book.setTitle(jsonWork.optString("title"));
		book.setSubtitle(jsonWork.optString("subtitle"));
		JSONObject jsonDescription = jsonWork.optJSONObject("description");
		if (jsonDescription != null) {
			book.setDescription(jsonDescription.optString("value"));
		}
		JSONObject jsonPublishedDate = jsonWork.optJSONObject("created");
		if (jsonPublishedDate != null) {
			LocalDate publishedDate = LocalDate
					.from(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(jsonPublishedDate.optString("value")));
			book.setPublishedDate(publishedDate);
		}

		JSONArray jsonCoverIds = jsonWork.optJSONArray("covers");
		if (jsonCoverIds != null) {
			List<String> coverIds = new ArrayList<>(jsonCoverIds.length());
			for (int i = 0; i < jsonCoverIds.length(); i++) {
				coverIds.add(jsonCoverIds.getString(i));
			}
			book.setCoverImageList(coverIds);
		}

		JSONArray jsonAuthorIdList = jsonWork.optJSONArray("authors");
		if (jsonAuthorIdList != null) {
			List<String> authorIdList = new ArrayList<>(jsonAuthorIdList.length());
			for (int i = 0; i < jsonAuthorIdList.length(); i++) {
				authorIdList.add(jsonAuthorIdList.getJSONObject(i).optJSONObject("author").getString("key")
						.replace("/authors/", ""));
			}
			book.setAuthorIdList(authorIdList);
			List<String> authorNameList = authorIdList.stream().map(authorId -> authorService.loadAuthorById(authorId))
					.map(optionalAuthor -> {
						if (optionalAuthor.isPresent())
							return optionalAuthor.get().getName();
						return "Unknown Author";
					}).collect(Collectors.toList());
			book.setAuthorNameList(authorNameList);
		}
		return book;
	}

	@Override
	public List<SearchResultBook> loadAllUserBooks(List<String> bookIds) {
		List<Book> books = bookRepository.findAllById(bookIds);
		return convertToSearchReasultBook(books);
	}

}
