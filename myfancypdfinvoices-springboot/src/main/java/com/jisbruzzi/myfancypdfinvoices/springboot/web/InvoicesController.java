package com.jisbruzzi.myfancypdfinvoices.springboot.web;

import com.jisbruzzi.myfancypdfinvoices.springboot.dto.InvoiceDto;
import com.jisbruzzi.myfancypdfinvoices.springboot.model.Invoice;
import com.jisbruzzi.myfancypdfinvoices.springboot.service.InvoiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvoicesController {
	private InvoiceService invoiceService;

	public InvoicesController(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}

	@GetMapping("/invoices")
	public List<Invoice> getAll() {
		return List.copyOf(invoiceService.findAll());
	}

	@PostMapping("/invoices")
	public Invoice create(@RequestBody InvoiceDto invoiceDto) {
		return invoiceService.create(invoiceDto.getUserId(), invoiceDto.getAmount());
	}
}
