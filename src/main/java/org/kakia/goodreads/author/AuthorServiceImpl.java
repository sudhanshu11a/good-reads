/**
 * 
 */
package org.kakia.goodreads.author;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Sudhanshu Sharma
 *
 */
@Service
public class AuthorServiceImpl {

	private final static Logger LOGGER = LoggerFactory.getLogger(AuthorServiceImpl.class);

	@Autowired
	private AuthorRepository authorRepository;

	private final WebClient webClient;

	private String SEARCH_BASE_URL = "https://openlibrary.org";

	public AuthorServiceImpl(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl(SEARCH_BASE_URL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	public Optional<Author> loadAuthorById(String id) {
		LOGGER.info("Loading Author for Id : " + id);
		Optional<Author> optionalAuthor = authorRepository.findById(id);
		if (!optionalAuthor.isPresent()) {
			try {
				String result = webClient.get().uri("/authors/" + id + ".json").retrieve().bodyToMono(String.class)
						.block();
				Author author = loadAuthorData(new JSONObject(result));
				authorRepository.save(author);
				LOGGER.info("Author Entity Saved - " + author.getId());
				return Optional.of(author);
			} catch (Exception e) {
				LOGGER.info("Author Entity Failed - " + id);
				LOGGER.error(e.getMessage());
			}
		}
		return optionalAuthor;
	}

	private Author loadAuthorData(JSONObject jsonAuthor) throws Exception {
		if (jsonAuthor.optString("key") == null) {
			throw new Exception("Fetched Author does not have key");
		}
		Author author = new Author();
		author.setId(jsonAuthor.optString("key").replace("/authors/", ""));
		author.setName(jsonAuthor.optString("name"));
		// author.setBio(jsonAuthor.optString("bio"));
		author.setWikipedia(jsonAuthor.optString("wikipedia"));
		String birthDateStr = jsonAuthor.optString("birth_date");
		if (birthDateStr != null && !birthDateStr.isBlank()) {
			LocalDate birthDate = null;
			try {
				if (birthDateStr.length() > 4) {
					DateTimeFormatter dateTimeFormatter = null;
					dateTimeFormatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
					birthDate = LocalDate.parse(birthDateStr, dateTimeFormatter);
				} else {
					MonthDay monthday = MonthDay.of(1, 1);
					birthDate = monthday.atYear(Integer.parseInt(birthDateStr));
				}
			} catch (Exception e) {
				LOGGER.error("Birth date could not be parsed " + birthDateStr);
			}
			author.setBirthDate(birthDate);
		}
		author.setPersnalName(jsonAuthor.optString("personal_name"));
		return author;
	}

}
