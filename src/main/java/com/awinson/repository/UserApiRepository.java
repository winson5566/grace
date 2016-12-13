package com.awinson.repository;

import com.awinson.Entity.UserApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by winson on 2016/12/2.
 */
@Transactional
@Repository
public interface UserApiRepository extends CrudRepository<UserApi,String> {
       List<UserApi> findByUserId(String userId);

       UserApi findByPlatformAndApiType(String platform,String apiType);

       UserApi findByUserIdAndPlatformAndApiType(String userId,String Platform,String apiType);
}
