package com.jisbruzzi.myfancypdfinvoices.service;

import com.jisbruzzi.myfancypdfinvoices.model.Invoice;
import com.jisbruzzi.myfancypdfinvoices.model.User;
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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InvoiceService {
	private final String cdnUrl;
	private final JdbcTemplate jdbcTemplate;
	private UserService userService;

	public InvoiceService(UserService userService, JdbcTemplate jdbcTemplate, @Value("${cdn.url}") String cdnUrl) {
		this.userService = userService;
		this.cdnUrl = cdnUrl;
		this.jdbcTemplate = jdbcTemplate;
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
	public Invoice create(String userId, Integer amount) {
		System.out.println("Is a database transaction open? = " + TransactionSynchronizationManager.isActualTransactionActive());
		String generatedPdfUrl = cdnUrl + "/images/default/sample.pdf";
		User user = userService.findById(userId);
		if (user == null) {
			throw new IllegalStateException();
		}

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO invoices (user_id, pdf_url, amount) VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, userId);
			ps.setString(2, generatedPdfUrl);
			ps.setInt(3, amount);
			return ps;
		}, keyHolder);

		String uuid = !keyHolder.getKeys().isEmpty() ? keyHolder.getKeys().values().iterator().next().toString() : null;

		Invoice invoice = new Invoice();
		invoice.setId(uuid);
		invoice.setPdfUrl(generatedPdfUrl);
		invoice.setAmount(amount);
		invoice.setUserId(userId);

		return invoice;
	}

	@Transactional
	public List<Invoice> findAll() {
		System.out.println("Is a database transaction open? = " + TransactionSynchronizationManager.isActualTransactionActive());
		return jdbcTemplate.query("SELECT id, user_id, pdf_url, amount FROM invoices", (resultSet, rowNumber) -> {
			Invoice invoice = new Invoice();
			invoice.setId(resultSet.getObject("id").toString());
			invoice.setPdfUrl(resultSet.getString("pdf_url"));
			invoice.setUserId(resultSet.getString("user_id"));
			invoice.setAmount(resultSet.getInt("amount"));
			return invoice;
		});
	}
}
