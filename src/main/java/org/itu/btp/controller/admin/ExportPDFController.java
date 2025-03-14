package org.itu.btp.controller.admin;

import com.lowagie.text.DocumentException;
import org.itu.btp.model.Devis;
import org.itu.btp.service.DevisService;
import org.itu.btp.service.ExportPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin/exportPDF")
public class ExportPDFController {
    private final ExportPDFService exportPDFService;
    private final DevisService devisService;

    @Autowired
    public ExportPDFController(ExportPDFService exportPDFService,DevisService devisService) {
        this.exportPDFService = exportPDFService;
        this.devisService = devisService;
    }

    @GetMapping("/devis/{id}")
    public ResponseEntity<byte[]> exportDevis(@PathVariable Integer id) throws DocumentException {
        Optional<Devis> optionalDevis = devisService.getDevisById(id);

        if (optionalDevis.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Devis devis = optionalDevis.get();

        byte[] pdfContent = exportPDFService.generatePdfDevis(devis, devisService.getListTravauxByDevis(devis));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=devis.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

}
