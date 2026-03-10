<template>
  <div>
    <div class="page-header">
      <h1>{{ buildLang.create }}</h1>
      <router-link to="/build" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ buildLang.product }} *</label>
          <select v-model.number="form.product" required>
            <option :value="0">{{ buildLang.selectProduct }}</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ buildLang.project }} *</label>
          <select v-model.number="form.project" required>
            <option :value="0">{{ buildLang.selectProject }}</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ buildLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ buildLang.date }}</label>
          <input v-model="form.date" type="date" />
        </div>
        <div class="form-group">
          <label>{{ buildLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/build" class="btn">{{ commonLang.cancel }}</router-link>
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
import { build as buildLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const route = useRoute()
const form = ref({ product: 0, project: 0, name: '', date: '', description: '' })
const products = ref([])
const projects = ref([])
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  if (!form.value.product || !form.value.project) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const payload = { product: form.value.product, project: form.value.project, name: form.value.name, description: form.value.description || undefined }
    if (form.value.date) payload.date = form.value.date
    const res = await createBuild(payload)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push('/build/' + id)
    else router.push('/build')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const [pr, proj] = await Promise.all([getProductList({ mode: 'noclosed' }), getProjectAll()])
  products.value = pr.data || []
  projects.value = proj.data || []
  if (route.query.product) form.value.product = Number(route.query.product) || 0
  if (route.query.project) form.value.project = Number(route.query.project) || 0
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
