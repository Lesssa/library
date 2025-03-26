package Exceptions;

/**
 * Обработка ситуации, когда имя содержит символы кроме дефиса и цифры.
 */
public class NameWithNumbersAndSymbolsException extends Exception{
    /**
     * Выводит предупреждающую строку.
     */
    public NameWithNumbersAndSymbolsException() {
        super("Имя не может содержать цифры и символы кроме дефиса!");
    }
}
