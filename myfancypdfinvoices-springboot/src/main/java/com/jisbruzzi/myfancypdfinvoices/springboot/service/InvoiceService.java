package com.jisbruzzi.myfancypdfinvoices.springboot.service;

import com.jisbruzzi.myfancypdfinvoices.springboot.model.Invoice;
import com.jisbruzzi.myfancypdfinvoices.springboot.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InvoiceService {
	private final String cdnUrl;
	private UserService userService;
	private List<Invoice> invoices = new CopyOnWriteArrayList<>();

	public InvoiceService(UserService userService, @Value("${cdn.url}") String cdnUrl) {
		this.userService = userService;
		this.cdnUrl = cdnUrl;
	}

	@PostConstruct
	public void init() {
		System.out.println("Fetching PDF template from S3...");
	}

	@PreDestroy
	public void shutdown() {
		System.out.println("Deleting templates...");
	}

	public Invoice create(String userId, Integer amount) {
		User user = userService.findById(userId);
		if(user==null){
			throw new IllegalStateException();
		}

		Invoice invoice = new Invoice(userId,amount,cdnUrl+ "/images/default/sample.pdf");
		invoices.add(invoice);
		return invoice;
	}

	public List<Invoice> findAll() {
		return invoices;
	}
}
