package cn.tycoding.dao;

import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.User;
import cn.tycoding.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private UserMapper userMapper;

    /**
     * 添加用户-角色关系
     *
     * @param userId
     * @param roleIds
     */
    public void correlationRoles(Long userId, Long... roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return;
        }
        for (Long roleId: roleIds) {
            if (!exists(userId, roleId)) {
                userMapper.correlationRoles(userId, roleId);
            }
        }
    }

    /**
     * 移除用户-角色关系
     *
     * @param userId
     * @param roleIds
     */
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return;
        }
        for (Long roleId: roleIds) {
            if (exists(userId, roleId)) {
                userMapper.uncorrelationRoles(userId, roleId);
            }
        }
    }

    /**
     * 判断当前的用户和角色是否存在
     *
     * @param userId
     * @param roleId
     * @return
     */
    public boolean exists(Long userId, Long roleId) {
        return userMapper.exists(userId, roleId);
    }

    @Override
    public List<Role> findRoles(String username) {
        return userMapper.findRoles(username);
    }

    @Override
    public List<Permission> findPermissions(String username) {
        return userMapper.findPermissions(username);
    }

    @Override
    public void create(User user) {
        userMapper.create(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.delete(id);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findByName(String username) {
        User user = userMapper.findByName(username);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userMapper.findById(id);

        // 因为数据库中`locked`字段使用的类型：`tinyint(1)`，
        // 那么使用mybatis查询数据库会自动将数据转换成boolean类型(使用了boolean类型接收)，0：false；1或其他非零数字：true
        System.out.println("是否锁定：" + user.getLocked());

        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public void deleteAllUserRoles(Long id) {
        userMapper.deleteAllUserRoles(id);
    }

}
