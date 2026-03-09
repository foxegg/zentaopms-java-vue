<template>
  <div>
    <div class="page-header">
      <h1>新建 Bug</h1>
      <router-link to="/bug" class="btn">返回列表</router-link>
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
          <label>项目</label>
          <select v-model.number="form.project">
            <option :value="0">请选择</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>标题 *</label>
          <input v-model="form.title" required />
        </div>
        <div class="form-group">
          <label>严重程度</label>
          <select v-model.number="form.severity">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
          </select>
        </div>
        <div class="form-group">
          <label>优先级</label>
          <select v-model.number="form.pri">
            <option v-for="n in 4" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>复现步骤</label>
          <textarea v-model="form.steps" rows="4"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/bug" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { createBug } from '@/api/bug'
import { getProductList } from '@/api/product'
import { getProjectAll } from '@/api/project'

const router = useRouter()
const route = useRoute()
const form = ref({ product: 0, project: 0, title: '', severity: 3, pri: 2, steps: '' })
const products = ref([])
const projects = ref([])
const submitting = ref(false)

async function onSubmit() {
  if (!form.value.product) return
  submitting.value = true
  try {
    const payload = {
      product: form.value.product,
      title: form.value.title,
      severity: form.value.severity,
      pri: form.value.pri,
      steps: form.value.steps || undefined
    }
    if (form.value.project) payload.project = form.value.project
    const res = await createBug(payload)
    router.push(`/bug/${res.id}`)
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
