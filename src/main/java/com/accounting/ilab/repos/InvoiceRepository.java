package com.accounting.ilab.repos;



import com.accounting.ilab.entity.Invoice;
import com.accounting.ilab.model.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT SUM(inv.product.price) FROM Invoice inv WHERE inv.user.id =:userId and inv.status = com.accounting.ilab.model.enums.InvoiceStatus.APPROVED")
    Optional<BigDecimal> getSumOfApprovedInvoicesByUserId(@Param("userId") final Long userId);

    @EntityGraph(attributePaths = {"product"})
    Page<Invoice> findByStatus(final InvoiceStatus invoiceStatus, final Pageable pageable);

}
