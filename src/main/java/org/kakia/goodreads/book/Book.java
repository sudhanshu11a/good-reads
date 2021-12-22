package org.kakia.goodreads.book;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

@Table(value = "book_by_id")
public class Book {
    
    @Id
    @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @Column("title")
    @CassandraType(type = Name.TEXT)
    private String title;

    @Column("subtitle")
    @CassandraType(type = Name.TEXT)
    private String subtitle;

    @Column("description")
    @CassandraType(type = Name.TEXT)
    private String description;

    @Column("published_date")
    @CassandraType(type = Name.DATE)
    private LocalDate publishedDate;

    @Column("cover_ids")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> coverImageList;

    @Column("author_names")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> authorNameList;

    @Column("author_ids")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> authorIdList;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle the subtitle to set
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the publishedDate
     */
    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    /**
     * @param publishedDate the publishedDate to set
     */
    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    /**
     * @return the coverImageList
     */
    public List<String> getCoverImageList() {
        return coverImageList;
    }

    /**
     * @param coverImageList the coverImageList to set
     */
    public void setCoverImageList(List<String> coverImageList) {
        this.coverImageList = coverImageList;
    }

    /**
     * @return the authorNameList
     */
    public List<String> getAuthorNameList() {
        return authorNameList;
    }

    /**
     * @param authorNameList the authorNameList to set
     */
    public void setAuthorNameList(List<String> authorNameList) {
        this.authorNameList = authorNameList;
    }

    /**
     * @return the authorIdList
     */
    public List<String> getAuthorIdList() {
        return authorIdList;
    }

    /**
     * @param authorIdList the authorIdList to set
     */
    public void setAuthorIdList(List<String> authorIdList) {
        this.authorIdList = authorIdList;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        return "Book [ id=" + id + ", title=" + title + ",authorIdList=" + authorIdList + ", authorNameList=" + authorNameList + ", coverImageList="
                + coverImageList + ", description=" + description + ", publishedDate=" + publishedDate
                + ", subtitle=" + subtitle + "]";
    }

}
