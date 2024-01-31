package com.accounting.ilab.model.response.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TPage<T> {

    private int totalPages;
    private Long totalElements;
    private Sort sort;
    private int number;
    private int size;
    private List<T> content;

    public void setStat(Page page, List<T> list) {
        this.totalPages = page.getTotalPages() ;
        this.totalElements = page.getTotalElements() ;
        this.sort = page.getSort() ;
        this.number =page.getNumber() ;
        this.size = page.getSize() ;
        this.content = list;
    }
}