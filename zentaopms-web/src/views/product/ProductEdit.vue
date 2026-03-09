<template>
  <div>
    <div class="page-header">
      <h1>编辑产品</h1>
      <router-link :to="`/product/${id}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required maxlength="110" />
        </div>
        <div class="form-group">
          <label>代号</label>
          <input v-model="form.code" maxlength="45" />
        </div>
        <div class="form-group">
          <label>类型</label>
          <select v-model="form.type">
            <option value="normal">正常</option>
          </select>
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/product/${id}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductById, updateProduct } from '@/api/product'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await getProductById(id.value)
    const p = res.data
    if (p) {
      form.value = {
        name: p.name || '',
        code: p.code || '',
        type: p.type || 'normal',
        description: p.description || ''
      }
    }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateProduct(id.value, form.value)
    router.push(`/product/${id.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
