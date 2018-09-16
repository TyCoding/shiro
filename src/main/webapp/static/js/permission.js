/**
 * 抽离--根据当前角色id查询其数据
 */
function findById(id) {
    var obj = {};
    $.ajax({
        url: '../../permissions/findById.do?id=' + id,
        type: 'get',
        async: false,
        success: (data) => {
            obj = data;
            console.log(data);
        },
        error: () => {
            alert('error!');
        }
    });
    return obj;
}

/**
 * 抽离--根据权限id查询其所关联的角色数据
 */
function findPermissionByRoleId(id) {
    var obj = {};
    $.ajax({
        url: '../../permissions/findRoleByPermissionId.do?id=' + id,
        type: 'get',
        async: false,
        success: (data) => {
            obj = data;
        },
        error: () => {
            alert('error!');
        }
    });
    return obj;
}

//操作-关联角色按钮
//此处的关联角色按钮功能：1、用来展示当前权限所关联的角色信息，以ZTree树的方式展示，由后端构建ZTree树的JSON数据结构，目的是实现对当前权限的管理。
//                     2、为了维护角色-权限表的关系。之前在用户层面上需要维护用户-角色表关系，在角色层面上需要维护角色-权限关系。现在在权限层面上仍是维护角色-权限表。
//误区： 始终要记得：同一个角色拥有不同权限的情况是不存在的，如果两个用户间拥有了不一样的权限，那么他们就一定是拥有着不同的角色。
function permissionBtn(id) {
    $('.title .info').text(''); //先清空
    /**
     * Tree树控件
     */
    var setting = {
        view: {
            selectedMulti: false
        },
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true,//是否采用简单数据模式
                idKey: "id",//树节点ID名称
                pIdKey: "pid",//父节点ID名称
                rootPId: -1,//根节点ID
            }
        },
        callback: {
            onCheck: zTreeOnCheck //点击复选框（选中或未选中），触发的回调函数
        }
    };

    function zTreeOnCheck(event, treeId, treeNode) {
        $('.title .info').text(''); //先清空textarea
        var zTree = $.fn.zTree.getZTreeObj("tree");//获取zTree对象
        let checkedNodes = zTree.getCheckedNodes();
        console.log(checkedNodes);
        if (treeNode.checked) {
            //展示被选中的数据
            checkedNodes.forEach(row => {
                $('.title .info').append('id:' + row.id + ',name:' + row.name + ',pid:' + row.pid + ',parent:' + row.parent + ' ----');
            });
        } else {
            //展示被选中的数据
            checkedNodes.forEach(row => {
                $('.title .info').append('id:' + row.id + ',name:' + row.name + ',pid:' + row.pid + ',parent:' + row.parent + ' ----');
            });
        }
    }

    $(function () {
        //加载后端构建的ZTree树（节点的数据格式已在后端格式化好了）
        $.ajax({
            url: '../../permissions/getRolesZTree.do',
            type: 'get',
            dataType: "json",
            success: (data) => {
                console.log(data);
                $.fn.zTree.init($("#tree"), setting, data);//初始化树节点时，添加同步获取的数据
                checkNodes();
            },
            error: () => {
                alert('error!');
            }
        });
    });

    //处理哪些节点应该被选中
    function checkNodes() {
        var zTree = $.fn.zTree.getZTreeObj("tree");//获取zTree对象

        var thisPermissions = findPermissionByRoleId(id); //根据当前角色id得到其所关联的权限数据

        var permissionNode = []; //初始化权限（子节点）被选中的列表(父节点必选)
        thisPermissions.forEach(row => {
            permissionNode.push(row.id); //push被选中的权限（子节点）
        });
        permissionNode.forEach(node_role => {
            zTree.selectNode(zTree.getNodeByParam("id", node_role), true, false); //调用selectNode方法，可实现展开选中子节点所在的父节点
            zTree.checkNode(zTree.getNodeByParam("id", node_role), true, false); //设置第三个参数是false表示选中父节点不主动联动勾选其下的子节点
        });

        let checkedNodes = zTree.getCheckedNodes();
        console.log(checkedNodes);
        //展示默认选中的数据
        checkedNodes.forEach(row => {
            $('.title .info').append('id:' + row.id + ',name:' + row.name + ',pid:' + row.pid + ',parent:' + row.parent + ' ----');
        });
    }

    $("#permission-modal .modal-body .id").val(id); //将当前操作用户的id作为模态框中的一个属性值
    //打开模态框
    $("#permission-modal").modal('show');
}

