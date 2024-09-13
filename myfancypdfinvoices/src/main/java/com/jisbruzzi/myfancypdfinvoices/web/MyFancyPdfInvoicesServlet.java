package com.jisbruzzi.myfancypdfinvoices.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jisbruzzi.myfancypdfinvoices.context.Application;
import com.jisbruzzi.myfancypdfinvoices.model.Invoice;
import com.jisbruzzi.myfancypdfinvoices.service.InvoiceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MyFancyPdfInvoicesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getRequestURI().equalsIgnoreCase("/")) {
			resp.setContentType("text/html; charset=UTF-8");
			resp.getWriter().print("""
					<html>
					<body>
					<h1>Hello world!!!</h1
					<p>Embedded tmocat!</p>
					</body>
					</html>""");
		} else if (req.getRequestURI().equalsIgnoreCase("/invoices")) {
			resp.setContentType("application/json; charset=UTF-8");
			List<Invoice> invoices = Application.invoiceService.findAll();
			resp.getWriter().print(Application.objectMapper.writeValueAsString(invoices));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getRequestURI().equalsIgnoreCase("/invoices")) {
				String userId = req.getParameter("user_id");
			Integer amount = Integer.valueOf(req.getParameter("amount"));
			Invoice invoice = Application.invoiceService.create(userId, amount);
			resp.setContentType("application/json; charset=UTF-8");
			String json = Application.objectMapper.writeValueAsString(invoice);
			resp.getWriter().print(json);
		}else{
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
