/**
 * 抽离-- 根据username查询用户信息的方法
 * @param username
 */
function findByUsername(username) {
    var obj = {};
    $.ajax({
        url: '../../user/findByUsername.do?username=' + username,
        type: 'get',
        async: false,
        success: (result) => {
            obj = result;
            console.log(result);
        },
        error: () => {
            alert('error!');
        }
    });
    return obj;
}

/**
 * 抽离-- 根据username查询用户角色的方法
 * @param username
 */
function findRoles(username) {
    var obj = [];
    $.ajax({
        url: '../../user/findRoles.do?username=' + username,
        type: 'get',
        async: false,
        success: (result) => {
            obj = result;
        },
        error: () => {
            alert('error!');
        }
    });
    console.log(obj);
    return obj;
}

//操作-权限按钮
//误区： 用户层面上只维护用户-角色表。不存在同一个角色拥有不同权限的情况，一旦两个用户拥有了不同的权限，那一定是拥有这不同的角色
function RolesBtn(id, username) {
    $('.title .info').text(''); //先清空
    /**
     * Tree树控件
     */
    var setting = {
        view: {
            selectedMulti: false
        },
        check: {
            enable: true,
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
            onCheck: zTreeOnCheck, //点击复选框（选中或未选中），触发的回调函数
        }
    };

    //点击复选框前的回调函数
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
            url: '../../user/getZTreeForUserRoles.do',
            type: 'get',
            dataType: "json",
            success: (data) => {
                console.log(data);
                $.fn.zTree.init($("#tree"), setting, data);//初始化树节点时，添加同步获取的数据
                checkNodes();
            },
            error: (data) => {
                alert(data.message);
            }
        });
    });

    //处理哪些节点应该被选中
    function checkNodes() {
        var zTree = $.fn.zTree.getZTreeObj("tree");//获取zTree对象

        var thisRoles = findRoles(username); //根据当前用户名获取到其角色信息

        var roleNode = []; //初始化角色（父节点）被选中的列表
        thisRoles.forEach(row => {
            roleNode.push(row.id); //push被选中的角色（父节点）
        });
        roleNode.forEach(node_role => {
            zTree.selectNode(zTree.getNodeByParam("id", node_role), false, true); //调用selectNode()方法，能够帮我们自动展开被选中子节点所在的父节点
            zTree.checkNode(zTree.getNodeByParam("id", node_role), true, true); //设置第三个参数是false表示选中父节点不主动联动勾选其下的子节点
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

//权限-提交按钮
function surePermission() {
    var zTree = $.fn.zTree.getZTreeObj("tree");//获取zTree对象
    let checkedNodes = zTree.getCheckedNodes();
    var ids = new Array(); //初始化当前用户角色id的集合，后台根据parent的值来判断当前id是否是父级（角色）
    var pids = new Array(); //初始化当前用户角色的pid集合
    var parents = new Array(); //初始化当前用户角色parent值的集合
    var names = new Array(); //初始化当前用户角色名称集合
    checkedNodes.forEach(row => {
        ids.push(row.id);
        pids.push(row.pid);
        parents.push(row.parent);
        names.push(row.name);
    });
    console.log($("#permission-modal .modal-body .id").val());
    console.log(ids);
    console.log(pids);
    console.log(parents);
    console.log(names);

    //提交
    $.ajax({
        url: '../../user/updateUserRoles.do',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({
            id: $("#permission-modal .modal-body .id").val(),
            ids: ids,
            pids: pids,
            parents: parents,
            names: names
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
function statusBtn(id, username) {
    $("#status-modal").modal('show');
    var obj = findByUsername(username);
    if (obj.locked) {
        $('.unuse').prop("checked", true);
    } else {
        $('.use').prop("checked", true);
    }
    $("#status-modal .id").text(obj.id);
    $("#status-modal .username").text(obj.username);
    $("#status-modal .password").text(obj.password);
    $("#status-modal .locked").text(obj.locked);
}

//状态-提交按钮
$(".status-sure").click(function () {
    $("#status-modal").modal('hide');
    var locked = 0; //0表示启用，1表示锁定
    if ($('.unuse').is(':checked')) {
        locked = 1;
    }
    // console.log('id:' + $("#status-modal .id").text() + ',username:' + $("#status-modal .username") + ',password:' + $("#status-modal .password") + ',locked:');
    $.ajax({
        url: '../../user/update.do',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({
            id: $("#status-modal .id").text(),
            username: $("#status-modal .username").text(),
            password: $("#status-modal .password").text(),
            locked: locked
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

//添加 - 编辑按钮共用的方法，根据id是否为空判断用户点击的是添加按钮还是编辑按钮
function editAndCreate(id, username) {
    $('#create-modal .modal-body .username').val('');
    $('#create-modal .modal-body .password').val('');
    $('#create-modal .modal-body .id').val('');
    if (id == null || id == "") {
        //点击的添加按钮
        $("#create-modal").modal('show');
    } else {
        //点击的编辑按钮
        $("#create-modal").modal('show');
        var users = findByUsername(username);
        $('#create-modal .modal-body .username').val(users.username);
        $('#create-modal .modal-body .password').val(users.password);
        $('#create-modal .modal-body .id').val(users.id);
    }
}

//添加--更新用户信息确定按钮
$(".create-sure").click(function () {
    var id = $('#create-modal .modal-body .id').val();
    var username = $('#create-modal .modal-body .username').val();
    var password = $('#create-modal .modal-body .password').val();
    console.log(username + ',' + password);
    if (id != null && id != "") {
        //更新用户信息
        $.ajax({
            url: '../../user/update.do',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify({
                id: id,
                username: username,
                password: password
            }),
            success: (data) => {
                if (data.success){
                    window.location.reload();
                }
            },
            error: (data) => {
                alert(data.message);
            }
        });
    } else {
        //创建用户
        $.ajax({
            url: '../../user/create.do',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify({
                username: username,
                password: password
            }),
            success: (data) => {
                if (data.success){
                    window.location.reload();
                }
            },
            error: (data) => {
                alert(data.message);
            }
        })
    }
});

//删除用户数据
function deleteUser(id){
    if (id != null && id != ""){
        $.ajax({
            url: '../../user/delete.do?id='+id,
            type: 'get',
            success: (data) => {
                if (data.success){
                    window.location.reload();
                }
            },
            error: (data) => {
                alert(data.message);
            }
        })
    }
}