package com.codegym.foody.service.impl;

import com.codegym.foody.model.dto.PaginationResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    public <T> PaginationResult calculatePagination(Page<T> page) {
        int totalPages = page.getTotalPages();
        int currentPage = page.getNumber();

        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 3);
        List<Integer> pageNumbers = IntStream.range(start, end).boxed().toList();

        return new PaginationResult(totalPages, currentPage, pageNumbers);
    }
}
