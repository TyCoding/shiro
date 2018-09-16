import cn.tycoding.entity.Role;
import cn.tycoding.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @auther TyCoding
 * @date 2018/7/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"})
public class RoleTest {

    @Autowired
    private RoleService roleService;

    //测试创建角色
    @Test
    public void createRoleTest(){
        Role role = new Role("admin","管理员",Boolean.TRUE);
        Role role2 = new Role("user","用户管理员",Boolean.TRUE);
        roleService.create(role);
        roleService.create(role2);
    }

    //删除角色
    @Test
    public void deleteRole(){
        roleService.delete(1L);
    }

    //创建角色-权限关系
    @Test
    public void correlationPermissions(){
        roleService.correlationPermissions(1L,1L);
    }

    //删除角色-权限关系
    @Test
    public void uncorrelationPermissionsTest(){
        roleService.uncorrelationPermissions(1L,1L);
    }
}
