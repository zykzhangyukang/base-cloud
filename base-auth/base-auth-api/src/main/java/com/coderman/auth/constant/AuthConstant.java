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

    @ConstList(group = project_domain, name = "工单系统")
    String PROJECT_DOMAIN_WOS = "wos";


    /**
     * 方法常量
     */
    String METHOD_TYPE = "method_type";

    @ConstList(group = METHOD_TYPE, name = "GET请求")
    String METHOD_TYPE_GET = "get";

    @ConstList(group = METHOD_TYPE, name = "POST请求")
    String METHOD_TYPE_POST = "post";

    @ConstList(group = METHOD_TYPE, name = "DELETE请求")
    String METHOD_TYPE_DELETE = "delete";

    @ConstList(group = METHOD_TYPE, name = "PUT请求")
    String METHOD_TYPE_PUT = "put";


    /**
     * 项目最顶级的功能父级id
     */
    Integer FUNC_ROOT_PARENT_ID = 0;

    /**
     * 功能类型
     */
    String FUNC_TYPE_GROUP = "func_type_group";

    @ConstList(group = FUNC_TYPE_GROUP, name = "目录")
    String FUNC_TYPE_DIR = "dir";

    @ConstList(group = FUNC_TYPE_GROUP, name = "功能")
    String FUNC_TYPE_FUNC = "func";

    /**
     * 是否隐藏菜单
     */
    String FUNC_HIDE_GROUP = "func_hide_group";

    @ConstList(group = FUNC_HIDE_GROUP, name = "隐藏")
    Integer FUNC_DIR_HIDE_YES = 1;

    @ConstList(group = FUNC_HIDE_GROUP, name = "显示")
    Integer FUNC_DIR_HIDE_NO =  0;
}
