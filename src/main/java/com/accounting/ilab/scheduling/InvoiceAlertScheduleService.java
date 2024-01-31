package com.accounting.ilab.scheduling;


import com.accounting.ilab.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InvoiceAlertScheduleService {

    private final InvoiceService invoiceService;

    @Scheduled(cron = "${service.invoice-alert.scheduleTime}")
    public void alertRejectedInvoiceToSecurity() {
        invoiceService.alertRejectedInvoiceToSecurity();
    }

}
