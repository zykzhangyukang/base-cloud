package com.coderman.auth.constant;


import com.coderman.api.anntation.Constant;
import com.coderman.api.anntation.ConstList;

/**
 * @author coderman
 * @Title: 系统模块常量
 * @Description: TOD
 * @date 2022/2/2623:50
 */
@Constant
public interface AuthConstant {

    /**
     * 登录会话过期时间 - 12个小时
     */
    Integer AUTH_EXPIRED_SECOND = 60 * 60 * 12;  // 60 * 60 * 2

    /**
     * 用户会话redis key 前缀
     */
    String AUTH_TOKEN_NAME = "auth:token:";

    /**
     * 用户状态
     */
    String USER_STATUS_GROUP = "user_status_group";

    @ConstList(group = USER_STATUS_GROUP, name = "锁定")
    Integer USER_STATUS_DISABLE = 0;

    @ConstList(group = USER_STATUS_GROUP, name = "启用")
    Integer USER_STATUS_ENABLE = 1;

    /**
     * 项目常量
     */
    String project_domain = "project_domain";

    @ConstList(group = project_domain, name = "权限系统")
    String PROJECT_DOMAIN_AUTH = "auth";

    @ConstList(group = project_domain, name = "同步系统")
    String PROJECT_DOMAIN_SYNC = "sync";


    /**
     * 方法常量
     */
    String METHOD_TYPE = "method_type";

    @ConstList(group = METHOD_TYPE, name = "GET")
    String METHOD_TYPE_GET = "get";

    @ConstList(group = METHOD_TYPE, name = "POST")
    String METHOD_TYPE_POST = "post";

    @ConstList(group = METHOD_TYPE, name = "DELETE")
    String METHOD_TYPE_DELETE = "delete";

    @ConstList(group = METHOD_TYPE, name = "PUT")
    String METHOD_TYPE_PUT = "put";


    /**
     * 项目最顶级的功能父级id
     */
    Integer FUNC_ROOT_PARENT_ID = 0;

    /**
     * 功能类型
     */
    String FUNC_TYPE_GROUP = "func_type_group";

    @ConstList(group = FUNC_TYPE_GROUP, name = "菜单路由")
    String FUNC_TYPE_DIR = "dir";

    @ConstList(group = FUNC_TYPE_GROUP, name = "功能按钮")
    String FUNC_TYPE_FUNC = "func";

    /**
     * 是否隐藏菜单
     */
    String FUNC_DIR_STATUS_GROUP = "func_dir_status_group";

    @ConstList(group = FUNC_DIR_STATUS_GROUP, name = "隐藏")
    String FUNC_DIR_STATUS_HIDE = "hide";

    @ConstList(group = FUNC_DIR_STATUS_GROUP, name = "显示")
    String FUNC_DIR_STATUS_SHOW =  "show";
}
