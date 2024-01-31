package com.accounting.ilab.repos;

import com.accounting.ilab.entity.InvoiceAlerts;
import com.accounting.ilab.model.enums.InvoiceNotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


@org.springframework.stereotype.Repository
public interface InvoiceAlertsRepository extends JpaRepository<InvoiceAlerts, Long> {

    Page<InvoiceAlerts> findByStatus(InvoiceNotificationStatus invoiceNotificationStatus, Pageable pageable);

}