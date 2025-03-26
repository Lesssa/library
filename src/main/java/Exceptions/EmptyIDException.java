package Exceptions;

/**
 * Обработка ситуации, когда идентификациоонный номер пуст.
 */
public class EmptyIDException extends Exception{
    /**
     * Выводит предупреждающую строку.
     */
    public EmptyIDException() {
        super("ID не может быть пустым!");
    }
}
