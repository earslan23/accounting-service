package com.accounting.ilab.service;


import com.accounting.ilab.entity.Invoice;
import com.accounting.ilab.entity.InvoiceAlerts;
import com.accounting.ilab.entity.Product;
import com.accounting.ilab.entity.User;
import com.accounting.ilab.exception.BusinessException;
import com.accounting.ilab.mapper.InvoiceMapper;
import com.accounting.ilab.model.enums.InvoiceNotificationStatus;
import com.accounting.ilab.model.enums.InvoiceStatus;
import com.accounting.ilab.model.request.InvoiceRequestDto;
import com.accounting.ilab.model.response.InvoiceResponseDto;
import com.accounting.ilab.model.response.ProductResponseDto;
import com.accounting.ilab.model.response.base.TPage;
import com.accounting.ilab.repos.InvoiceAlertsRepository;
import com.accounting.ilab.repos.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceAlertsRepository invoiceAlertsRepository;

    private final ProductService productService;
    private final MessageService messageService;

    private final InvoiceMapper invoiceMapper;

    private final UserService userService;

    @Value("${invoice.amount.limit}")
    private BigDecimal amountLimit;

    @Value("${service.invoice-alert.pageSize}")
    private int pageSize;

    @Value("${service.invoice-alert.max-retry-count}")
    private int maxRetryCount;


    /**
     * @param invoiceRequestDto check product and invoice limit conditions
     * @return
     */

    public InvoiceResponseDto addInvoice(final InvoiceRequestDto invoiceRequestDto) {
        final ProductResponseDto product = productService.getProductById(invoiceRequestDto.productId());
        return checkInvoiceAndAdd(invoiceRequestDto, product);
    }

    /**
     * @param invoiceRequestDto
     * @param product           check limit conditions ; if the limit is insufficient, add record to the InvoiceAlerts table
     * @return
     */

    @Transactional(noRollbackFor = {BusinessException.class})
    public InvoiceResponseDto checkInvoiceAndAdd(final InvoiceRequestDto invoiceRequestDto, final ProductResponseDto product) {

        boolean isApprovableInvoice = true;

        final User user = userService.getCurrentUser();

        isApprovableInvoice = checkInvoiceStatus(invoiceRequestDto, isApprovableInvoice, user.getId());

        final Invoice invoice = getInvoice(invoiceRequestDto, product, isApprovableInvoice, user.getId());

        final Invoice savedInvoice = invoiceRepository.save(invoice);

        checkAndSaveInvoicesAlert(isApprovableInvoice, savedInvoice);

        log.info("Invoice successfully added {}", savedInvoice);
        if (!isApprovableInvoice) {
            throw new BusinessException(messageService.getLocaleMessage("error.message.invoice.limit"));
        }

        final ProductResponseDto foundedProduct = productService.getProductById(invoiceRequestDto.productId());
        savedInvoice.setProduct(new Product(foundedProduct.id(), foundedProduct.name()));
        return invoiceMapper.fromEntityToDto(savedInvoice);

    }

    public TPage<InvoiceResponseDto> getInvoicesByStatusPageable(final InvoiceStatus invoiceStatus, final Pageable pageable) {
        final Page<Invoice> invoicePage = invoiceRepository.findByStatus(invoiceStatus, pageable);
        final TPage page = new TPage<InvoiceResponseDto>();
        final var invoices = invoiceMapper.fromEntityListToDtoList(invoicePage.getContent());
        page.setStat(invoicePage, invoices);
        log.info("Invoices listed {} " , log);
        return page;
    }


    public void alertRejectedInvoiceToSecurity() {

        int retryCount = 0;
        Page<InvoiceAlerts> invoiceAlertPage;

        while (retryCount < maxRetryCount && (invoiceAlertPage = getPendingInvoiceAlertsPage(PageRequest.of(0, pageSize))).hasContent()) {
            sendInvoiceAlertsToSecurity(invoiceAlertPage.getContent());
            markInvoiceAlertsAsSent(invoiceAlertPage.getContent());
            retryCount++;
        }
    }

    private Page<InvoiceAlerts> getPendingInvoiceAlertsPage(Pageable pageable) {
        return invoiceAlertsRepository.findByStatus(InvoiceNotificationStatus.PENDING, pageable);
    }

    private void sendInvoiceAlertsToSecurity(List<InvoiceAlerts> invoiceAlerts) {
        // Send to Message Broker or do asynchronous job
        // Implement the logic to send alerts to security
    }

    private void markInvoiceAlertsAsSent(List<InvoiceAlerts> invoiceAlerts) {
        invoiceAlerts.forEach(alert -> alert.setStatus(InvoiceNotificationStatus.SENT));
        try {
            invoiceAlertsRepository.saveAll(invoiceAlerts);
        } catch (Exception e) {
            log.error("Schedule Invoice Alert Exception occured {}", e);
        }
    }


    private void checkAndSaveInvoicesAlert(final boolean isApprovableInvoice, final Invoice savedInvoice) {
        if (!isApprovableInvoice) {
            final InvoiceAlerts invoiceAlerts = new InvoiceAlerts();
            invoiceAlerts.setInvoice(savedInvoice);
            invoiceAlertsRepository.save(invoiceAlerts);
            log.info("Invoice successfully added to InvoiceAlerts {}", invoiceAlerts);
        }
    }

    private boolean checkInvoiceStatus(InvoiceRequestDto invoiceRequestDto, boolean isApprovableInvoice, Long userId) {
        Optional<BigDecimal> sumOfInvoicesByUserId = invoiceRepository.getSumOfApprovedInvoicesByUserId(userId);

        final ProductResponseDto product = productService.getProductById(invoiceRequestDto.productId());

        BigDecimal sumOfApprovedAmount = sumOfInvoicesByUserId.isPresent() ? sumOfInvoicesByUserId.get() : BigDecimal.ZERO;

        if (amountLimit.compareTo(sumOfApprovedAmount) <= 0 || amountLimit.subtract(sumOfApprovedAmount).compareTo(product.price()) < 0) {
            isApprovableInvoice = false;
        }
        return isApprovableInvoice;
    }

    private Invoice getInvoice(final InvoiceRequestDto invoiceRequestDto, final ProductResponseDto product, final boolean isApprovableInvoice, final Long userId) {
        final Invoice invoice = invoiceMapper.fromDtoToEntity(invoiceRequestDto);
        invoice.setUser(userService.getReferenceById(userId));
        invoice.setProduct(productService.getReferenceById(product.id()));
        invoice.setStatus(isApprovableInvoice ? InvoiceStatus.APPROVED : InvoiceStatus.REJECTED);
        return invoice;
    }


}
