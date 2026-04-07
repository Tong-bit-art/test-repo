<template>
  <div class="category-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>专业类目管理</h3>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加类目
          </el-button>
        </div>
      </template>
      
      <!-- 类目列表 -->
      <el-table :data="categories" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="类目名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 类目编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form :model="categoryForm" :rules="rules" ref="categoryFormRef" label-width="100px">
        <el-form-item label="类目名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入类目名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="categoryForm.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { categoryApi } from '../../api'
import { ElMessage } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'

const categories = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加类目')
const categoryFormRef = ref()
const categoryForm = ref({
  id: '',
  name: '',
  description: ''
})

const rules = {
  name: [
    { required: true, message: '请输入类目名称', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadCategories()
})

const loadCategories = async () => {
  try {
    const response = await categoryApi.list()
    categories.value = response
  } catch (error) {
    ElMessage.error('加载专业类目失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '添加类目'
  categoryForm.value = {
    id: '',
    name: '',
    description: ''
  }
  dialogVisible.value = true
}

const handleEdit = (category) => {
  dialogTitle.value = '编辑类目'
  categoryForm.value = { ...category }
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  try {
    await categoryApi.delete(id)
    ElMessage.success('删除成功')
    loadCategories()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const handleSave = async () => {
  if (!categoryFormRef.value) return
  await categoryFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (categoryForm.value.id) {
          await categoryApi.update(categoryForm.value.id, categoryForm.value)
          ElMessage.success('编辑成功')
        } else {
          await categoryApi.save(categoryForm.value)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadCategories()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
}
</script>

<style scoped>
.category-management {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .el-dialog {
    width: 90% !important;
  }
}
</style>
