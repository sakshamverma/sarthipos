package com.aggarwalsweets.printer;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.aggarwalsweets.model.LineItem;
import com.aggarwalsweets.model.Order;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfPrinter implements IPrinter {

	private final String[] headers = { "PRODUCT", "QUANTITY" };

	private PdfPrinter() {
	}

	private static PdfPrinter pdfPrinter = null;

	public static PdfPrinter getPrinter() {
		if (pdfPrinter == null) {
			synchronized (PdfPrinter.class) {
				pdfPrinter = new PdfPrinter();
			}
		}
		return pdfPrinter;
	}

	@Override
	public void printOrder(Order order) throws Exception {
		Document  document = new Document();
		String fileFullPath = PrinterProperties.pdfOutputPath+"\\"+order.getOrderId()+".pdf";
		PdfWriter.getInstance(document, new FileOutputStream(fileFullPath));
		document.open();
		Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
		Chunk chunk = new Chunk("AGARWAL SWEETS", font);
		document.add(chunk);
		PdfPTable table = new PdfPTable(2);
		addTableHeader(table);
		addRows(table, order.getLineItems());

		document.add(table);
		document.close();		
	}

	private void addRows(PdfPTable table, List<LineItem> items) {
		for (LineItem item : items) {
			table.addCell(item.getName());
			table.addCell(item.getQuantity() + "");
		}
	}

	private void addTableHeader(PdfPTable table) {
		Arrays.stream(headers).forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			table.addCell(header);
		});
	}

	@Override
	public Map<String, Integer> getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

}
