<template>
  <div class="category-manage">
    <div class="page-header">
      <h1>分类管理</h1>
      <el-button type="primary" @click="showDialog(null)">新增分类</el-button>
    </div>

    <el-table :data="categories" v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="slug" label="Slug" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="showDialog(row)">编辑</el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="editingCategory ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="dialogForm" ref="dialogFormRef" :rules="dialogRules">
        <el-form-item label="名称" prop="name">
          <el-input v-model="dialogForm.name" />
        </el-form-item>
        <el-form-item label="Slug" prop="slug">
          <el-input v-model="dialogForm.slug" placeholder="例: phone-digital" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { categoryAPI } from '../../api/category'
import { ElMessage } from 'element-plus'

const categories = ref([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const dialogFormRef = ref(null)
const editingCategory = ref(null)

const dialogForm = reactive({ name: '', slug: '' })
const dialogRules = {
  name: [{ required: true, message: '请输入名称' }],
  slug: [{ required: true, message: '请输入Slug' }]
}

function showDialog(cat) {
  if (cat) {
    editingCategory.value = cat
    dialogForm.name = cat.name
    dialogForm.slug = cat.slug
  } else {
    editingCategory.value = null
    dialogForm.name = ''
    dialogForm.slug = ''
  }
  dialogVisible.value = true
}

async function handleSave() {
  const valid = await dialogFormRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (editingCategory.value) {
      await categoryAPI.update(editingCategory.value.id, { ...dialogForm })
      ElMessage.success('更新成功')
    } else {
      await categoryAPI.create({ ...dialogForm })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchCategories()
  } finally {
    saving.value = false
  }
}

async function handleDelete(id) {
  try {
    await categoryAPI.delete(id)
    ElMessage.success('已删除')
    fetchCategories()
  } catch (e) {}
}

async function fetchCategories() {
  loading.value = true
  try {
    const res = await categoryAPI.getList()
    categories.value = res.data.data || []
  } finally {
    loading.value = false
  }
}

onMounted(fetchCategories)
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h1 { font-size: 20px; }
</style>
