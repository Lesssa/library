package Checks;

import Exceptions.*;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Проверка всех полей на правильность.
 */
public class Check {
    /**
     * Проверяет, не пусто ли, не содержит ли что-то кроме букв и дефиса
     * или не начинается ли с маленькой буквы имя человека.
     * @param name имя человека
     * @throws EmptyNameException если имя пустое
     * @throws NameWithNumbersAndSymbolsException если имя содержит символы или цифры
     * @throws NameStartingWithLowerCaseLetterException если имя начинается с маленькой буквы
     */
    public static void namecheck(String name) throws EmptyNameException, NameWithNumbersAndSymbolsException, NameStartingWithLowerCaseLetterException {
        if (name == null || name.equals("")) throw new EmptyNameException();
        if (Pattern.matches(".*[^a-zA-Zа-яА-Я-. ].*", name)) throw new NameWithNumbersAndSymbolsException();
        if (Pattern.matches("[a-zа-я].*", name)) throw new NameStartingWithLowerCaseLetterException();
    }

    /**
     * Проверяет, не пусто ли или не начинается ли с маленькой буквы название книги.
     * @param bookname название книги
     * @throws EmptyNameException если название пустое
     * @throws NameStartingWithLowerCaseLetterException если название начинается с маленькой буквы
     */
    public static void booknamecheck(String bookname) throws EmptyNameException, NameStartingWithLowerCaseLetterException {
        if (bookname == null || bookname.equals("")) throw new EmptyNameException();
        if (Pattern.matches("[a-zа-я].*", bookname)) throw new NameStartingWithLowerCaseLetterException();
    }

    /**
     * Проверяет, не пуст ли или не содержит ли что-то кроме
     * цифр идентификационный номер.
     * @param id идентификационный номер
     * @throws IDContainingLettersException если ID содержит символы и буквы
     * @throws EmptyIDException если ID пустое
     */
    public static void idcheck(String id) throws IDContainingLettersException, EmptyIDException {
        if (Objects.equals(id, "0") || id == null || id.equals("")) throw new EmptyIDException();
        if (Pattern.matches(".*[^0-9].*", id)) throw new IDContainingLettersException();
    }

    /**
     * Проверяет, не пуста ли или не имеет ли неверный формат дата.
     * @param date дата
     * @throws EmptyDateException если поле даты пусто
     * @throws WrongDateTypeException если дата имеет неверный формат
     */
    public static void datecheck(String date) throws EmptyDateException, WrongDateTypeException {
        Pattern date_pattern = Pattern.compile(
                "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$"
                        + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                        + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                        + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");
        if (Objects.equals(date, "0") || date.equals("")) throw new EmptyDateException();
        if (!date_pattern.matcher(date).matches()) throw new WrongDateTypeException();

    }
}

