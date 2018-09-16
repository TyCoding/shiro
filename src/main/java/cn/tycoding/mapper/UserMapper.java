package cn.tycoding.mapper;

import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
public interface UserMapper {

    void correlationRoles(@Param("userId") Long userId, @Param("roleId") Long roleId);

    void uncorrelationRoles(@Param("userId") Long userId, @Param("roleId") Long roleId);

    List<Role> findRoles(String username);

    List<Permission> findPermissions(String username);

    boolean exists(@Param("userId") Long userId, @Param("roleId") Long roleId);

    void create(User user);

    void delete(Long id);

    void update(User user);

    List<User> findAll();

    User findByName(String username);

    User findById(Long id);

    void deleteAllUserRoles(Long id);
}
