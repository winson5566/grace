package com.awinson.repository;

import com.awinson.Entity.Role;
import com.awinson.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by winson on 2016/12/2.
 */
@Transactional
@Repository
public interface RoleRepository extends CrudRepository<Role,String> {
}
