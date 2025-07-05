import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

/* Layout */
import Layout from "@/layout";

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: "/redirect",
    component: Layout,
    hidden: true,
    children: [
      {
        path: "/redirect/:path(.*)",
        component: () => import("@/views/redirect"),
      },
    ],
  },
  {
    path: "/login",
    component: () => import("@/views/login"),
    hidden: true,
  },
  // 第三方登录页
  {
    path: "/thirdpartylogin",
    component: () => import("@/views/thirdpartylogin"),
    hidden: true,
  },
  // 第三方登录页中转
  {
    path: "/logindemo",
    component: () => import("@/views/logindemo"),
    hidden: true,
  },
  {
    path: "/register",
    component: () => import("@/views/register"),
    hidden: true,
  },
  {
    path: "/404",
    component: () => import("@/views/error/404"),
    hidden: true,
  },
  {
    path: "/401",
    component: () => import("@/views/error/401"),
    hidden: true,
  },
  {
    path: "/",
    component: Layout,
    redirect: "index",
    children: [
      {
        path: "index",
        component: () => import("@/views/index"),
        name: "Index",
        meta: { title: "首页", icon: "dashboard", affix: true },
      },
    ],
  },
  {
    path: "/user",
    component: Layout,
    hidden: true,
    redirect: "noredirect",
    children: [
      {
        path: "profile",
        component: () => import("@/views/system/user/profile/index"),
        name: "Profile",
        meta: { title: "个人中心", icon: "user" },
      },
    ],
  },
];

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  {
    // 原材料下单
    path: "/materialOrder",
    component: Layout,
    hidden: true,
    permissions: ["business:order"],
    children: [
      {
        // 原材料下单-进行下单
        path: "customsInspectionOrder",
        component: () =>
          import("@/views/business/materialOrder/customsInspectionOrder"),
        name: "CustomsInspectionOrder",
        meta: {
          title: "进行原材料下单",
          activeMenu: "/business/materialOrder",
          keepAlive: true
        },
      },
      {
        // 原材料下单-查看详情
        path: "customsInspectionView",
        component: () =>
          import("@/views/business/materialOrder/customsInspectionView"),
        name: "CustomsInspectionView",
        meta: {
          title: "查看原材料下单详情",
          activeMenu: "/business/materialOrder",
          keepAlive: true
        },
      },
      {
        // 铜材料下单
        path: "copperOrder",
        component: () => import("@/views/business/materialOrder/copperOrder"),
        name: "CopperOrder",
        meta: { title: "进行铜材料下单", activeMenu: "/business/materialOrder",keepAlive: true },
      },
      {
        // 铜材料下单-查看详情
        path: "copperView",
        component: () => import("@/views/business/materialOrder/copperView"),
        name: "CopperView",
        meta: { title: "查看铜材料下单详情", activeMenu: "/business/materialOrder",keepAlive: true },
      },
    ],
  },
  {
    // 成品下单
    path: "/productOrder",
    component: Layout,
    hidden: true,
    permissions: ["business:productOrder"],
    children: [
      {
        // 查看成品下单详情
        path: "addView",
        component: () =>
          import("@/views/business/productOrder/components/addView.vue"),
        name: "AddView",
        meta: { title: "查看成品下单详情", activeMenu: "/business/productOrder",keepAlive: true },
      },
      {
        // 进行下单-成品下单
        path: "addOrder",
        component: () =>
          import("@/views/business/productOrder/components/addOrder.vue"),
        name: "AddOrder",
        meta: { title: "进行成品下单", activeMenu: "/business/productOrder",keepAlive: true },
      },
    ],
  },
  {
    // 检验任务
    path: "/inspectionTask",
    component: Layout,
    hidden: true,
    permissions: ["business:inspection"],
    children: [
      {
        // 检验单详情
        path: "inspection",
        component: () => import("@/views/business/inspectionTask/inspection"),
        name: "inspection",
        meta: { title: "检验单详情", activeMenu: "/business/inspectionTask", keepAlive: false },
      },
    ],
  },
  {
    // 用户管理
    path: "/system/user-auth",
    component: Layout,
    hidden: true,
    permissions: ["system:user:edit"],
    children: [
      {
        path: "role/:userId(\\d+)",
        component: () => import("@/views/system/user/authRole"),
        name: "AuthRole",
        meta: { title: "分配角色", activeMenu: "/system/user" },
      },
    ],
  },
  {
    // 角色管理
    path: "/system/role-auth",
    component: Layout,
    hidden: true,
    permissions: ["system:role:edit"],
    children: [
      {
        path: "user/:roleId(\\d+)",
        component: () => import("@/views/system/role/authUser"),
        name: "AuthUser",
        meta: { title: "分配用户", activeMenu: "/system/role" },
      },
    ],
  },
  {
    // 数据字典
    path: "/system/dict-data",
    component: Layout,
    hidden: true,
    permissions: ["system:dict:list"],
    children: [
      {
        path: "index/:dictId(\\d+)",
        component: () => import("@/views/system/dict/data"),
        name: "Data",
        meta: { title: "字典数据", activeMenu: "/system/dict" },
      },
    ],
  },
  {
    // 数据字典
    path: "/customer",
    component: Layout,
    hidden: true,
    permissions: ["customer:edit"],
  },
];

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push;
let routerReplace = Router.prototype.replace;
// push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch((err) => err);
};
// replace
Router.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch((err) => err);
};

export default new Router({
  mode: "history", // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes,
});
