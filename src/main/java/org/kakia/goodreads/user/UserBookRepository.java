/**
 * 
 */
package org.kakia.goodreads.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sudhanshu Sharma
 *
 */
@Repository
public interface UserBookRepository extends CassandraRepository<UserBook, UserBookPrimaryKey>{

	//Slice<UserBook> findAllByIdUserId(String userId, Pageable pageable);

	//@Query("SELECT b FROM UserBook b")
	List<UserBook> findAllByUserId(String userId);
}
