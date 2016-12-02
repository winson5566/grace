package com.awinson.repository;

import com.awinson.Entity.PriceHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by winson on 2016/12/2.
 */
@Transactional
@Repository
public interface PriceHistoryRepository extends CrudRepository<PriceHistory,String> {
    public PriceHistory findByType(Integer type);

}
