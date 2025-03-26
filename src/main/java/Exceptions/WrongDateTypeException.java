package Exceptions;

/**
 * Обработка ситуации, когда дата имеет неверный формат.
 */
public class WrongDateTypeException extends Exception{
    /**
     * Выводит предупреждающую строку.
     */
    public WrongDateTypeException() {
        super("Дата имеет неверный формат!");
    }
}
