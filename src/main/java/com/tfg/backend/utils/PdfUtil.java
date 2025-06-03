package com.tfg.backend.utils;

import com.tfg.backend.dto.InformeEquipajeResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfUtil {
    public static byte[] generarPdfDesdeInforme(List<InformeEquipajeResponse> informe) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            for (InformeEquipajeResponse dia : informe) {
                document.add(new Paragraph(dia.getTexto()));
                document.add(new Paragraph("\n"));
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }
}
