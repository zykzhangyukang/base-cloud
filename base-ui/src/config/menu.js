const menu = [
    {
        name: 'Index',
        title: '后台首页',
        icon: 'HomeOutlined',
    },
    {
        title: '同步系统',
        icon: 'DiffOutlined',
        name: 'Sync',
        subs: [
            {
                title: '同步计划',
                name: 'SyncPlan',
                icon: 'PieChartOutlined',
            },
            {
                title: '同步记录',
                name: 'SyncResult',
                icon: 'WindowsOutlined',
            },
            {
                title: '同步消息',
                name: 'SyncMsg',
                icon: 'TwitterOutlined',
            },
            {
                title: '回调任务',
                name: 'SyncCallback',
                icon: 'SlackOutlined',
            }
        ]
    },
    {
        title: '权限系统',
        icon: 'SnippetsOutlined',
        name: 'Auth',
        subs: [
            {
                title: '用户管理',
                name: 'AuthUser',
                icon: 'AlertOutlined',
                subs: [
                    {
                        title: '用户新增',
                        name: 'AuthUserAdd',
                        icon: 'HomeOutlined',
                        hidden: true,
                    }
                ]
            },
            {
                title: '角色管理',
                name: 'AuthRole',
                icon: 'BugOutlined',
            },
            {
                title: '资源管理',
                name: 'AuthResc',
                icon: 'CloudServerOutlined',
            },
            {
                title: '功能管理',
                name: 'AuthFunc',
                icon: 'DatabaseOutlined',
            }
        ]
    },
]

module.exports = menu
