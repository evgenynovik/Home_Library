package com.netcracker.education.dao.interfaces;

import com.netcracker.education.dao.domain.BookSeries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookSeriesDAO extends JpaRepository<BookSeries, Integer> {
}
