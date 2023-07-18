package com.increff.invoiceapp.service;


import com.increff.invoiceapp.model.InvoiceForm;
import com.increff.invoiceapp.model.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateInvoiceService {
    public void generateInvoice(InvoiceForm form)
    {
        List<OrderItem> items = form.getOrderItemList();
        Double amt = 0.0;
        for(OrderItem i : items) {
            Double cur = 0.0;
            cur = i.getSellingPrice() * i.getQuantity();
            amt+=cur;
            i.setAmt(cur);
        }
        form.setAmount(amt);
        CreateXMLFileJava createXMLFileJava = new CreateXMLFileJava();

        createXMLFileJava.createXML(form);

        PDFFromFOP pdfFromFOP = new PDFFromFOP();

        pdfFromFOP.createPDF();
    }


}
