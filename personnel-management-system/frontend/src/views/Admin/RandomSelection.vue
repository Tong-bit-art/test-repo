<template>
  <div class="random-selection">
    <el-card>
      <template #header>
        <h3>随机抽取</h3>
      </template>
      
      <!-- 抽取类型选择 -->
      <el-form :model="selectionForm" label-width="120px" class="selection-form">
        <el-form-item label="抽取类型">
          <el-radio-group v-model="selectionForm.selectionType" @change="handleSelectionTypeChange">
            <el-radio label="pure_random">纯随机抽取</el-radio>
            <el-radio label="specified_range">指定范围抽取</el-radio>
            <el-radio label="specified_personnel">指定人员抽取</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <!-- 抽取人数 -->
        <el-form-item label="抽取人数">
          <el-input-number v-model="selectionForm.count" :min="1" :max="100" />
        </el-form-item>
        
        <!-- 指定范围抽取的筛选条件 -->
        <div v-if="selectionForm.selectionType === 'specified_range'" class="range-filter">
          <el-form :model="filterForm" label-width="80px">
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
            </el-row>
          </el-form>
        </div>
        
        <!-- 指定人员抽取的人员选择 -->
        <div v-if="selectionForm.selectionType === 'specified_personnel'" class="personnel-select">
          <el-form-item label="选择人员">
            <el-select
              v-model="selectionForm.personnelIds"
              multiple
              placeholder="请选择人员"
              filterable
              class="personnel-select"
            >
              <el-option v-for="personnel in allPersonnel" :key="personnel.id" :label="personnel.name" :value="personnel.id" />
            </el-select>
          </el-form-item>
        </div>
        
        <el-form-item>
          <el-button type="primary" @click="handleSelection" :loading="loading" :disabled="isSelecting">
            <el-icon><Refresh /></el-icon>
            {{ isSelecting ? '抽取中...' : '开始抽取' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 抽取动画区域 -->
      <div v-if="isSelecting" class="selection-animation">
        <div class="animation-container">
          <h2>{{ animationPerson }}</h2>
        </div>
      </div>
      
      <!-- 抽取结果 -->
      <div v-if="selectionResult" class="selection-result">
        <h4>抽取结果</h4>
        <el-table :data="selectionResult.results" style="width: 100%">
          <el-table-column prop="personnel.name" label="姓名" />
          <el-table-column prop="personnel.gender" label="性别" />
          <el-table-column prop="personnel.age" label="年龄" />
          <el-table-column prop="personnel.department" label="部门" />
          <el-table-column prop="personnel.position" label="职位" />
          <el-table-column prop="personnel.phone" label="电话" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { personnelApi, categoryApi } from '../../api'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'

const selectionForm = ref({
  selectionType: 'pure_random',
  count: 1,
  personnelIds: [],
  filter: {
    name: '',
    gender: '',
    categoryId: '',
    department: '',
    position: '',
    phone: '',
    email: ''
  }
})

const filterForm = ref({
  name: '',
  gender: '',
  categoryId: '',
  department: '',
  position: '',
  phone: '',
  email: ''
})

const categories = ref([])
const allPersonnel = ref([])
const selectionResult = ref(null)
const loading = ref(false)
const isSelecting = ref(false)
const animationPerson = ref('')

onMounted(() => {
  loadCategories()
  loadAllPersonnel()
})

const loadCategories = async () => {
  try {
    const response = await categoryApi.list()
    categories.value = response
  } catch (error) {
    ElMessage.error('加载专业类目失败')
  }
}

const loadAllPersonnel = async () => {
  try {
    const response = await personnelApi.list({ page: 1, size: 1000 })
    allPersonnel.value = response.content
  } catch (error) {
    ElMessage.error('加载人员信息失败')
  }
}

const handleSelectionTypeChange = () => {
  selectionResult.value = null
}

const handleSelection = async () => {
  if (selectionForm.value.selectionType === 'specified_personnel' && selectionForm.value.personnelIds.length === 0) {
    ElMessage.error('请选择人员')
    return
  }

  loading.value = true
  try {
    const requestData = {
      selectionType: selectionForm.value.selectionType,
      count: selectionForm.value.count
    }

    if (selectionForm.value.selectionType === 'specified_range') {
      requestData.filter = {
        name: filterForm.value.name,
        gender: filterForm.value.gender,
        categoryId: filterForm.value.categoryId,
        department: filterForm.value.department,
        position: filterForm.value.position,
        phone: filterForm.value.phone,
        email: filterForm.value.email,
        page: 1,
        size: 1000
      }
    } else if (selectionForm.value.selectionType === 'specified_personnel') {
      requestData.personnelIds = selectionForm.value.personnelIds
    }

    // 显示动画效果
    isSelecting.value = true
    await showAnimation()

    // 调用后端API进行抽取
    const response = await personnelApi.randomSelection(requestData)
    selectionResult.value = response
    ElMessage.success('抽取成功')
  } catch (error) {
    ElMessage.error('抽取失败')
  } finally {
    loading.value = false
    isSelecting.value = false
  }
}

const showAnimation = () => {
  return new Promise((resolve) => {
    let currentIndex = 0
    const interval = setInterval(() => {
      const randomIndex = Math.floor(Math.random() * allPersonnel.value.length)
      animationPerson.value = allPersonnel.value[randomIndex].name
      currentIndex++
      
      // 动画持续3-5秒
      if (currentIndex > 30) {
        clearInterval(interval)
        resolve()
      }
    }, 100)
  })
}
</script>

<style scoped>
.random-selection {
  height: 100%;
}

.selection-form {
  margin-bottom: 20px;
}

.range-filter {
  margin: 20px 0;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.personnel-select {
  width: 100%;
}

.selection-animation {
  margin: 40px 0;
  padding: 60px;
  background: #f0f9eb;
  border-radius: 8px;
  text-align: center;
}

.animation-container h2 {
  font-size: 48px;
  color: #67c23a;
  font-weight: bold;
  animation: pulse 0.5s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.selection-result {
  margin-top: 40px;
}

.selection-result h4 {
  margin-bottom: 20px;
  color: #303133;
}

@media (max-width: 768px) {
  .range-filter {
    padding: 10px;
  }
  
  .selection-animation {
    padding: 30px;
  }
  
  .animation-container h2 {
    font-size: 32px;
  }
}
</style>
