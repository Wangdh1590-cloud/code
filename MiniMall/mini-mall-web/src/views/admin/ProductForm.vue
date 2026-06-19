<template>
  <div class="product-form">
    <h1>{{ isEdit ? '编辑商品' : '新增商品' }}</h1>
    <el-card>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" v-loading="loading">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Slug" prop="slug">
          <el-input v-model="form.slug" placeholder="URL标识，如 iphone-15" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="选择分类">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格(元)" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="1" style="width:200px;" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" style="width:200px;" />
        </el-form-item>
        <el-form-item label="图片URL" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="https://..." />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="是否上架" prop="isActive">
          <el-switch v-model="form.isActive" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productAPI } from '../../api/product'
import { categoryAPI } from '../../api/category'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const saving = ref(false)
const loading = ref(false)
const categories = ref([])
const isEdit = computed(() => !!route.params.id)

const form = reactive({
  name: '', slug: '', description: '', price: 0, imageUrl: '', stock: 999, isActive: true, categoryId: null
})

const rules = {
  name: [{ required: true, message: '请输入名称' }],
  slug: [{ required: true, message: '请输入Slug' }],
  categoryId: [{ required: true, message: '请选择分类' }],
}

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    const data = {
      ...form,
      price: Math.round(form.price * 100)  // 元转分
    }
    if (isEdit.value) {
      await productAPI.update(route.params.id, data)
      ElMessage.success('更新成功')
    } else {
      await productAPI.create(data)
      ElMessage.success('创建成功')
    }
    router.push('/admin/products')
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  const cRes = await categoryAPI.getList()
  categories.value = cRes.data.data || []

  if (isEdit.value) {
    loading.value = true
    try {
      const res = await productAPI.getById(route.params.id)
      const p = res.data.data
      Object.assign(form, {
        name: p.name, slug: p.slug, description: p.description || '',
        price: p.price / 100, imageUrl: p.imageUrl || '', stock: p.stock,
        isActive: !!p.isActive, categoryId: p.categoryId
      })
    } finally {
      loading.value = false
    }
  }
})
</script>

<style scoped>
.product-form { max-width: 700px; }
.product-form h1 { margin-bottom: 24px; }
</style>
