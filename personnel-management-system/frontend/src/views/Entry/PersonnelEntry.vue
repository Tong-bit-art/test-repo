<template>
  <div class="personnel-entry">
    <el-card>
      <template #header>
        <h3>人员录入</h3>
      </template>
      
      <!-- 录入方式选择 -->
      <el-tabs v-model="activeTab">
        <el-tab-pane label="手动录入">
          <el-form :model="personnelForm" :rules="rules" ref="personnelFormRef" label-width="120px" class="entry-form">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="personnelForm.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-select v-model="personnelForm.gender" placeholder="请选择性别">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="personnelForm.age" :min="1" :max="150" />
            </el-form-item>
            <el-form-item label="专业类目" prop="categoryId">
              <el-select v-model="personnelForm.categoryId" placeholder="请选择专业类目">
                <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="部门" prop="department">
              <el-input v-model="personnelForm.department" placeholder="请输入部门" />
            </el-form-item>
            <el-form-item label="职位" prop="position">
              <el-input v-model="personnelForm.position" placeholder="请输入职位" />
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model="personnelForm.phone" placeholder="请输入电话" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="personnelForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="地址" prop="address">
              <el-input v-model="personnelForm.address" type="textarea" placeholder="请输入地址" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSubmit" :loading="loading">
                <el-icon><Check /></el-icon>
                提交
              </el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="Excel导入">
          <div class="import-section">
            <el-upload
              class="upload-demo"
              action=""
              :auto-upload="false"
              :on-change="handleFileChange"
              :show-file-list="false"
              accept=".xlsx,.xls"
            >
              <el-button type="primary">
                <el-icon><Upload /></el-icon>
                选择Excel文件
              </el-button>
            </el-upload>
            <div v-if="selectedFile" class="file-info">
              <span>{{ selectedFile.name }}</span>
              <el-button type="danger" size="small" @click="selectedFile = null">
                <el-icon><Delete /></el-icon>
                移除
              </el-button>
            </div>
            <el-button type="success" @click="handleImport" :loading="importLoading" :disabled="!selectedFile">
              <el-icon><Check /></el-icon>
              开始导入
            </el-button>
            <div class="import-tip">
              <p>Excel文件格式说明：</p>
              <p>1. 第一行为表头：姓名,性别,年龄,专业类目,部门,职位,电话,邮箱,地址</p>
              <p>2. 从第二行开始填写数据</p>
              <p>3. 专业类目请填写已存在的类目名称</p>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { personnelApi, categoryApi } from '../../api'
import { ElMessage } from 'element-plus'
import { Check, Upload, Delete } from '@element-plus/icons-vue'

const activeTab = ref('0')
const categories = ref([])
const loading = ref(false)
const importLoading = ref(false)
const selectedFile = ref(null)
const personnelFormRef = ref()
const personnelForm = ref({
  name: '',
  gender: '',
  age: '',
  categoryId: '',
  department: '',
  position: '',
  phone: '',
  email: '',
  address: ''
})

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'blur' }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择专业类目', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请输入部门', trigger: 'blur' }
  ],
  position: [
    { required: true, message: '请输入职位', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' }
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

const handleSubmit = async () => {
  if (!personnelFormRef.value) return
  await personnelFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await personnelApi.save(personnelForm.value)
        ElMessage.success('提交成功')
        resetForm()
      } catch (error) {
        ElMessage.error('提交失败')
      } finally {
        loading.value = false
      }
    }
  })
}

const resetForm = () => {
  personnelForm.value = {
    name: '',
    gender: '',
    age: '',
    categoryId: '',
    department: '',
    position: '',
    phone: '',
    email: '',
    address: ''
  }
  if (personnelFormRef.value) {
    personnelFormRef.value.resetFields()
  }
}

const handleFileChange = (file) => {
  selectedFile.value = file.raw
}

const handleImport = async () => {
  if (!selectedFile.value) return
  
  importLoading.value = true
  const formData = new FormData()
  formData.append('file', selectedFile.value)
  
  try {
    await personnelApi.importExcel(formData)
    ElMessage.success('导入成功')
    selectedFile.value = null
  } catch (error) {
    ElMessage.error('导入失败')
  } finally {
    importLoading.value = false
  }
}
</script>

<style scoped>
.personnel-entry {
  height: 100%;
}

.entry-form {
  margin-top: 20px;
}

.import-section {
  margin-top: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.file-info {
  margin: 20px 0;
  padding: 10px;
  background: #ecf5ff;
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.import-tip {
  margin-top: 30px;
  padding: 15px;
  background: #fef0f0;
  border: 1px solid #fbc4c4;
  border-radius: 4px;
  font-size: 14px;
  color: #606266;
}

.import-tip p {
  margin: 5px 0;
}

@media (max-width: 768px) {
  .entry-form {
    margin-top: 10px;
  }
  
  .import-section {
    padding: 10px;
  }
  
  .el-form-item {
    margin-bottom: 15px;
  }
}
</style>
