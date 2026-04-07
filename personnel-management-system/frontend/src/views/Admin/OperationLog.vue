<template>
  <div class="operation-log">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>操作日志</h3>
          <div class="filter-area">
            <el-select v-model="filterType" placeholder="操作类型" clearable style="width: 150px; margin-right: 10px;">
              <el-option label="登录" value="LOGIN" />
              <el-option label="登出" value="LOGOUT" />
              <el-option label="查询" value="QUERY" />
              <el-option label="创建" value="CREATE" />
              <el-option label="更新" value="UPDATE" />
              <el-option label="删除" value="DELETE" />
              <el-option label="导入" value="IMPORT" />
              <el-option label="导出" value="EXPORT" />
              <el-option label="抽取" value="SELECTION" />
            </el-select>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 300px; margin-right: 10px;"
            />
            <el-button type="primary" @click="loadLogs">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="logs" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="操作用户" width="120" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOperationTypeTag(row.operationType)">
              {{ getOperationTypeLabel(row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationDescription" label="操作描述" min-width="200" />
        <el-table-column prop="ipAddress" label="IP地址" width="140" />
        <el-table-column prop="createdAt" label="操作时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import api from '../../api'

const logs = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const filterType = ref('')
const dateRange = ref(null)

onMounted(() => {
  loadLogs()
})

const loadLogs = async () => {
  loading.value = true
  try {
    const response = await api.get('/admin/operation-logs')
    let data = response || []
    
    if (filterType.value) {
      data = data.filter(log => log.operationType === filterType.value)
    }
    
    if (dateRange.value && dateRange.value.length === 2) {
      const startDate = new Date(dateRange.value[0])
      const endDate = new Date(dateRange.value[1])
      endDate.setHours(23, 59, 59, 999)
      data = data.filter(log => {
        const logDate = new Date(log.createdAt)
        return logDate >= startDate && logDate <= endDate
      })
    }
    
    total.value = data.length
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    logs.value = data.slice(start, end)
  } catch (error) {
    ElMessage.error('加载操作日志失败')
  } finally {
    loading.value = false
  }
}

const handleSizeChange = () => {
  loadLogs()
}

const handleCurrentChange = () => {
  loadLogs()
}

const getOperationTypeTag = (type) => {
  const typeMap = {
    'LOGIN': 'success',
    'LOGOUT': 'info',
    'QUERY': '',
    'CREATE': 'primary',
    'UPDATE': 'warning',
    'DELETE': 'danger',
    'IMPORT': 'primary',
    'EXPORT': 'success',
    'SELECTION': 'warning'
  }
  return typeMap[type] || ''
}

const getOperationTypeLabel = (type) => {
  const labelMap = {
    'LOGIN': '登录',
    'LOGOUT': '登出',
    'QUERY': '查询',
    'CREATE': '创建',
    'UPDATE': '更新',
    'DELETE': '删除',
    'IMPORT': '导入',
    'EXPORT': '导出',
    'SELECTION': '抽取'
  }
  return labelMap[type] || type
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}
</script>

<style scoped>
.operation-log {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
}

.filter-area {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 15px;
  }
  
  .filter-area {
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .filter-area .el-select,
  .filter-area .el-date-picker {
    width: 100% !important;
    margin-right: 0 !important;
  }
}
</style>
