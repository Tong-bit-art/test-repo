<template>
  <div class="entry-layout">
    <el-container style="height: 100vh;">
      <el-header class="entry-header">
        <div class="header-left">
          <h3>人员库管理系统</h3>
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
      <el-main class="entry-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { SwitchButton } from '@element-plus/icons-vue'
import { isAdmin, isEntry } from '../utils/permission'

const router = useRouter()
const user = ref(null)

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
  }
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style scoped>
.entry-layout {
  height: 100vh;
}

.entry-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #303133;
  color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  height: 60px;
}

.header-left h3 {
  margin: 0;
  font-size: 18px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-right span {
  color: #fff;
}

.entry-main {
  padding: 20px;
  background-color: #f5f7fa;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .entry-header {
    padding: 0 10px;
  }
  
  .header-left h3 {
    font-size: 16px;
  }
  
  .entry-main {
    padding: 10px;
  }
}
</style>