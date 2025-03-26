package Exceptions;

/**
 * Обработка ситуации, когда имя начинается с маленькой буквы.
 */
public class NameStartingWithLowerCaseLetterException extends Exception{
    /**
     * Выводится предупреждающая строка.
     */
    public NameStartingWithLowerCaseLetterException() {
        super("Имя должно начинаться с заглавной буквы!");
    }
}
