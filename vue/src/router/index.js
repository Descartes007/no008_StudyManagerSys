import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // 重定向到主页
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'user', name: 'User', meta: { name: '用户信息' }, component: () => import('../views/manager/User') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
      { path: 'course', name: 'Course', meta: { name: '课程信息' }, component: () => import('../views/manager/Course') },
      { path: 'score', name: 'Score', meta: { name: '积分专区' }, component: () => import('../views/manager/Score') },
      { path: 'information', name: 'Information', meta: { name: '资料审核' }, component: () => import('../views/manager/Information') },
      { path: 'orders', name: 'Orders', meta: { name: '课程订单' }, component: () => import('../views/manager/Orders') },
      { path: 'scoreOrder', name: 'ScoreOrder', meta: { name: '积分兑课' }, component: () => import('../views/manager/ScoreOrder') },
      { path: 'fileOrder', name: 'FileOrder', meta: { name: '资料下载' }, component: () => import('../views/manager/FileOrder') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'course', name: 'Course', meta: { name: '全部课程' }, component: () => import('../views/front/Course') },
      { path: 'courseDetail', name: 'CourseDetail', meta: { name: '课程详情' }, component: () => import('../views/front/CourseDetail') },
      { path: 'scoreDetail', name: 'ScoreDetail', meta: { name: '积分详情' }, component: () => import('../views/front/ScoreDetail') },
      { path: 'informationDetail', name: 'InformationDetail', meta: { name: '资料详情' }, component: () => import('../views/front/InformationDetail') },
      { path: 'score', name: 'Score', meta: { name: '积分专区' }, component: () => import('../views/front/Score') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
      { path: 'myInfo', name: 'MyInfo', meta: { name: '我的资料' }, component: () => import('../views/front/MyInfo') },
      { path: 'information', name: 'Information', meta: { name: '海量资源' }, component: () => import('../views/front/Information') },
      { path: 'orders', name: 'Orders', meta: { name: '已购课程' }, component: () => import('../views/front/Orders') },
      { path: 'scoreOrder', name: 'ScoreOrder', meta: { name: '我的兑换' }, component: () => import('../views/front/ScoreOrder') },
      { path: 'fileOrder', name: 'FileOrder', meta: { name: '历史下载' }, component: () => import('../views/front/FileOrder') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
