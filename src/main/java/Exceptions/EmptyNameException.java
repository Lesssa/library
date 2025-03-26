package Exceptions;

/**
 * Обработка ситуации, когда имя пустое.
 */
public class EmptyNameException extends Exception{
    /**
     * Выводит предупреждающую строку.
     */
    public EmptyNameException() {
        super("Имя не может быть пустым!");
    }
}
