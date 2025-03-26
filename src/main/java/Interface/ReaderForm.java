package Interface;

import Entities.Book;
import Entities.Reader;
import Exceptions.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;

import static Checks.Check.*;

/**
 * Каталог книг.
 */
public class ReaderForm extends Table {

    int i;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("library");
    EntityManager em = emf.createEntityManager();

    /**
     * Закрывает текущее окно каталога и открывает второе.
     */
    public void change_table() throws IOException {
        this.List.setVisible(false);
        (new BookForm()).showbk();
    }

    /**
     * Позволяет пользователю выбрать, в каком формате создать отчет по работе библиотеки.
     */
    public void report() {
        int rep = JOptionPane.showOptionDialog(null, "Отчет в каком формате Вы хотите создать?", "Создание отчета",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"PDF", "HTML"}, "PDF");
        switch (rep) {
            case 0:
                pdf();
                break;
            case 1:
                html();
                break;
        }
    }

    /**
     * Создает HTML-отчет о работе библиотеки за заданный промежуток времени.
     */
    public void html(){
        try {
            int count = 0;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Создание отчета в HTML-формате");
            int result = fileChooser.showSaveDialog(List);
            if (result != JFileChooser.APPROVE_OPTION) return;
            File chosenFile = fileChooser.getSelectedFile();
            String path = chosenFile.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".html"))
                path += ".html";
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileWriter(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert pw != null;
            pw.println("<TABLE BORDER><TR><TH>Имя читателя<TH>ID читателя<TH>Список книг<TH>Дата регистрации</TR>");
            Date startdate = Date.valueOf(JOptionPane.showInputDialog("Введите дату начала отчетного периода в формате yyyy-mm-dd"));
            datecheck(String.valueOf(startdate));
            Date enddate = Date.valueOf(JOptionPane.showInputDialog("Введите дату конца отчетного периода в формате yyyy-mm-dd"));
            datecheck(String.valueOf(enddate));
            java.util.List<Reader> readerList = em.createQuery("SELECT reader FROM Reader reader").getResultList();
            for (Reader reader : readerList) {
                if (reader.getRegdate() != null)
                    if (reader.getRegdate().before(enddate) & reader.getRegdate().after(startdate)) {
                        java.util.List<Book> ids = em.createQuery("SELECT book_id from Book where reader_id =" + reader.getReader_id()).getResultList();
                        String bids = StringUtils.join(ids, ", ");
                        pw.println("<TR><TD>" + reader.getName()
                                + "<TD>" + reader.getReader_id()
                                + "<TD>" + bids
                                + "<TD>" + reader.getRegdate());
                        count++;
                    }
            }
            pw.println("</TABLE>" + "Количество записавшихся читателей за указанный период: "
                    + count + "</br>" + "\nОтчет за период с ".concat(String.valueOf(startdate)).concat(" по ")
                    .concat(String.valueOf(enddate)).concat(" предоставлен ").concat(String.valueOf(LocalDate.now())));
            pw.close();
        } catch (EmptyDateException | WrongDateTypeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Создает PDF-отчет о работе библиотеки за заданный промежуток времени.
     */
    public void pdf() {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            com.itextpdf.text.Font bf= FontFactory.getFont("./fonts/arial.ttf", "CP1251", BaseFont.EMBEDDED, 10);
            com.itextpdf.text.Font hdr= FontFactory.getFont("./fonts/ariblk.ttf", "CP1251", BaseFont.EMBEDDED, 10);
            com.itextpdf.text.Font hd= FontFactory.getFont("./fonts/arial.ttf", "CP1251", BaseFont.EMBEDDED, 15);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Создание отчета в PDF-формате");
            int result = fileChooser.showSaveDialog(List);

            if(result!=JFileChooser.APPROVE_OPTION) return;
            File chosenFile = fileChooser.getSelectedFile();
            String path = chosenFile.getAbsolutePath();
            if(!path.toLowerCase().endsWith(".pdf"))
                path += ".pdf";

            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Date startdate = Date.valueOf(JOptionPane.showInputDialog("Введите дату начала отчетного периода в формате yyyy-mm-dd"));
            datecheck(String.valueOf(startdate));
            Date enddate = Date.valueOf(JOptionPane.showInputDialog("Введите дату конца отчетного периода в формате yyyy-mm-dd"));
            datecheck(String.valueOf(enddate));
            float [] pointColumnWidths = {150F, 150F, 150F, 150F};
            PdfPTable table = new PdfPTable(pointColumnWidths);
            java.util.List<Reader> readerList = em.createQuery("SELECT reader FROM Reader reader").getResultList();
            int count = 0;
            PdfPCell cell = new PdfPCell();
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Paragraph("Имя читателя", hdr));
            cell.addElement(paragraph);
            table.addCell(cell);
            paragraph.clear();
            cell = new PdfPCell();
            paragraph.add(new Paragraph("ID читателя", hdr));
            cell.addElement(paragraph);
            table.addCell(cell);
            paragraph.clear();
            cell = new PdfPCell();
            paragraph.add(new Paragraph("Список ID книг", hdr));
            cell.addElement(paragraph);
            table.addCell(cell);
            paragraph.clear();
            cell = new PdfPCell();
            paragraph.add(new Paragraph("Дата регистрации", hdr));
            cell.addElement(paragraph);
            table.addCell(cell);
            paragraph.clear();
            cell = new PdfPCell();
            for (Reader reader : readerList) {
                if (reader.getRegdate() != null)
                    if (reader.getRegdate().before(enddate) & reader.getRegdate().after(startdate)) {
                        java.util.List<Book> ids = em.createQuery("SELECT book_id from Book where reader_id =" + reader.getReader_id()).getResultList();
                        String bids = StringUtils.join(ids, ", ");
                        paragraph.add(new Paragraph(reader.getName(), bf));
                        cell.addElement(paragraph);
                        table.addCell(cell);
                        paragraph.clear();
                        cell = new PdfPCell();
                        paragraph.add(new Paragraph(String.valueOf(reader.getReader_id()), bf));
                        cell.addElement(paragraph);
                        table.addCell(cell);
                        paragraph.clear();
                        cell = new PdfPCell();

                        paragraph.add(new Paragraph(String.valueOf(bids), bf));
                        cell.addElement(paragraph);
                        table.addCell(cell);
                        paragraph.clear();
                        cell = new PdfPCell();
                        paragraph.add(new Paragraph(reader.getRegdate().toString(), bf));
                        cell.addElement(paragraph);
                        table.addCell(cell);
                        paragraph.clear();
                        cell = new PdfPCell();
                        count++;
                    }
            }
            Paragraph parag = new Paragraph();
            parag.add(new Paragraph("Отчет по работе библиотеки\n", hd));
            parag.setSpacingAfter(10);
            document.add(parag);
            parag = new Paragraph();
            parag.add(new Paragraph("Количество записавшихся читателей за указанный период: ".concat(String.valueOf(count)), bf));
            parag.setFont(bf);
            paragraph.add(new Paragraph("Отчет за период с ".concat(String.valueOf(startdate)).concat(" по ").concat(String.valueOf(enddate)).concat(" предоставлен ").concat(String.valueOf(LocalDate.now())), bf));
            document.add(table);
            document.add(parag);
            document.add(paragraph);
            document.add(new Paragraph(""));
            document.close();
        } catch (DocumentException | IOException | EmptyDateException | WrongDateTypeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ищет записи по выбранному параметру. Пользователь решает, по какому полю выполнить поиск, а затем вводит значение поля. Результат выводится в отдельном окне.
     */
    public void search() {
        int search = JOptionPane.showOptionDialog(null, "По какому полю хотите выполнить поиск?", "Поиск",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, cols, "Название");
        JFrame frame = new JFrame("Результаты поиска");
        frame.setSize(600, 400);
        frame.setLocation(100, 100);
        Object[][] result, res;
        DefaultTableModel searchmdl;
        JTable searchtbl;
        JScrollPane scrlsrch;
        res = new Object[tbl.getRowCount()][5];
        try {
            switch (search) {
                case 0 -> {
                    String newname = JOptionPane.showInputDialog("Введите имя читателя");
                    if (newname != null)
                        namecheck(newname);
                    else
                        return;
                    int k = 0;
                    for (int i = 0; i < tbl.getRowCount(); i++) {
                        String name = (String) tbl.getValueAt(i, 0);
                        if (name.equals(newname)) {
                            res[k][0] = tbl.getValueAt(i, 0);
                            res[k][1] = tbl.getValueAt(i, 1);
                            res[k][2] = tbl.getValueAt(i, 2);
                            res[k][3] = tbl.getValueAt(i, 3);
                            k++;
                        }
                    }
                    result = new Object[k][4];
                    System.arraycopy(res, 0, result, 0, k);
                    searchmdl = new DefaultTableModel(result, cols);
                    searchtbl = new JTable(searchmdl);
                    scrlsrch = new JScrollPane(searchtbl);
                    frame.add(scrlsrch, BorderLayout.CENTER);
                    frame.setVisible(true);
                }
                case 1 -> {
                    String newbid = JOptionPane.showInputDialog("Введите ID читателя");
                    if (newbid != null)
                        idcheck(newbid);
                    else
                        return;
                    int k = 0;
                    for (int i = 0; i < tbl.getRowCount(); i++) {
                        int name = (int) tbl.getValueAt(i, 1);
                        if (name == Integer.parseInt(newbid)) {
                            res[k][0] = tbl.getValueAt(i, 0);
                            res[k][1] = tbl.getValueAt(i, 1);
                            res[k][2] = tbl.getValueAt(i, 2);
                            res[k][3] = tbl.getValueAt(i, 3);
                            k++;
                        }
                    }
                    result = new Object[k][4];
                    System.arraycopy(res, 0, result, 0, k);
                    searchmdl = new DefaultTableModel(result, cols);
                    searchtbl = new JTable(searchmdl);
                    scrlsrch = new JScrollPane(searchtbl);
                    frame.add(scrlsrch, BorderLayout.CENTER);
                    frame.setVisible(true);
                }
                case 2 -> {
                    String newbd = JOptionPane.showInputDialog("Введите дату регистрации");
                    if (newbd != null)
                        datecheck(newbd);
                    else
                        return;
                    int k = 0;
                    String name;
                    for (int i = 0; i < tbl.getRowCount(); i++) {
                        Object cell = tbl.getValueAt(i, 3);
                        if (cell != null && cell != "0")
                            name = tbl.getValueAt(i, 3).toString();
                        else name = "0";
                        if (name.equals(newbd)) {
                            res[k][0] = tbl.getValueAt(i, 0);
                            res[k][1] = tbl.getValueAt(i, 1);
                            res[k][2] = tbl.getValueAt(i, 2);
                            res[k][3] = tbl.getValueAt(i, 3);
                            k++;
                        }
                    }
                    result = new Object[k][4];
                    System.arraycopy(res, 0, result, 0, k);
                    searchmdl = new DefaultTableModel(result, cols);
                    searchtbl = new JTable(searchmdl);
                    scrlsrch = new JScrollPane(searchtbl);
                    frame.add(scrlsrch, BorderLayout.CENTER);
                    frame.setVisible(true);
                }
            }
        } catch (NameWithNumbersAndSymbolsException | NameStartingWithLowerCaseLetterException | EmptyNameException |
                 IDContainingLettersException | EmptyIDException | EmptyDateException | WrongDateTypeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Заполняет ячейки таблицы значениями из базы данных. Если ячейка пуста, вписывается "0".
     */
    public void datains() {
        cols = new String[] {"Имя", "ID читателя", "Список книг", "Дата регистрации"};
        java.util.List<Reader> readers = em.createQuery("SELECT reader FROM Reader reader").getResultList();
        data = new Object[readers.size()][4];
        for(i = 0; i < readers.size(); i++)
        {
            Reader reader = readers.get(i);
            data[i][0] = reader.getName();
            data[i][1] = reader.getReader_id();
            java.util.List<Integer> ids = em.createQuery("SELECT book_id from Book where reader_id =" + data[i][1]).getResultList();
            data[i][2] = StringUtils.join(ids, ", ");
            if (reader.getRegdate() != null)
                data[i][3] = reader.getRegdate();
            else
                data[i][3] = "0";
        }
        show();
    }

    /**
     * Удаляет записи из таблицы и базы данных. Реализует два способа удаления - через выделение строк и нажатие кнопки удаления
     * и по идентификационному номеру книги.
     */
    public void dellist() {
        int[] selection = tbl.getSelectedRows();
        int len = selection.length;
        if (len != 0)
            for (int i = len - 1; i >= 0; i--) {
                em.getTransaction().begin();
                Object id = model.getValueAt(selection[i], 1);
                Reader reader = em.find(Reader.class, id);
                em.remove(reader);
                em.getTransaction().commit();
                model.removeRow(selection[i]);
            }
        else {
            //удаление по id
            //ввод id, поиск в БД строки по id, ее удаление
            try {
                String del = JOptionPane.showInputDialog("Введите ID читателя, информацию о котором нужно удалить");
                if (del != null)
                    idcheck(del);
                else
                    return;
                int delint = Integer.parseInt(del);
                em.getTransaction().begin();
                Reader reader = em.find(Reader.class, delint);
                em.remove(reader);
                em.getTransaction().commit();
                for (int i = 0; i < tbl.getRowCount(); i++) {
                    int ia = Integer.parseInt(tbl.getValueAt(i, 2).toString());
                    if (ia == delint) {
                        model.removeRow(i);
                        break;
                    }
                }
            } catch (IDContainingLettersException | EmptyIDException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        i--;
    }

    /**
     * Сохраняет информацию из таблицы в БД.
     */
    public void savelist() {
        em.getTransaction().begin();
        try {
            for(int i = 0; i < tbl.getRowCount(); i++) {
                String nm = (String)tbl.getValueAt(i, 0);
                String rdid = tbl.getValueAt(i, 1).toString();
                //String bk = tbl.getValueAt(i, 2).toString();
                Reader rddd = em.find(Reader.class, Integer.parseInt(rdid));
                Object rdate = tbl.getValueAt(i, 3);

                //Book bkkk = em.find(Book.class, Integer.valueOf(rdid));
                rddd.setName(nm);
                //rddd.setReader_id(rdid);
                if (rdate != null && rdate != "0") {
                    Date sqldate = Date.valueOf(rdate.toString());
                    rddd.setRegdate(sqldate);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            em.getTransaction().commit();
        }
    }

    /**
     * Добавляет запись в таблицу и БД. Пользователь последовательно вводит значения полей, идентификационный номер книги заполняется атвоматически,
     * идентификационный номер читателя и дата взятия заполняются нулями.
     */
    public void addlist() {
        Reader rd = new Reader();
        try {
            String nname = JOptionPane.showInputDialog("Введите имя читателя");
            if (nname != null)
                booknamecheck(nname);
            else
                return;
            String nrdate = JOptionPane.showInputDialog("Введите дату регистрации читателя");
            if (nrdate != null)
                datecheck(nrdate);
            else
                return;
            em.getTransaction().begin();
            rd.setName(nname);
            rd.setRegdate(Date.valueOf(nrdate));
            em.persist(rd);
            em.getTransaction().commit();
            model.addRow(new Object[]{nname, rd.getReader_id(), "0", nrdate});
            i++;
        } catch (NameStartingWithLowerCaseLetterException | EmptyNameException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (EmptyDateException | WrongDateTypeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Меняет значение выбранного пользователем поля. Вводится идентификационный номер книги, выбирается поле, которое нужно изменить и вводится новое значение.
     */
    public void editlist() {
        String edin;
        try {
            edin = JOptionPane.showInputDialog("Информацию о каком читателе вы хотите изменить(введите ID)?");
            if (edin != null)
                idcheck(edin);
            else
                return;
            int k = 0;
            do {
                if (tbl.getValueAt(k, 1).toString().equals(edin)) break;
                k++;
            } while (k < i);
            int edop = JOptionPane.showOptionDialog(null, "Какое поле хотите изменить?", "Редактирование записи о читателе",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Имя читателя", "Дата регистрации"}, "Имя читателя");
            try {
                switch (edop) {
                    case 0 -> {
                        String newname = JOptionPane.showInputDialog("Введите имя читателя");
                        if (newname != null)
                            booknamecheck(newname);
                        else
                            return;
                        tbl.setValueAt(newname, k, 0);
                    }
                    case 1 -> {
                        String newbd = JOptionPane.showInputDialog("Введите дату регистрации в формате yyyy-mm-dd");
                        if (newbd != null)
                            datecheck(newbd);
                        else
                            return;
                        tbl.setValueAt(newbd, k, 3);
                    }
                }
                i++;
            } catch (NameStartingWithLowerCaseLetterException | EmptyNameException |
                     EmptyDateException | WrongDateTypeException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } catch (IDContainingLettersException | EmptyIDException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    /**
     * Назначает посказки для кнопок на панели,
     * добавляет колонки в таблицу и вызывает функции-события кнопок панели.
     */
    public void showrd() {
        datains();
        save.setToolTipText("Сохранить список читателей");
        add.setToolTipText("Добавить читателя");
        edit.setToolTipText("Редактировать записи о читателе");
        delete.setToolTipText("Удалить читателя");
        report.setToolTipText("Создать отчет");
        chngtbl.setText("К таблице книг");


        srch.addActionListener(event -> search());
        delete.addActionListener(event -> dellist());
        save.addActionListener(event -> savelist());
        add.addActionListener(event -> addlist());
        edit.addActionListener(event -> editlist());
        chngtbl.addActionListener(event -> {
            try {
                change_table();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        report.addActionListener(event -> report());
        /*savefile.addActionListener(event -> savefile());
        load.addActionListener(event ->load());*/

        List.validate();
    }
}

