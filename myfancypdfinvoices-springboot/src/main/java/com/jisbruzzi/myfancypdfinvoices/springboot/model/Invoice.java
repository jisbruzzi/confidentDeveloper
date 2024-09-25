package com.jisbruzzi.myfancypdfinvoices.springboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;
@Table("invoices")
public class Invoice {
	@JsonProperty("pdf_url")
	private String pdfUrl;
	private Integer amount;
	@Id
	private String id;

	@JsonProperty("user_id")
	private String userId;

	public Invoice(String pdfUrl, Integer amount, String userId) {
		this.pdfUrl = pdfUrl;
		this.amount = amount;
		this.id = UUID.randomUUID().toString();
		this.userId = userId;
	}

	public Invoice() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
