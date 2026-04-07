import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import AdminLayout from '../components/AdminLayout.vue'
import EntryLayout from '../components/EntryLayout.vue'
import PersonnelList from '../views/Admin/PersonnelList.vue'
import PersonnelEntry from '../views/Entry/PersonnelEntry.vue'
import CategoryManagement from '../views/Admin/CategoryManagement.vue'
import UserManagement from '../views/Admin/UserManagement.vue'
import RandomSelection from '../views/Admin/RandomSelection.vue'
import OperationLog from '../views/Admin/OperationLog.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresRole: 'ADMIN' },
    children: [
      {
        path: 'personnel',
        component: PersonnelList,
        meta: { title: '人员信息管理' }
      },
      {
        path: 'categories',
        component: CategoryManagement,
        meta: { title: '专业类目管理' }
      },
      {
        path: 'users',
        component: UserManagement,
        meta: { title: '用户管理' }
      },
      {
        path: 'selection',
        component: RandomSelection,
        meta: { title: '随机抽取' }
      },
      {
        path: 'logs',
        component: OperationLog,
        meta: { title: '操作日志' }
      }
    ]
  },
  {
    path: '/entry',
    component: EntryLayout,
    meta: { requiresAuth: true, requiresRole: ['ADMIN', 'ENTRY'] },
    children: [
      {
        path: 'personnel',
        component: PersonnelEntry,
        meta: { title: '信息录入' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStr = localStorage.getItem('user')
  const user = userStr ? JSON.parse(userStr) : null
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth) {
    if (!token || !user) {
      next('/login')
      return
    }

    const requiredRole = to.meta.requiresRole
    if (requiredRole) {
      let hasRole = false
      if (Array.isArray(requiredRole)) {
        hasRole = user.roles && user.roles.some(role => requiredRole.includes(role.name))
      } else {
        hasRole = user.roles && user.roles.some(role => role.name === requiredRole)
      }
      if (!hasRole) {
        if (user.roles && user.roles.some(role => role.name === 'ADMIN')) {
          next('/admin/personnel')
        } else if (user.roles && user.roles.some(role => role.name === 'ENTRY')) {
          next('/entry/personnel')
        } else {
          next('/login')
        }
        return
      }
    }
  }

  if (to.path === '/login' && token && user) {
    if (user.roles && user.roles.some(role => role.name === 'ADMIN')) {
      next('/admin/personnel')
    } else if (user.roles && user.roles.some(role => role.name === 'ENTRY')) {
      next('/entry/personnel')
    } else {
      next()
    }
    return
  }

  next()
})

export default router