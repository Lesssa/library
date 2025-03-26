package Interface;

import javax.swing.*;
import java.io.IOException;

public class Menu {
    public void menu() throws IOException {
        int butns = JOptionPane.showOptionDialog(null,
                "Какой каталог хотите открыть? Не волнуйтесь, позже Вы сможете открыть второй каталог.",
                "Выбор каталога",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[] {"Каталог книг", "Каталог читателей"}, "Каталог книг");
        switch (butns) {
            case 0 -> new BookForm().showbk();
            case 1 -> new ReaderForm().showrd();
        }
    }
}
