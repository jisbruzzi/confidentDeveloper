package com.jisbruzzi.myfancypdfinvoices.springboot.repository;

import com.jisbruzzi.myfancypdfinvoices.springboot.model.Invoice;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface InvoiceRepository extends CrudRepository<Invoice, String> {
	@Query("SELECT id, pdf_url, user_id, amount FROM \"invoices\" where user_id=:userId")
	Iterable<Invoice> findByUserId(@Param("userId") String userId);
}
