package Exceptions;

/**
 * Обработка ситуации, когда идентификационный номер содержит буквы и символы.
 */
public class IDContainingLettersException extends Exception{
    /**
     * Выводит предупреждающую строку.
     */
    public IDContainingLettersException() {
        super("ID не может содержать букв или символов!");
    }
}
