/**
 * 
 */
package org.kakia.goodreads.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

/**
 * @author Sudhanshu Sharma
 *
 */
@Table(value = "user_book")
public class UserBook implements Serializable {

	@Id
	@PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@CassandraType(type = Name.TIMEUUID)
	private UUID timeUuid;

	@Column("user_id")
	@Indexed("user_id")
	@CassandraType(type = Name.TEXT)
	private String userId;

	@Column("book_id")
	@CassandraType(type = Name.TEXT)
	private String bookId;

    @Column("start_date")
    @CassandraType(type = Name.DATE)
	private LocalDate startDate;
	
    @Column("rating")
    @CassandraType(type = Name.INT)
	private int rating;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public UUID getTimeUuid() {
		return timeUuid;
	}

	public void setTimeUuid(UUID timeUuid) {
		this.timeUuid = timeUuid;
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}
    
    
}
