import cn.tycoding.dao.UserDao;
import cn.tycoding.entity.Permission;
import cn.tycoding.entity.Role;
import cn.tycoding.entity.User;
import cn.tycoding.service.RoleService;
import cn.tycoding.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * User相关的测试类
 *
 * @auther TyCoding
 * @date 2018/7/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})
@TransactionConfiguration(defaultRollback = false)
public class UserTest {

    @Autowired
    protected UserService userService;
    @Autowired
    protected UserDao userDao;

    @Autowired
    private RoleService roleService;

    //测试创建用户
    @Test
    public void createUserTest() {
        User user = new User("admin", "123");
        User user2 = new User("tycoding", "123");
        User user3 = new User("涂陌", "123");
        userService.create(user);
        userService.create(user2);
        userService.create(user3);
    }

    //测试根据用户ID查询
    @Test
    public void findOneTest() {
        User user = userDao.findById(1l);
        System.out.println(
                "id:" + user.getId()
                        + ",username:" + user.getUsername()
                        + ",password:" + user.getPassword()
                        + ",salt:" + user.getSalt()
                        + ",locked:" + user.getLocked());
    }

    //测试更新用户信息
    @Test
    public void updateUserTest() {
        User user = new User();
        user.setId(3l);
        user.setUsername("涂陌");
        user.setPassword("123");
        user.setLocked(false);
        userService.update(user);
    }

    //测试修改密码
    @Test
    public void changePasswordTest() {
        userService.changePassword(3l, "123");
    }

    //测试创建用户-角色关系
    @Test
    public void correlationRolesTest() {
        userService.correlationRoles(1L, 1L);
    }

    //移除用户-角色关系
    @Test
    public void uncorrelationRolesTest() {
        userService.uncorrelationRoles(1L, 1L);
    }

    //根据用户名查询
    @Test
    public void findByUsernameTest() {
        User user = userService.findByName("涂陌");
        System.out.println(
                "id:" + user.getId()
                        + ",username:" + user.getUsername()
                        + ",password:" + user.getPassword()
                        + ",salt:" + user.getSalt()
                        + ",locked:" + user.getLocked()
        );
    }

    //根据用户名查找其角色
    @Test
    public void findRolesTest() {
        List<Role> setUser = userService.findRoles("tycoding");
        System.out.println(setUser);
    }

    //根据用户名查找其权限
    @Test
    public void findPermissionsTest() {
        List<Permission> setUser = userService.findPermissions("tycoding");
        System.out.println(setUser);
    }

    //删除用户信息
    @Test
    public void deleteTest() {
        userService.delete(3L);
    }


    //查询用户信息
    @Test
    public void findUser() {
        List<Role> roleList = new ArrayList<Role>();
        List<User> userList = userService.findAll();
        System.out.println(roleList);

    }

}
