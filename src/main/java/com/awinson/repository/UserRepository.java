package com.awinson.repository;

import com.awinson.Entity.PriceHistory;
import com.awinson.Entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by winson on 2016/12/2.
 */
@Transactional
@Repository
public interface UserRepository extends CrudRepository<User,String> {
    User findByUsername(String username);

}
