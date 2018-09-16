import cn.tycoding.entity.Permission;
import cn.tycoding.service.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 权限相关的测试类
 * @auther TyCoding
 * @date 2018/7/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-*.xml"} )
public class PermissionTest {

    @Autowired
    private PermissionService permissionService;

    //创建权限
    @Test
    public void createPermissionTest(){
        Permission permission = new Permission("user:create","用户模块新增",Boolean.TRUE);
        Permission permission2 = new Permission("user:update","用户模块修改",Boolean.TRUE);
        permissionService.create(permission);
        permissionService.create(permission2);
    }

    //删除权限
    @Test
    public void deletePermissionTest(){
        permissionService.delete(1L);
    }
}
