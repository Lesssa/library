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
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;

import static Checks.Check.*;

/**
 * Каталог книг.
 */
public class BookForm extends Table {

    private int i;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("library");
    EntityManager em = emf.createEntityManager();


    public void change_table() {
        this.List.setVisible(false);
        (new ReaderForm()).showrd();
    }
    /**
     * Сохраняет таблицу в xml-файл.
     */
    /*public void savefile() {
        FileDialog savee = new FileDialog(List, "Сохранение данных в xml-файл", FileDialog.SAVE);
        savee.setFile("");
        savee.setVisible(true);
        String fileName = savee.getDirectory() + savee.getFile();
        if(fileName == null) return; // Если пользователь нажал «отмена»
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Node booklist = doc.createElement("booklist");
            doc.appendChild(booklist);
            for (int i = 0; i < model.getRowCount(); i++) {
                Element book = doc.createElement("book");
                booklist.appendChild(book);
                book.setAttribute("name", model.getValueAt(i, 0).toString());
                book.setAttribute("author", model.getValueAt(i, 1).toString());
                book.setAttribute("book_id", model.getValueAt(i, 2).toString());
                if (model.getValueAt(i, 3) != null)
                    book.setAttribute("reader_id", model.getValueAt(i, 3).toString());
                else
                    book.setAttribute("reader_id", "0");
                book.setAttribute("bookdate", model.getValueAt(i, 4).toString());
            }
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                FileWriter fileWriter = new FileWriter(fileName);
                transformer.transform(new DOMSource(doc), new StreamResult(fileWriter));
            } catch (TransformerException | IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Загружает таблицу из xml-файла.
     */
    /*public void load() {
        FileDialog savee = new FileDialog(List, "Загрузка данных из xml-файла", FileDialog.LOAD);
        savee.setFile("");
        savee.setVisible(true);
        String fileName = savee.getDirectory() + savee.getFile();
        if(fileName == null) return; // Если пользователь нажал «отмена»
        do
            model.removeRow(0);
        while (model.getRowCount() != 0);
        try {

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(new File(fileName));
            doc.getDocumentElement().normalize();
            NodeList nlBooks = doc.getElementsByTagName("book");
            for (int temp = 0; temp < nlBooks.getLength(); temp++) {
                Node elem = nlBooks.item(temp);
                NamedNodeMap attrs = elem.getAttributes();
                String name = attrs.getNamedItem("name").getNodeValue();
                String author = attrs.getNamedItem("author").getNodeValue();
                String book_id = attrs.getNamedItem("book_id").getNodeValue();
                String reader_id = attrs.getNamedItem("reader_id").getNodeValue();
                String bookdate = attrs.getNamedItem("bookdate").getNodeValue();
                model.addRow(new String[]{name, author, book_id, reader_id, bookdate});
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }*/

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
            pw.println("<TABLE BORDER><TR><TH>Название книги<TH>Автор<TH>ID читателя<TH>Дата взятия</TR>");
            Date startdate = Date.valueOf(JOptionPane.showInputDialog("Введите дату начала отчетного периода в формате yyyy-mm-dd"));
            datecheck(String.valueOf(startdate));
            Date enddate = Date.valueOf(JOptionPane.showInputDialog("Введите дату конца отчетного периода в формате yyyy-mm-dd"));
            datecheck(String.valueOf(enddate));
            java.util.List<Book> bookList = em.createQuery("SELECT book FROM Book book").getResultList();
            for (Book book : bookList) {
                if (book.getBookdate() != null)
                    if (book.getBookdate().before(enddate) & book.getBookdate().after(startdate)) {
                        pw.println("<TR><TD>" + book.getName()
                                + "<TD>" + book.getAuthor()
                                + "<TD>" + book.getReader_id().getReader_id()
                                + "<TD>" + book.getBookdate());
                        count++;
                    }
            }
            pw.println("</TABLE>" + "Количество книг, взятых читателями за указанный период: "
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
            java.util.List<Book> bookList = em.createQuery("SELECT book FROM Book book").getResultList();
            int count = 0;
            PdfPCell cell = new PdfPCell();
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Paragraph("Название книги", hdr));
            cell.addElement(paragraph);
            table.addCell(cell);
            paragraph.clear();
            cell = new PdfPCell();
            paragraph.add(new Paragraph("Автор", hdr));
            cell.addElement(paragraph);
            table.addCell(cell);
            paragraph.clear();
            cell = new PdfPCell();
            paragraph.add(new Paragraph("ID читателя", hdr));
            cell.addElement(paragraph);
            table.addCell(cell);
            paragraph.clear();
            cell = new PdfPCell();
            paragraph.add(new Paragraph("Дата взятия книг", hdr));
            cell.addElement(paragraph);
            table.addCell(cell);
            paragraph.clear();
            cell = new PdfPCell();
            for (Book book : bookList) {
                if (book.getBookdate() != null)
                    if (book.getBookdate().before(enddate) & book.getBookdate().after(startdate)) {
                        paragraph.add(new Paragraph(book.getName(), bf));
                        cell.addElement(paragraph);
                        table.addCell(cell);
                        paragraph.clear();
                        cell = new PdfPCell();
                        paragraph.add(new Paragraph(book.getAuthor(), bf));
                        cell.addElement(paragraph);
                        table.addCell(cell);
                        paragraph.clear();
                        cell = new PdfPCell();

                        paragraph.add(new Paragraph(String.valueOf(book.getReader_id().getReader_id()), bf));
                        cell.addElement(paragraph);
                        table.addCell(cell);
                        paragraph.clear();
                        cell = new PdfPCell();
                        paragraph.add(new Paragraph(book.getBookdate().toString(), bf));
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
            parag.add(new Paragraph("Количество книг, взятых читателями за указанный период: ".concat(String.valueOf(count)), bf));
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
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Название", "Автор", "ID книги", "ID читателя", "Дата взятия"}, "Название");
        JFrame frame = new JFrame("Результаты поиска");
        frame.setSize(600, 400);
        frame.setLocation(100, 100);
        Object[][] result, res;
        DefaultTableModel searchmdl;
        JTable searchtbl;
        JScrollPane scrlsrch;
        ImageIcon icon;
        res = new Object[tbl.getRowCount()][6];
        try {
            switch (search) {
                case 0 -> {
                    String newname = JOptionPane.showInputDialog("Введите название книги");
                    if (newname != null)
                        booknamecheck(newname);
                    else
                        return;
                    int k = 0;
                    for (int i = 0; i < tbl.getRowCount(); i++) {
                        String name = (String) tbl.getValueAt(i, 1);
                        if (name.equals(newname)) {
                            res[k][1] = tbl.getValueAt(i, 1);
                            res[k][2] = tbl.getValueAt(i, 2);
                            res[k][3] = tbl.getValueAt(i, 3);
                            res[k][4] = tbl.getValueAt(i, 4);
                            res[k][5] = tbl.getValueAt(i, 5);
                            Book book = em.find(Book.class, Integer.valueOf(String.valueOf(res[k][3])));
                            if (book.getPicpath() != null) {
                                icon = pic_resize(book.getPicpath());
                                res[k][0] = icon;
                            }
                            k++;
                        }
                    }
                    result = new Object[k][6];
                    System.arraycopy(res, 0, result, 0, k);
                    searchmdl = new DefaultTableModel(result, cols) {
                        public Class getColumnClass(int column)
                        {
                            if(column==0) return ImageIcon.class;
                            return String.class;
                        }
                    };
                    searchtbl = new JTable(searchmdl);
                    scrlsrch = new JScrollPane(searchtbl);
                    frame.add(scrlsrch, BorderLayout.CENTER);
                    for (int i = 0; i < searchtbl.getRowCount(); i++)
                        if (searchtbl.getValueAt(i, 0) != null)
                            searchtbl.setRowHeight(i, 100);
                    frame.setVisible(true);
                }
                case 1 -> {
                    String newauthor = JOptionPane.showInputDialog("Введите имя автора");
                    if (newauthor != null)
                        namecheck(newauthor);
                    else
                        return;
                    int k = 0;
                    for (int i = 0; i < tbl.getRowCount(); i++) {
                        String auth = (String) tbl.getValueAt(i, 2);
                        if (auth.equals(newauthor)) {
                            res[k][1] = tbl.getValueAt(i, 1);
                            res[k][2] = tbl.getValueAt(i, 2);
                            res[k][3] = tbl.getValueAt(i, 3);
                            res[k][4] = tbl.getValueAt(i, 4);
                            res[k][5] = tbl.getValueAt(i, 5);
                            Book book = em.find(Book.class, Integer.valueOf(String.valueOf(res[k][3])));
                            if (book.getPicpath() != null) {
                                icon = pic_resize(book.getPicpath());
                                res[k][0] = icon;
                            }
                            k++;
                        }
                    }
                    result = new Object[k][6];
                    System.arraycopy(res, 0, result, 0, k);
                    searchmdl = new DefaultTableModel(result, cols) {
                        public Class getColumnClass(int column)
                        {
                            if(column==0) return ImageIcon.class;
                            return String.class;
                        }
                    };
                    searchtbl = new JTable(searchmdl);
                    scrlsrch = new JScrollPane(searchtbl);
                    frame.add(scrlsrch, BorderLayout.CENTER);
                    for (int i = 0; i < searchtbl.getRowCount(); i++)
                        if (searchtbl.getValueAt(i, 0) != null)
                            searchtbl.setRowHeight(i, 100);
                    frame.setVisible(true);
                }
                case 2 -> {
                    String newbid = JOptionPane.showInputDialog("Введите ID книги");
                    if (newbid != null)
                        idcheck(newbid);
                    else
                        return;
                    int k = 0;
                    for (int i = 0; i < tbl.getRowCount(); i++) {
                        int name = (int) tbl.getValueAt(i, 3);
                        if (name == Integer.parseInt(newbid)) {
                            res[k][1] = tbl.getValueAt(i, 1);
                            res[k][2] = tbl.getValueAt(i, 2);
                            res[k][3] = tbl.getValueAt(i, 3);
                            res[k][4] = tbl.getValueAt(i, 4);
                            res[k][5] = tbl.getValueAt(i, 5);
                            Book book = em.find(Book.class, Integer.valueOf(String.valueOf(res[k][3])));
                            if (book.getPicpath() != null) {
                                icon = pic_resize(book.getPicpath());
                                res[k][0] = icon;
                            }
                            k++;
                        }
                    }
                    result = new Object[k][6];
                    System.arraycopy(res, 0, result, 0, k);
                    searchmdl = new DefaultTableModel(result, cols) {
                        public Class getColumnClass(int column)
                        {
                            if(column==0) return ImageIcon.class;
                            return String.class;
                        }
                    };
                    searchtbl = new JTable(searchmdl);
                    scrlsrch = new JScrollPane(searchtbl);
                    frame.add(scrlsrch, BorderLayout.CENTER);
                    for (int i = 0; i < searchtbl.getRowCount(); i++)
                        if (searchtbl.getValueAt(i, 0) != null)
                            searchtbl.setRowHeight(i, 100);
                    frame.setVisible(true);
                }
                case 3 -> {
                    String newrid = JOptionPane.showInputDialog("Введите ID читателя");
                    if (newrid != null)
                        idcheck(newrid);
                    else
                        return;
                    int k = 0, name;
                    for (int i = 0; i < tbl.getRowCount(); i++) {
                        Object cell = tbl.getValueAt(i, 4);
                        if (cell != null && cell != "0")
                            name = (int) tbl.getValueAt(i, 4);
                        else name = 0;
                        if (name == Integer.parseInt(newrid)) {
                            //res[k][0] = tbl.getValueAt(i, 0);
                            res[k][1] = tbl.getValueAt(i, 1);
                            res[k][2] = tbl.getValueAt(i, 2);
                            res[k][3] = tbl.getValueAt(i, 3);
                            res[k][4] = tbl.getValueAt(i, 4);
                            res[k][5] = tbl.getValueAt(i, 5);
                            Book book = em.find(Book.class, Integer.valueOf(String.valueOf(res[k][3])));
                            if (book.getPicpath() != null) {
                                icon = pic_resize(book.getPicpath());
                                res[k][0] = icon;
                            }
                            k++;
                        }
                    }
                    result = new Object[k][6];
                    System.arraycopy(res, 0, result, 0, k);
                    searchmdl = new DefaultTableModel(result, cols) {
                        public Class getColumnClass(int column)
                        {
                            if(column==0) return ImageIcon.class;
                            return String.class;
                        }
                    };
                    searchtbl = new JTable(searchmdl);
                    scrlsrch = new JScrollPane(searchtbl);
                    frame.add(scrlsrch, BorderLayout.CENTER);
                    for (int i = 0; i < searchtbl.getRowCount(); i++)
                        if (searchtbl.getValueAt(i, 0) != null)
                            searchtbl.setRowHeight(i, 100);
                    frame.setVisible(true);
                }
                case 4 -> {
                    String newbd = JOptionPane.showInputDialog("Введите дату взятия");
                    if (newbd != null)
                        datecheck(newbd);
                    else
                        return;
                    int k = 0;
                    String name;
                    for (int i = 0; i < tbl.getRowCount(); i++) {
                        Object cell = tbl.getValueAt(i, 5);
                        if (cell != null && cell != "0")
                            name = tbl.getValueAt(i, 5).toString();
                        else name = "0";
                        if (name.equals(newbd)) {
                            //res[k][0] = tbl.getValueAt(i, 0);
                            res[k][1] = tbl.getValueAt(i, 1);
                            res[k][2] = tbl.getValueAt(i, 2);
                            res[k][3] = tbl.getValueAt(i, 3);
                            res[k][4] = tbl.getValueAt(i, 4);
                            res[k][5] = tbl.getValueAt(i, 5);
                            Book book = em.find(Book.class, Integer.valueOf(String.valueOf(res[k][3])));
                            if (book.getPicpath() != null) {
                                icon = pic_resize(book.getPicpath());
                                res[k][0] = icon;
                            }
                            k++;
                        }
                    }
                    result = new Object[k][6];
                    System.arraycopy(res, 0, result, 0, k);
                    searchmdl = new DefaultTableModel(result, cols) {
                        public Class getColumnClass(int column)
                        {
                            if(column==0) return ImageIcon.class;
                            return String.class;
                        }
                    };
                    searchtbl = new JTable(searchmdl);
                    scrlsrch = new JScrollPane(searchtbl);
                    frame.add(scrlsrch, BorderLayout.CENTER);
                    for (int i = 0; i < searchtbl.getRowCount(); i++)
                        if (searchtbl.getValueAt(i, 0) != null)
                            searchtbl.setRowHeight(i, 100);
                    frame.setVisible(true);
                }
            }
        } catch (NameWithNumbersAndSymbolsException | NameStartingWithLowerCaseLetterException | EmptyNameException |
                 IDContainingLettersException | EmptyIDException | EmptyDateException | WrongDateTypeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageIcon pic_resize(String path) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(path));
        float w = bufferedImage.getWidth();
        float h = bufferedImage.getHeight();
        h = h/100;
        w = w/h;
        int ww = Math.round(w);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(bufferedImage)
                .size(ww, 100)
                .outputFormat("JPEG")
                .outputQuality(1)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ImageIcon outputImage = new ImageIcon(ImageIO.read(inputStream));
        return outputImage;
    }

    /**
     * Заполняет ячейки таблицы значениями из базы данных. Если ячейка пуста, вписывается "0".
     */
    public void datains() throws IOException {
        cols = new String[] {"Фотография", "Название", "Автор", "ID книги", "ID читателя", "Дата взятия"};
        java.util.List<Book> books = em.createQuery("SELECT book FROM Book book").getResultList();
        ImageIcon icon;
        data = new Object[books.size()][6];
        for(i = 0; i < books.size(); i++)
        {
            Book book = books.get(i);
            if (book.getPicpath() != null) {
                icon = pic_resize(book.getPicpath());
                /*tbl.setRowHeight(i, icon.getIconHeight());*/
                data[i][0] = icon;
            }
            data[i][1] = book.getName();
            data[i][2] = book.getAuthor();
            data[i][3] = book.getBook_id();
            if (book.getReader_id() != null) {
                int id = book.getReader_id().getReader_id();
                data[i][4] = id;
            }
            else
                data[i][4] = "0";
            if (book.getBookdate() != null)
                data[i][5] = book.getBookdate();
            else
                data[i][5] = "0";
        }
        show();
        for (int i = 0; i < tbl.getRowCount(); i++)
            if (tbl.getValueAt(i, 0) != null)
                tbl.setRowHeight(i, 100);
        //tbl.setRowHeight(100);
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
                Object id = model.getValueAt(selection[i], 3);
                Book book = em.find(Book.class, id);
                em.remove(book);
                em.getTransaction().commit();
                model.removeRow(selection[i]);

            }
        else {
            //удаление по id
            //ввод id, поиск в БД строки по id, ее удаление
            try {
                String del = JOptionPane.showInputDialog("Введите ID книги, информацию о которой нужно удалить");
                if (del != null)
                    idcheck(del);
                else
                    return;
                int delint = Integer.parseInt(del);
                em.getTransaction().begin();
                Book book = em.find(Book.class, delint);
                em.remove(book);
                em.getTransaction().commit();
                for (int i = 0; i < tbl.getRowCount(); i++) {
                    int ia = Integer.parseInt(tbl.getValueAt(i, 3).toString());
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
                String nm = (String)tbl.getValueAt(i, 1);
                String athr = (String)tbl.getValueAt(i, 2);
                String bkid = tbl.getValueAt(i, 3).toString();
                String rd = tbl.getValueAt(i, 4).toString();
                Object rdate = tbl.getValueAt(i, 5);
                Reader rdid = em.find(Reader.class, Integer.valueOf(rd));
                Book bkkk = em.find(Book.class, Integer.valueOf(bkid));
                bkkk.setName(nm);
                bkkk.setAuthor(athr);
                bkkk.setReader_id(rdid);
                if (rdate != null && rdate != "0") {
                    Date sqldate = Date.valueOf(rdate.toString());
                    bkkk.setBookdate(sqldate);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            em.getTransaction().commit();
        }
    }

    /**
     * Добавляет запись в таблицу и БД. Пользователь последовательно вводит значения полей, идентификационный номер книги заполняется автоматически,
     * идентификационный номер читателя и дата взятия заполняются нулями.
     */
    public void addlist() {
        Book bk = new Book();
        try {
            String nname = JOptionPane.showInputDialog("Введите название книги");
            if (nname != null)
                booknamecheck(nname);
            else
                return;
            String newauth = JOptionPane.showInputDialog("Введите имя автора книги");
            if (newauth != null)
                namecheck(newauth);
            else
                return;

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Выбор фотографии обложки");
            int result = fileChooser.showOpenDialog(List);

            if(result!=JFileChooser.APPROVE_OPTION) return;
            File chosenFile = fileChooser.getSelectedFile();
            String path = chosenFile.getAbsolutePath();
            //Book book = em.find(Book.class, bk.getBook_id());
            //Book book = new Book();
            em.getTransaction().begin();

            bk.setPicpath(path);
            //em.persist(bk);
            bk.setName(nname);
            bk.setAuthor(newauth);
            em.persist(bk);
            em.getTransaction().commit();
            ImageIcon icon = pic_resize(path);
            model.addRow(new Object[]{icon, nname, newauth, bk.getBook_id(), "0", "0"});
            tbl.setRowHeight(tbl.getRowCount()-1, 100);
            //tbl.setValueAt(icon, k, 0);
            /*em.getTransaction().begin();

            em.persist(bk);
            em.getTransaction().commit();*/

            //model.addRow(new Object[]{nname, newauth, bk.getBook_id(), "0", "0"});
            i++;
        } catch (NameWithNumbersAndSymbolsException | NameStartingWithLowerCaseLetterException | EmptyNameException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Меняет значение выбранного пользователем поля. Вводится идентификационный номер книги, выбирается поле, которое нужно изменить и вводится новое значение.
     */
    public void editlist() {
        String edin;
        try {
            edin = JOptionPane.showInputDialog("Информацию о какой книге вы хотите изменить(введите ID)?");
            if (edin != null)
                idcheck(edin);
            else
                return;
            int k = 0;
            do {
                if (tbl.getValueAt(k, 3).toString().equals(edin)) break;
                k++;
            } while (k < i);
            int edop = JOptionPane.showOptionDialog(null, "Какое поле хотите изменить?", "Редактирование записи о книге",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Название", "Имя автора", "ID читателя", "Дата взятия книги", "Фотография"}, "Название");
            try {
                switch (edop) {
                    case 0 -> {
                        String newname = JOptionPane.showInputDialog("Введите новое название");
                        if (newname != null)
                            booknamecheck(newname);
                        else
                            return;
                        tbl.setValueAt(newname, k, 1);
                    }
                    case 1 -> {
                        String newauthor = JOptionPane.showInputDialog("Введите имя автора");
                        if (newauthor != null)
                            namecheck(newauthor);
                        else
                            return;
                        tbl.setValueAt(newauthor, k, 2);
                    }
                    case 2 -> {
                        String newrid = JOptionPane.showInputDialog("Введите ID читателя");
                        if (newrid != null)
                            idcheck(newrid);
                        else
                            return;
                        tbl.setValueAt(newrid, k, 4);
                    }
                    case 3 -> {
                        String newbd = JOptionPane.showInputDialog("Введите дату взятия книги в формате yyyy-mm-dd");
                        if (newbd != null)
                            datecheck(newbd);
                        else
                            return;
                        tbl.setValueAt(newbd, k, 5);
                    }
                    case 4 -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Выбор фотографии обложки");
                        int result = fileChooser.showOpenDialog(List);

                        if(result!=JFileChooser.APPROVE_OPTION) return;
                        File chosenFile = fileChooser.getSelectedFile();
                        String path = chosenFile.getAbsolutePath();
                        BufferedImage bufferedImage = ImageIO.read(new File(path));
                        Book book = em.find(Book.class, Integer.valueOf(edin));
                        ImageIcon outputImage = pic_resize(path);
                        em.getTransaction().begin();
                        book.setPicpath(path);
                        em.persist(book);
                        em.getTransaction().commit();
                        tbl.setValueAt(outputImage, k, 0);
                        tbl.setRowHeight(k, 100);
                    }
                }
                i++;
            } catch (NameWithNumbersAndSymbolsException | NameStartingWithLowerCaseLetterException | EmptyNameException | IDContainingLettersException |
                     EmptyIDException | EmptyDateException | WrongDateTypeException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IDContainingLettersException | EmptyIDException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    /**
     * Назначает посказки для кнопок на панели,
     * добавляет колонки в таблицу и вызывает функции-события кнопок панели.
     */
    public void showbk() throws IOException {
        datains();
        save.setToolTipText("Сохранить список книг");
        add.setToolTipText("Добавить книгу");
        edit.setToolTipText("Редактировать записи о книге");
        delete.setToolTipText("Удалить книгу");
        report.setToolTipText("Создать отчет");
        chngtbl.setText("К таблице читателей");


        srch.addActionListener(event -> search());
        delete.addActionListener(event -> dellist());
        save.addActionListener(event -> savelist());
        add.addActionListener(event -> addlist());
        edit.addActionListener(event -> editlist());
        /*savefile.addActionListener(event -> savefile());
        load.addActionListener(event ->load());*/
        chngtbl.addActionListener(event -> change_table());
        report.addActionListener(event -> report());

        List.validate();
    }
}
