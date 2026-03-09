<template>
  <div>
    <div class="page-header">
      <h1>新建构建</h1>
      <router-link to="/build" class="btn">返回列表</router-link>
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
          <label>项目 *</label>
          <select v-model.number="form.project" required>
            <option :value="0">请选择</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>日期</label>
          <input v-model="form.date" type="date" />
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/build" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { createBuild } from '@/api/build'
import { getProductList } from '@/api/product'
import { getProjectAll } from '@/api/project'

const router = useRouter()
const route = useRoute()
const form = ref({ product: 0, project: 0, name: '', date: '', description: '' })
const products = ref([])
const projects = ref([])
const submitting = ref(false)

async function onSubmit() {
  if (!form.value.product || !form.value.project) return
  submitting.value = true
  try {
    const payload = { product: form.value.product, project: form.value.project, name: form.value.name, description: form.value.description || undefined }
    if (form.value.date) payload.date = form.value.date
    const res = await createBuild(payload)
    router.push('/build/' + res.id)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const [pr, proj] = await Promise.all([getProductList(), getProjectAll()])
  products.value = pr.data || []
  projects.value = proj.data || []
  if (route.query.product) form.value.product = Number(route.query.product) || 0
  if (route.query.project) form.value.project = Number(route.query.project) || 0
})
</script>
