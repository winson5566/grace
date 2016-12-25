package com.awinson.repository;

import com.awinson.Entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by winson on 2016/12/2.
 */
@Transactional
@Repository
@RepositoryDefinition(domainClass = User.class,idClass = String.class)
public interface UserRepository {

    @Cacheable(value="enableUser", key="#p0")
    List<User> findByEnable(String enable);

    User findByUsername(String username);

    @CacheEvict(value="enableUser", key="#p0.enable")
    void save(User user);
}
