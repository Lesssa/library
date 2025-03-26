package Interface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

/**
 * Основа для каталога книг или читателей.
 */
public class Table {
    JFrame List;
    DefaultTableModel model;
    JButton save, edit, add, delete, srch, chngtbl/*, load, savefile*/, report;
    JTable tbl;
    JScrollPane scrl;
    JToolBar toolBar;
    String[] cols;
    Object[][] data;
    JPanel filterPanel;
    /**
     * Формирует основу для окна каталога читателей или книг.
     */
    public void show() {
        List = new JFrame("Каталог библиотеки");
        List.setSize(600, 400);
        List.setLocation(100, 100);
        List.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        save = new JButton(new ImageIcon("./img/3floppy_unmount_7414.png"));
        add = new JButton(new ImageIcon("./img/edit_add_7429.png"));
        edit = new JButton(new ImageIcon("./img/pencil_8114.png"));
        delete = new JButton(new ImageIcon("./img/edit_remove_6949.png"));
        /*load = new JButton(new ImageIcon("./img/page_white_get_4472.png"));
        savefile = new JButton(new ImageIcon("./img/3floppy_unmount_7414.png"));*/
        report = new JButton(new ImageIcon("./img/report_4669.png"));
        chngtbl = new JButton();

        chngtbl.setToolTipText("Сохраните изменения перед нажатием");

        toolBar = new JToolBar("Панель инструментов");
        toolBar.add(save);
        toolBar.add(add);
        toolBar.add(edit);
        toolBar.add(delete);
        toolBar.add(report);
        /*toolBar.add(load);
        toolBar.add(savefile);*/

        List.setLayout(new BorderLayout());
        List.add(toolBar, BorderLayout.NORTH);

        model = new DefaultTableModel(data, cols) {
            public Class getColumnClass(int column)
            {
                if(column==0) return ImageIcon.class;
                return String.class;
            }
        };
        tbl = new JTable(model);
        scrl = new JScrollPane(tbl);
        List.add(scrl, BorderLayout.CENTER);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl.setRowSorter(sorter);

        srch = new JButton("Поиск");
        filterPanel = new JPanel();

        filterPanel.add(srch);
        filterPanel.add(chngtbl);

        List.add(filterPanel, BorderLayout.SOUTH);

        /*savefile.setToolTipText("Сохранить в файл");
        load.setToolTipText("Загрузить базу данных из файла");
*/
        List.setVisible(true);
    }
}
