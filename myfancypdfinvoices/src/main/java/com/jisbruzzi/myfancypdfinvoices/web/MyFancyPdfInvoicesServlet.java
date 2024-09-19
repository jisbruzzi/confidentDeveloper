package com.jisbruzzi.myfancypdfinvoices.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jisbruzzi.myfancypdfinvoices.context.MyFancyPdfInvoicesApplicationConfiguration;
import com.jisbruzzi.myfancypdfinvoices.model.Invoice;
import com.jisbruzzi.myfancypdfinvoices.service.InvoiceService;
import com.jisbruzzi.myfancypdfinvoices.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.List;

public class MyFancyPdfInvoicesServlet extends HttpServlet {

	private InvoiceService invoiceService;
	private ObjectMapper objectMapper;
	private UserService userService;

	@Override
	public void init() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyFancyPdfInvoicesApplicationConfiguration.class);
		ctx.registerShutdownHook();

		this.userService = ctx.getBean(UserService.class);
		this.objectMapper = ctx.getBean(ObjectMapper.class);
		this.invoiceService = ctx.getBean(InvoiceService.class);
	}

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
			List<Invoice> invoices = invoiceService.findAll();
			resp.getWriter().print(objectMapper.writeValueAsString(invoices));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getRequestURI().equalsIgnoreCase("/invoices")) {
			String userId = req.getParameter("user_id");
			Integer amount = Integer.valueOf(req.getParameter("amount"));
			Invoice invoice = invoiceService.create(userId, amount);
			resp.setContentType("application/json; charset=UTF-8");
			String json = objectMapper.writeValueAsString(invoice);
			resp.getWriter().print(json);
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
