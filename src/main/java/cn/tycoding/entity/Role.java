package cn.tycoding.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
public class Role implements Serializable {
    private Long id; //角色编号
    private String role; //角色标识 程序中判断使用，如"admin"
    private String description; //角色描述，UI界面显示使用
    private Long pid; //父节点id值
    private Boolean available = Boolean.FALSE; //是否可用，如果不可用将不会添加给用户

    private List<Permission> res; //获取包装角色-权限信息的数据

    public Role() {
    }

    public Role(String role, String description, Boolean available) {
        this.role = role;
        this.description = description;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<Permission> getRes() {
        return res;
    }

    public void setRes(List<Permission> res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }
}
