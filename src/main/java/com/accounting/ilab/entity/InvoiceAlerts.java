package com.accounting.ilab.entity;

import com.accounting.ilab.entity.base.BaseEntity;
import com.accounting.ilab.model.enums.InvoiceNotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@NoArgsConstructor
@Entity
@Table(name = "invoice_alerts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@AllArgsConstructor
public class InvoiceAlerts extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "gen_seq_invoice_alerts",
            sequenceName = "seq_invoice_alerts",
            allocationSize = 100
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_seq_invoice_alerts")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    @ToString.Exclude
    private Invoice invoice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceNotificationStatus status;

    @PrePersist
    public void init() {
        this.status = InvoiceNotificationStatus.PENDING;
    }

}
