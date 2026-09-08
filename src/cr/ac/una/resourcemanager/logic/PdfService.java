package cr.ac.una.resourcemanager.logic;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import cr.ac.una.resourcemanager.Application;

import javax.swing.JTable;
import java.awt.Desktop;
import java.io.File;

public class PdfService {

    public void print(String dest, String titulo, String rutaImagen, JTable tablaSwing) throws Exception {
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.setMargins(20, 20, 28, 20);

        Table header = new Table(1);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);
        header.addCell(getCell(new Paragraph(titulo).setFont(font).setBold().setFontSize(20f), TextAlignment.CENTER, false));

        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            try {
                Image img = new Image(ImageDataFactory.create(Application.class.getResource(rutaImagen)));
                header.addCell(getCell(img, HorizontalAlignment.CENTER, false));
            } catch (Exception e) {
                System.out.println("Imagen no encontrada: " + rutaImagen);
            }
        }

        document.add(header);

        int totalColumnas = tablaSwing.getColumnCount();
        if (totalColumnas > 0) {
            Table tablaDatos = new Table(totalColumnas);
            tablaDatos.useAllAvailableWidth();
            tablaDatos.setMarginTop(20);

            for (int i = 0; i < totalColumnas; i++) {
                tablaDatos.addHeaderCell(getCell(new Paragraph(tablaSwing.getColumnName(i)).setFont(font).setBold(), TextAlignment.CENTER, true));
            }

            for (int fila = 0; fila < tablaSwing.getRowCount(); fila++) {
                for (int col = 0; col < totalColumnas; col++) {
                    Object valor = tablaSwing.getValueAt(fila, col);
                    tablaDatos.addCell(getCell(new Paragraph(valor != null ? valor.toString() : "").setFont(font), TextAlignment.LEFT, true));
                }
            }
            document.add(tablaDatos);
        }

        document.close();

        openPdf(dest);
    }

    private Cell getCell(Paragraph paragraph, TextAlignment alignment, boolean hasBorder) {
        Cell cell = new Cell().add(paragraph);
        cell.setPadding(8);
        cell.setTextAlignment(alignment);
        if (!hasBorder) cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    private Cell getCell(Image image, HorizontalAlignment alignment, boolean hasBorder) {
        Cell cell = new Cell().add(image);
        image.setHorizontalAlignment(alignment);
        cell.setPadding(0);
        if (!hasBorder) cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    private void openPdf(String path) {
        try {
            File pdfFile = new File(path);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("La función de escritorio no está soportada en este sistema operativo.");
                }
            } else {
                System.out.println("El archivo PDF de destino no existe o no se pudo crear.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al intentar abrir el archivo PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
