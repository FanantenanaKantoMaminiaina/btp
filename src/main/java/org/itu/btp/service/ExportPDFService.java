package org.itu.btp.service;

import com.lowagie.text.DocumentException;
import org.itu.btp.model.Devis;
import org.itu.btp.model.MaisonTravaux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExportPDFService {
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public ExportPDFService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdfDevis(Devis devis, List<MaisonTravaux> listTravaux) throws DocumentException {
        Context context = new Context();
        context.setVariable("devis", devis);
        context.setVariable("listTravaux", listTravaux);

        String htmlContent = templateEngine.process("admin/devis/detailDevisPDF", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent, null);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
}
