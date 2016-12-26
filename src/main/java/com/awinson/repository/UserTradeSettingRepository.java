package com.awinson.repository;

import com.awinson.Entity.UserTradeSetting;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by winson on 2016/12/2.
 */
@Transactional
@Repository
@RepositoryDefinition(domainClass = UserTradeSetting.class,idClass = String.class)
public interface UserTradeSettingRepository {

    @Cacheable(value="userTradeSetting", key="#p0")
    UserTradeSetting findByUserId(String userId);

    @CacheEvict(value="userTradeSetting", key="#p0.userId")
    void save(UserTradeSetting userTradeSetting);

}
