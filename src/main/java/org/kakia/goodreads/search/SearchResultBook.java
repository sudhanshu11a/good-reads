/**
 * 
 */
package org.kakia.goodreads.search;

import java.util.List;

/**
 * @author Sudhanshu Sharma
 *
 */
public class SearchResultBook {

	private String key;

	private String title;

	private String first_publish_year;

	private String cover_i;

	private List<String> author_name;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the first_publish_year
	 */
	public String getFirst_publish_year() {
		return first_publish_year;
	}

	/**
	 * @param first_publish_year the first_publish_year to set
	 */
	public void setFirst_publish_year(String first_publish_year) {
		this.first_publish_year = first_publish_year;
	}

	/**
	 * @return the cover_i
	 */
	public String getCover_i() {
		return cover_i;
	}

	/**
	 * @param cover_i the cover_i to set
	 */
	public void setCover_i(String cover_i) {
		this.cover_i = cover_i;
	}

	/**
	 * @return the author_name
	 */
	public List<String> getAuthor_name() {
		return author_name;
	}

	/**
	 * @param author_name the author_name to set
	 */
	public void setAuthor_name(List<String> author_name) {
		this.author_name = author_name;
	}

}
