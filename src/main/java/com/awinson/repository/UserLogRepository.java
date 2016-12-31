package com.awinson.repository;

import com.awinson.Entity.UserLog;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by 10228 on 2016/12/29.
 */
@Transactional
@Repository
@RepositoryDefinition(domainClass = UserLog.class, idClass = String.class)
public interface UserLogRepository {


    //List<UserLog> findByUserId(String userId);

    void save(UserLog userLog);

    List<UserLog> findByUserIdAndType(String userId,String type);

    List<UserLog> findTop20ByUserIdAndTypeOrderByCreateTimestampDesc(String userId,String type);

}
