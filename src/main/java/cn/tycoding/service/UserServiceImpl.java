package cn.tycoding.service;

import cn.tycoding.dao.UserDao;
import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordHelper passwordHelper;

    /**
     * 添加用户-角色关系
     *
     * @param userId
     * @param roleIds
     */
    public void correlationRoles(Long userId, Long... roleIds) {
        userDao.correlationRoles(userId, roleIds);
    }

    /**
     * 移除用户-角色关系
     *
     * @param userId
     * @param roleIds
     */
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        userDao.uncorrelationRoles(userId, roleIds);
    }

    /**
     * 根据用户名查找其角色
     *
     * @param username
     * @return
     */
    public List<Role> findRoles(String username) {
        return userDao.findRoles(username);
    }

    /**
     * 根据用户名查找其权限
     *
     * @param username
     * @return
     */
    public List<Permission> findPermissions(String username) {
        return userDao.findPermissions(username);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        User user = userDao.findById(id);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.update(user);
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public void create(User user) {
        //加密密码
        passwordHelper.encryptPassword(user);
        userDao.create(user);
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }

    @Override
    public void update(User user) {
        //加密密码
        passwordHelper.encryptPassword(user);
        userDao.update(user);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }


    @Override
    public User findByName(String username) {
        return userDao.findByName(username);
    }

    @Override
    public void deleteAllUserRoles(Long id) {
        userDao.deleteAllUserRoles(id);
    }

}
