package com.awinson.repository;

import com.awinson.Entity.PriceMargin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by winson on 2016/12/2.
 */
@Transactional
@Repository
public interface PriceMarginRepository extends CrudRepository<PriceMargin,String> {
}
