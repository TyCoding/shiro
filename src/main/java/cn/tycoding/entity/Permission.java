package cn.tycoding.entity;

/**
 * @auther TyCoding
 * @date 2018/7/18
 */
public class Permission {
    private Long id; //权限编号
    private String permission; //权限标识 程序中判断使用，如"user:create"
    private String description; //权限描述。UI界面显示用
    private Long rid; //此权限关联的角色的id
    private Boolean available = Boolean.FALSE; //是否可用，如果不可用将不会添加给用户

    public Permission() {
    }

    public Permission(String permission, String description, Boolean avaiable) {
        this.permission = permission;
        this.description = description;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }
}
