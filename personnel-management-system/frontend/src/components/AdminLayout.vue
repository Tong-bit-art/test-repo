<template>
  <div class="admin-layout">
    <el-container style="height: 100vh;">
      <el-aside width="200px" class="admin-sidebar">
        <div class="logo">
          <h3>人员库管理系统</h3>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical-demo"
          @select="handleMenuSelect"
        >
          <el-menu-item v-if="isAdmin" index="/admin/personnel">
            <el-icon><UserFilled /></el-icon>
            <span>人员管理</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin || isEntry" index="/entry/personnel">
            <el-icon><Plus /></el-icon>
            <span>人员录入</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin" index="/admin/selection">
            <el-icon><Refresh /></el-icon>
            <span>随机抽取</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin" index="/admin/categories">
            <el-icon><Collection /></el-icon>
            <span>专业类目</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin" index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item v-if="isAdmin" index="/admin/logs">
            <el-icon><Document /></el-icon>
            <span>操作日志</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="admin-header">
          <div class="header-left">
            <el-button type="text" @click="toggleSidebar">
              <el-icon><Menu /></el-icon>
            </el-button>
          </div>
          <div class="header-right">
            <span>{{ user?.name }}</span>
            <el-tag v-if="isAdmin" type="danger" size="small">管理员</el-tag>
            <el-tag v-else-if="isEntry" type="success" size="small">录入员</el-tag>
            <el-button type="text" @click="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-button>
          </div>
        </el-header>
        <el-main class="admin-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { UserFilled, Refresh, Collection, User, Menu, SwitchButton, Plus, Document } from '@element-plus/icons-vue'
import { isAdmin, isEntry } from '../utils/permission'

const router = useRouter()
const activeMenu = ref('/admin/personnel')
const user = ref(null)

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
  }
  activeMenu.value = router.currentRoute.value.path
})

const handleMenuSelect = (key) => {
  router.push(key)
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

const toggleSidebar = () => {
  // 这里可以实现侧边栏的展开/收起功能
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.admin-sidebar {
  background-color: #303133;
  color: #fff;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #404040;
}

.el-menu-vertical-demo {
  border-right: none;
  height: calc(100vh - 60px);
}

.el-menu {
  background-color: #303133;
  border-right: none;
}

.el-menu-item {
  color: #e0e0e0;
  height: 50px;
  line-height: 50px;
}

.el-menu-item:hover {
  background-color: #404040;
}

.el-menu-item.is-active {
  background-color: #409eff;
  color: #fff;
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.admin-main {
  padding: 20px;
  background-color: #f5f7fa;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .admin-sidebar {
    width: 60px !important;
  }
  
  .logo h3 {
    font-size: 14px;
  }
  
  .el-menu-item span {
    display: none;
  }
}
</style>