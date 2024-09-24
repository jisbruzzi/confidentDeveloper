package com.jisbruzzi.myfancypdfinvoices.springboot.service;

import com.jisbruzzi.myfancypdfinvoices.springboot.model.Invoice;
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

	public InvoiceService(UserService userService, @Value("${cdn.url}") String cdnUrl, JdbcTemplate jdbcTemplate) {
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
	public List<Invoice> findAll() {
		System.out.println("Is a database transaction open? = " + TransactionSynchronizationManager.isActualTransactionActive());
		return jdbcTemplate.query("select id, user_id, pdf_url, amount from invoices", (resultSet, rowNum) -> {
			Invoice invoice = new Invoice();
			invoice.setId(resultSet.getObject("id").toString());
			invoice.setPdfUrl(resultSet.getString("pdf_url"));
			invoice.setUserId(resultSet.getString("user_id"));
			invoice.setAmount(resultSet.getInt("amount"));
			return invoice;
		});
	}

	@Transactional
	public Invoice create(String userId, Integer amount) {
		System.out.println("Is a database transaction open? = " + TransactionSynchronizationManager.isActualTransactionActive());
		String generatedPdfUrl = cdnUrl + "/images/default/sample.pdf";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection
					.prepareStatement("insert into invoices (user_id, pdf_url, amount) values (?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, userId);  //
			ps.setString(2, generatedPdfUrl);
			ps.setInt(3, amount);
			return ps;
		}, keyHolder);

		String uuid = !keyHolder.getKeys().isEmpty() ? keyHolder.getKeys().values().iterator().next().toString()
				: null;

		Invoice invoice = new Invoice();
		invoice.setId(uuid);
		invoice.setPdfUrl(generatedPdfUrl);
		invoice.setAmount(amount);
		invoice.setUserId(userId);
		return invoice;
	}
}
