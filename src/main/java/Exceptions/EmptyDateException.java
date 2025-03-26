package Exceptions;

/**
 * Обработка ситуации, когда дата пустая.
 */
public class EmptyDateException extends Exception {
    /**
     * Выводит предупреждающую строку.
     */
    public EmptyDateException() {
        super("Поле даты не может быть пустым!");
    }
}