//权限-确定按钮
function surePermission() {
    var zTree = $.fn.zTree.getZTreeObj("tree");//获取zTree对象
    let checkedNodes = zTree.getCheckedNodes();
    var id = $("#permission-modal .modal-body .id").val();
    var ids = new Array(); //初始化当前用户角色id的集合，后台根据parent的值来判断当前id是否是父级（角色）
    var parents = new Array(); //初始化当前用户角色parent值的集合
    checkedNodes.forEach(row => {
        ids.push(row.id);
        parents.push(row.parent);
    });
    console.log(id);
    console.log(ids);
    console.log(parents);

    //提交
    $.ajax({
        url: '../../permissions/updateRolesPermissions.do',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({
            id: id,
            ids: ids,
            parents: parents
        }),
        success: (data) => {
            if (data.success) {
                window.location.reload();
            }
        },
        error: (data) => {
            alert(data.message);
        }
    })

}

//操作-状态按钮
function statusBtn(id) {
    $("#status-modal").modal('show');
    var obj = findById(id);
    if (obj.available) {
        $('.unuse').prop("checked", true);
    } else {
        $('.use').prop("checked", true);
    }
    $("#status-modal .id").text(obj.id);
}

//状态-提交按钮
$(".status-sure").click(function () {
    $("#status-modal").modal('hide');
    var available = 0; //0表示启用，1表示锁定
    if ($('.unuse').is(':checked')) {
        available = 1;
    }
    $.ajax({
        url: '../../permissions/update.do',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({
            id: $("#status-modal .id").text(),
            available: available
        }),
        success: (result) => {
            if (result.success) {
                window.location.reload();
            }
            console.log(result);
        },
        error: (data) => {
            alert(data.message);
        }
    });
});

//删除角色数据
function deleteRole(id) {
    if (id != null && id != "") {
        $.ajax({
            url: '../../permissions/delete.do?id=' + id,
            type: 'get',
            success: (data) => {
                if (data.success) {
                    window.location.reload();
                }
            },
            error: (data) => {
                alert(data.message);
            }
        })
    }
}

//添加
function create(id) {
    $('#create-modal .modal-body .role').val('');
    $('#create-modal .modal-body .description').val('');
    $("#create-modal").modal('show');

    /**
     * Tree树控件
     */
    var setting = {
        view: {
            selectedMulti: false
        },
        check: {
            enable: true,
            chkStyle: "radio"
        },
        data: {
            simpleData: {
                enable: true,//是否采用简单数据模式
                idKey: "id",//树节点ID名称
                pIdKey: "pid",//父节点ID名称
                rootPId: -1,//根节点ID
            }
        }
    };

    $(function () {
        //加载后端构建的ZTree树（节点的数据格式已在后端格式化好了）
        $.ajax({
            url: '../../permissions/getRolesZTree.do',
            type: 'get',
            dataType: "json",
            success: (data) => {
                console.log(data);
                $.fn.zTree.init($("#tree-create"), setting, data);//初始化树节点时，添加同步获取的数据
            },
            error: () => {
                alert('error!');
            }
        });
    });
}

//添加--确定按钮
$(".create-sure").click(function () {
    var permission = $('#create-modal .modal-body .permission').val();
    var description = $('#create-modal .modal-body .description').val();

    var zTree = $.fn.zTree.getZTreeObj("tree-create");//获取zTree对象
    let checkedNodes = zTree.getCheckedNodes();
    var rid = 0; //如果没有勾选角色，默认是0
    if (checkedNodes.length > 0){
        //checkedNodes是被勾选数据的集合数组，且是被如果有父节点被选中，那么数组中这个父节点一定在其被选子节点的后面，取数组最后位置得索引即可得到最底层节点的数据
        rid = checkedNodes[checkedNodes.length - 1].id
    }
    console.log(permission);
    console.log(description);
    console.log(rid);
    //创建用户
    $.ajax({
        url: '../../permissions/create.do',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({
            permission: permission,
            description: description,
            rid: rid
        }),
        success: (data) => {
            if (data.success) {
                window.location.reload();
            }
        },
        error: (data) => {
            alert(data.message);
        }
    })


});

//操作-编辑
function edit(id) {
    $('#edit-modal .modal-body .id').val('');
    if (id != null || id != '') {
        $("#edit-modal").modal('show');
        var permissions = findById(id);
        $('#edit-modal .modal-body .permission').val(permissions.permission);
        $('#edit-modal .modal-body .description').val(permissions.description);
        $('#edit-modal .modal-body .id').val(permissions.id);
    }
}

//编辑-确定按钮
$(".edit-sure").click(() => {
    var id = $('#edit-modal .modal-body .id').val();
    var permission = $('#edit-modal .modal-body .permission').val();
    var description = $('#edit-modal .modal-body .description').val();
    if (id != null && id != "") {
        //更新用户信息
        $.ajax({
            url: '../../permissions/update.do',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify({
                id: id,
                permission: permission,
                description: description
            }),
            success: (data) => {
                if (data.success) {
                    window.location.reload();
                }
            },
            error: (data) => {
                alert(data.message);
            }
        });
    }
});