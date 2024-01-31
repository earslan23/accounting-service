package com.accounting.ilab.entity;

import com.accounting.ilab.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@NoArgsConstructor
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "gen_seq_product",
            sequenceName = "seq_product",
            allocationSize = 100
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_seq_product")
    private Long id;

    private String description;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(nullable = false)
    private String name;


    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
