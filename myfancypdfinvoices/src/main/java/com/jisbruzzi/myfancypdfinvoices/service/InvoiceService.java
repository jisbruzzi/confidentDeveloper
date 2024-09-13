package com.jisbruzzi.myfancypdfinvoices.service;

import com.jisbruzzi.myfancypdfinvoices.context.Application;
import com.jisbruzzi.myfancypdfinvoices.model.Invoice;
import com.jisbruzzi.myfancypdfinvoices.model.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InvoiceService {
	private List<Invoice> invoices = new CopyOnWriteArrayList<>();
	private UserService userService;

	public InvoiceService(UserService userService) {
		this.userService = userService;
	}

	public Invoice create(String userId, Integer amount) {
		User user = userService.findById(userId);
		if (user == null) {
			throw new IllegalStateException();
		}

		Invoice invoice = new Invoice("http://www.africau.edu/images/default/sample.pdf", amount, userId);
		invoices.add(invoice);
		return invoice;
	}

	public List<Invoice> findAll() {
		return invoices;
	}
}
