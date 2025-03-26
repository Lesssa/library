package Entities;

import javax.persistence.*;
import java.sql.Date;

/**
 * Класс сущности "Книга".
 */
@Entity
@Table(name = "library.book")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int book_id;
    @JoinColumn(name = "reader_id")
    @ManyToOne
    private Reader reader_id;
    @Column(name = "author")
    private String author;
    @Column(name = "name")
    private String name;
    @Column(name = "bookdate")
    private Date bookdate;
    @Column(name = "picpath")
    private String picpath;

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    /**
     * Возвращает дату взятия книги.
     * @return дату взятия книги
     */
    public Date getBookdate() {
        return bookdate;
    }

    /**
     * Устанавливает дату взятия книги.
     * @param bookdate дата взятия книги
     */
    public void setBookdate(Date bookdate) {
        this.bookdate = bookdate;
    }

    /**
     * Устанавливает название книги.
     * @param name название книги
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает название книги.
     * @return название книги
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает идентификационный номер читателя, взявшего книгу.
     * @param reader_id идентификационный номер читателя
     */
    public void setReader_id(Reader reader_id) {
        this.reader_id = reader_id;
    }

    /**
     * Возвращает идентификационный номер читателя, взявшего книгу.
     * @return идентификационный номер читателя
     */
    public Reader getReader_id() {
        return reader_id;
    }

    /**
     * Устанавливает идентификационный номер книги.
     * @param book_id идентификационный номер книги
     */
    /*public void setBook_id(int book_id) {
        this.book_id = book_id;
    }*/

    /**
     * Возвращает идентификационный номер книги.
     * @return идентификационный номер книги
     */
    public int getBook_id() {
        return book_id;
    }

    /**
     * Устанавливает имя автора книги.
     * @param author имя автора книги
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Возвращает имя автора книги.
     * @return имя автора книги
     */
    public String getAuthor() {
        return author;
    }
}

