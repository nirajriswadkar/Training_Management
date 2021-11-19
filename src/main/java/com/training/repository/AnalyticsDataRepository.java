package com.training.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.entity.AnalyticsData;

@Repository
public interface AnalyticsDataRepository extends JpaRepository<AnalyticsData, Long>{

    public <T> T findApplicationTitleBy(Class<T> clazz);
    public <T> T findAllBy(Class<T> clazz);
    public List<AnalyticsData> findByEntitytype(String entitytype, Sort sortByViewcountAsc);
}