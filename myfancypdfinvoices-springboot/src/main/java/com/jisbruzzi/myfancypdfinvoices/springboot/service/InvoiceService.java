package com.jisbruzzi.myfancypdfinvoices.springboot.service;

import com.jisbruzzi.myfancypdfinvoices.springboot.model.Invoice;
import com.jisbruzzi.myfancypdfinvoices.springboot.repository.InvoiceRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class InvoiceService {
	private final String cdnUrl;
	private UserService userService;
	private JdbcTemplate jdbcTemplate;

	private InvoiceRepository invoiceRepository;

	public InvoiceService(UserService userService, @Value("${cdn.url}") String cdnUrl, JdbcTemplate jdbcTemplate,
			InvoiceRepository invoiceRepository
	) {
		this.userService = userService;
		this.cdnUrl = cdnUrl;
		this.jdbcTemplate = jdbcTemplate;
		this.invoiceRepository = invoiceRepository;
	}

	@PostConstruct
	public void init() {
		System.out.println("Fetching PDF template from S3...");
	}

	@PreDestroy
	public void shutdown() {
		System.out.println("Deleting templates...");
	}

	@Transactional
	public Iterable<Invoice> findAll() {
		return invoiceRepository.findAll();
	}

	@Transactional
	public Invoice create(String userId, Integer amount) {
		String generatedPdfUrl = cdnUrl + "/images/default/sample.pdf";

		Invoice invoice = new Invoice();
		invoice.setPdfUrl(generatedPdfUrl);
		invoice.setAmount(amount);
		invoice.setUserId(userId);

		return invoiceRepository.save(invoice);
	}

	@Transactional
	public Iterable<Invoice> getByUserId(String userId) {
		return invoiceRepository.findByUserId(userId);
	}
}
