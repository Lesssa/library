package Entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Класс сущности "Читатель".
 */
@Entity
@Table(name = "library.reader")
public class Reader {
    @Id
    @Column(name = "reader_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reader_id;
    @Column(name = "name")
    private String name;

    @Column(name = "regdate")
    private Date regdate;

    /*@OneToMany(mappedBy = "book_id")
    private List<Book> booklist;*/

    /**
     * Возвращает дату регистрации читателя.
     * @return дату регистрации читателя
     */
    public Date getRegdate() {
        return regdate;
    }

    /**
     * Устанавливает значение даты регистрации.
     * @param regdate дата регистрации читателя
     */
    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    /**
     * Устанавливает значения списка идентификационных номеров назначенных читателю книг.
     * @param booklist список идентификационных номеров книг
     */
    /*public void setBook_id(List<Book> booklist) {
        this.booklist = booklist;
    }*/

    /**
     * Возвращает список идентификационных номеров книг, назначенных читателю.
     * @return список идентификационных номеров книг
     */
    /*public List<Book> getBook_id() {
        return booklist;
    }*/

    /**
     * Добавляет в список идентификационных номеров книг одну.
     * @param book_id иднетификационный номер книги
     */
    /*public void addBook_id(Book book_id) {
        this.booklist.add(book_id);
    }

    /**
     * Возвращает количество книг в списке.
     * @param booklist список идентификационных номеров книг
     * @return количество книг в списке
     */
    /*public int bookcount(List<Book> booklist) {
        return booklist.size();
    }*/

    /**
     * Устанавливает имя читателя.
     * @param name имя читателя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя читателя.
     * @return имя читателя
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает идентификационный номер читателя.
     * @param reader_id идентификационный номер читателя
     */
    /*public void setReader_id(int reader_id) {
        this.reader_id = reader_id;
    }*/

    /**
     * Возвращает идентификационный номер читателя.
     * @return идентификационный номер читателя
     */
    public int getReader_id() {
        return reader_id;
    }
}
