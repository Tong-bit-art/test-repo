<template>
  <div class="personnel-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>人员管理</h3>
          <el-button type="primary" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出Excel
          </el-button>
        </div>
      </template>
      
      <!-- 筛选条件 -->
      <el-form :model="filterForm" label-width="80px" class="filter-form">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="姓名">
              <el-input v-model="filterForm.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别">
              <el-select v-model="filterForm.gender" placeholder="请选择性别">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="专业类目">
              <el-select v-model="filterForm.categoryId" placeholder="请选择专业类目">
                <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="部门">
              <el-input v-model="filterForm.department" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="职位">
              <el-input v-model="filterForm.position" placeholder="请输入职位" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="电话">
              <el-input v-model="filterForm.phone" placeholder="请输入电话" />
            </el-form-item>
          </el-col>
          <el-col :span="24" class="filter-actions">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 人员列表 -->
      <el-table :data="personnelList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="gender" label="性别" />
        <el-table-column prop="age" label="年龄" />
        <el-table-column label="专业类目">
          <template #default="{ row }">
            {{ row.category ? row.category.name : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="department" label="部门" />
        <el-table-column prop="position" label="职位" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="address" label="地址" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <el-pagination
        v-model:current-page="filterForm.page"
        v-model:page-size="filterForm.size"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { personnelApi, categoryApi } from '../../api'
import { ElMessage } from 'element-plus'
import { Download, Delete } from '@element-plus/icons-vue'

const personnelList = ref([])
const categories = ref([])
const total = ref(0)
const filterForm = ref({
  name: '',
  gender: '',
  categoryId: '',
  department: '',
  position: '',
  phone: '',
  email: '',
  page: 1,
  size: 10
})

onMounted(() => {
  loadCategories()
  loadPersonnel()
})

const loadCategories = async () => {
  try {
    const response = await categoryApi.list()
    categories.value = response
  } catch (error) {
    ElMessage.error('加载专业类目失败')
  }
}

const loadPersonnel = async () => {
  try {
    const response = await personnelApi.list(filterForm.value)
    personnelList.value = response.content
    total.value = response.totalElements
  } catch (error) {
    ElMessage.error('加载人员信息失败')
  }
}

const handleSearch = () => {
  filterForm.value.page = 1
  loadPersonnel()
}

const resetFilter = () => {
  filterForm.value = {
    name: '',
    gender: '',
    categoryId: '',
    department: '',
    position: '',
    phone: '',
    email: '',
    page: 1,
    size: 10
  }
  loadPersonnel()
}

const handleSizeChange = (size) => {
  filterForm.value.size = size
  loadPersonnel()
}

const handleCurrentChange = (current) => {
  filterForm.value.page = current
  loadPersonnel()
}

const handleDelete = async (id) => {
  try {
    await personnelApi.delete(id)
    ElMessage.success('删除成功')
    loadPersonnel()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const handleExport = async () => {
  try {
    const response = await personnelApi.exportExcel(filterForm.value)
    const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '人员信息.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}
</script>

<style scoped>
.personnel-list {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-form {
  margin-bottom: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.filter-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

@media (max-width: 768px) {
  .filter-form {
    padding: 10px;
  }
  
  .el-col {
    margin-bottom: 10px;
  }
  
  .filter-actions {
    flex-direction: column;
  }
}
</style>
