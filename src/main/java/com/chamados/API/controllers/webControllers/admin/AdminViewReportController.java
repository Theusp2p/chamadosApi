package com.chamados.API.controllers.webControllers.admin;

import com.chamados.API.entities.SupportTicket;
import com.chamados.API.services.SupportTicketService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/admin/relatorios")
@RequiredArgsConstructor
public class AdminViewReportController {

    private final SupportTicketService ticketService;

    @GetMapping
    public String getRelatorioPorData(@RequestParam(value = "data", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                      LocalDate data,
                                      Model model) {
        if (data == null) {
            data = LocalDate.now();
        }

        LocalDateTime startOfDay = data.atStartOfDay();
        LocalDateTime endOfDay = data.plusDays(1).atStartOfDay();

        List<SupportTicket> tickets = ticketService.findCloseTicketsByDate(startOfDay, endOfDay);
        model.addAttribute("tickets", tickets);
        model.addAttribute("dataSelecionada", data);
        return "admin/relatorios/list";
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePdfReport(@RequestParam("data")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                    LocalDate data, Authentication authentication) {
        try {
            byte[] pdfBytes = generatePdf(data, authentication);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData(
                    "attachment",
                    String.format("relatorio-chamados-%s.pdf", data)
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar relatório PDF", e);
        }
    }

    private byte[] generatePdf(LocalDate data, Authentication authentication) throws DocumentException, IOException {
        LocalDateTime startOfDay = data.atStartOfDay();
        LocalDateTime endOfDay = data.plusDays(1).atStartOfDay();
        List<SupportTicket> tickets = ticketService.findCloseTicketsByDate(startOfDay, endOfDay);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, baos);

        document.open();
        addDocumentHeader(document, data);
        addResolvedTicketsSummary(document, data, tickets);
        addTicketsTable(document, tickets);
        addDocumentFooter(document, authentication);
        document.close();

        return baos.toByteArray();
    }

    private void addResolvedTicketsSummary(Document document, LocalDate data, List<SupportTicket> tickets) throws DocumentException {
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(50);
        summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        summaryTable.setSpacingBefore(20);
        summaryTable.setSpacingAfter(20);

        PdfPCell headerCell = new PdfPCell(new Phrase("ESTATÍSTICAS DO DIA",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        headerCell.setColspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8);
        summaryTable.addCell(headerCell);

        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        summaryTable.addCell(createSummaryCell("Total Resolvidos:", labelFont));
        summaryTable.addCell(createSummaryCell(String.valueOf(tickets.size()), valueFont));

        document.add(summaryTable);
        document.add(Chunk.NEWLINE);
    }

    private void addTicketsTable(Document document, List<SupportTicket> tickets) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2f, 1.5f, 1f, 1f, 1.5f, 1.5f});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        addTableHeaderCell(table, "Criado às", headerFont);
        addTableHeaderCell(table, "Assunto", headerFont);
        addTableHeaderCell(table, "Atribuído a", headerFont);
        addTableHeaderCell(table, "SLA Atend.", headerFont);
        addTableHeaderCell(table, "Tempo Resol.", headerFont);
        addTableHeaderCell(table, "Departamento", headerFont);
        addTableHeaderCell(table, "Solicitante", headerFont);

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        for (SupportTicket ticket : tickets) {
            addTableCell(table, ticket.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm")), cellFont);
            addTableCell(table, ticket.getObject(), cellFont);
            addTableCell(table, ticket.getAttributedTo() != null ? ticket.getAttributedTo() : "N/A", cellFont);
            addTableCell(table, calculateSlaTime(ticket), cellFont);
            addTableCell(table, calculateResolutionTime(ticket), cellFont);
            addTableCell(table, ticket.getDepartment().getName(), cellFont);
            addTableCell(table, ticket.getCreatedBy(), cellFont);
        }

        document.add(table);
    }

    private String calculateSlaTime(SupportTicket ticket) {
        if (ticket.getStartOfService() == null) {
            return "Não atendido";
        }
        long minutes = ChronoUnit.MINUTES.between(ticket.getCreatedAt(), ticket.getStartOfService());
        return formatDuration(minutes);
    }

    private String calculateResolutionTime(SupportTicket ticket) {
        if (ticket.getStartOfService() == null || ticket.getUpdatedAt() == null) {
            return "N/A";
        }
        long minutes = ChronoUnit.MINUTES.between(ticket.getStartOfService(), ticket.getUpdatedAt());
        return formatDuration(minutes);
    }

    private String formatDuration(long minutes) {
        if (minutes < 60) {
            return minutes + "m";
        }
        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;
        return hours + "h" + (remainingMinutes < 10 ? "0" : "") + remainingMinutes + "m";
    }

    private void addDocumentHeader(Document document, LocalDate data) throws DocumentException, IOException {
        Image logo = Image.getInstance(getClass().getResource("/static/icons/LogoFinal.png"));
        logo.scaleToFit(150, 150);
        logo.setAlignment(Element.ALIGN_CENTER);
        document.add(logo);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("RELATÓRIO DE CHAMADOS FECHADOS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        String formattedDate = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Paragraph dateParagraph = new Paragraph("Data do Relatório: " + formattedDate, dateFont);
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        dateParagraph.setSpacingAfter(20);
        document.add(dateParagraph);

        document.add(new Chunk(new LineSeparator()));
        document.add(Chunk.NEWLINE);
    }

    private void addDocumentFooter(Document document, Authentication authentication) throws DocumentException {
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        String generationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Paragraph footer = new Paragraph("Relatório gerado em: " + generationDate + " por " + authentication.getName(), footerFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);
    }

    private PdfPCell createSummaryCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private void addTableHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell header = new PdfPCell(new Phrase(text, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPadding(5);
        table.addCell(header);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}