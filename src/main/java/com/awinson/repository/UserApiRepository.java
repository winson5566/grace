package com.awinson.repository;

import com.awinson.Entity.UserApi;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by winson on 2016/12/2.
 */
@Transactional
@Repository
@RepositoryDefinition(domainClass = UserApi.class, idClass = String.class)
public interface UserApiRepository{

       @Cacheable(value="byUserId",key="#userId")
       List<UserApi> findByUserId(String userId);

       UserApi findByPlatformAndApiType(String platform,String apiType);

       UserApi findByUserIdAndPlatformAndApiType(String userId,String Platform,String apiType);

       @CacheEvict(value="byUserId",key="#userApi.userId")
       void save(UserApi userApi);
}
