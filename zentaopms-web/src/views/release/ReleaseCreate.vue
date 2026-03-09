<template>
  <div>
    <div class="page-header">
      <h1>新建版本</h1>
      <router-link to="/release" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>产品 *</label>
          <select v-model.number="form.product" required>
            <option :value="0">请选择</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>发布日期</label>
          <input v-model="form.date" type="date" />
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/release" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { createRelease } from '@/api/release'
import { getProductList } from '@/api/product'

const router = useRouter()
const route = useRoute()
const form = ref({ product: 0, name: '', date: '', description: '' })
const products = ref([])
const submitting = ref(false)

async function onSubmit() {
  if (!form.value.product) return
  submitting.value = true
  try {
    const payload = {
      product: form.value.product,
      name: form.value.name,
      description: form.value.description || undefined
    }
    if (form.value.date) payload.date = form.value.date
    const res = await createRelease(payload)
    router.push(`/release/${res.id}`)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProductList()
  products.value = r.data || []
  if (route.query.product) form.value.product = Number(route.query.product) || 0
})
</script>
