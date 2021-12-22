package org.kakia.goodreads.author;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

@Table(value = "author_by_id")
public class Author {
    
    @Id
    @PrimaryKeyColumn(name = "author_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @Column("author_name")
    @CassandraType(type = Name.TEXT)
    private String name;

    @Column("persnal_name")
    @CassandraType(type = Name.TEXT)
    private String persnalName;
    
    @Column("wikipedia")
    @CassandraType(type = Name.TEXT)
    private String wikipedia;
    
    @Column("published_date")
    @CassandraType(type = Name.DATE)
    private LocalDate birthDate;
    
    @Column("bio")
    @CassandraType(type = Name.TEXT)
    private String bio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersnalName() {
        return persnalName;
    }

    public void setPersnalName(String persnalName) {
        this.persnalName = persnalName;
    }

    
	/**
	 * @return the wikipedia
	 */
	public String getWikipedia() {
		return wikipedia;
	}

	/**
	 * @param wikipedia the wikipedia to set
	 */
	public void setWikipedia(String wikipedia) {
		this.wikipedia = wikipedia;
	}

	/**
	 * @return the birthDate
	 */
	public LocalDate getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the bio
	 */
	public String getBio() {
		return bio;
	}

	/**
	 * @param bio the bio to set
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Author [id=" + id + ", name=" + name + ", persnalName=" + persnalName + "]";
    }

    
}
