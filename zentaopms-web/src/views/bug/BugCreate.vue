<template>
  <div>
    <div class="page-header">
      <h1>{{ bugLang.create }}</h1>
      <router-link to="/bug" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ bugLang.product }} *</label>
          <select v-model.number="form.product" required>
            <option :value="0">{{ bugLang.selectProduct }}</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ bugLang.openedBuild }} *</label>
          <select v-model="form.openedBuild" required :disabled="!form.product">
            <option value="">{{ form.product ? bugLang.selectBuild : bugLang.selectProduct }}</option>
            <option v-for="b in builds" :key="b.id" :value="b.name || b.id">{{ b.name || ('#' + b.id) }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ bugLang.project }}</label>
          <select v-model.number="form.project">
            <option :value="0">{{ bugLang.selectProject }}</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ bugLang.title }} *</label>
          <input v-model="form.title" required />
        </div>
        <div class="form-group">
          <label>{{ bugLang.severity }}</label>
          <select v-model.number="form.severity">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ bugLang.pri }}</label>
          <select v-model.number="form.pri">
            <option v-for="n in 4" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ bugLang.steps }}</label>
          <textarea v-model="form.steps" rows="4"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/bug" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { createBug } from '@/api/bug'
import { getBuildByProduct } from '@/api/build'
import { getProductList } from '@/api/product'
import { getProjectAll } from '@/api/project'
import { bug as bugLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const route = useRoute()
const form = ref({ product: 0, project: 0, openedBuild: '', title: '', severity: 3, pri: 3, steps: '' })
const products = ref([])
const projects = ref([])
const builds = ref([])
const submitting = ref(false)
const errorMsg = ref('')

async function loadBuilds(productId) {
  if (!productId) {
    builds.value = []
    form.value.openedBuild = ''
    return
  }
  try {
    const res = await getBuildByProduct(productId)
    builds.value = res?.data ?? res ?? []
    if (!builds.value.some(b => (b.name || b.id) === form.value.openedBuild)) form.value.openedBuild = ''
  } catch {
    builds.value = []
    form.value.openedBuild = ''
  }
}

async function onSubmit() {
  if (!form.value.product) return
  if (!form.value.openedBuild || !form.value.title) {
    errorMsg.value = commonLang.operateFail
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const payload = {
      product: form.value.product,
      title: form.value.title,
      openedBuild: form.value.openedBuild,
      severity: form.value.severity,
      pri: form.value.pri,
      steps: form.value.steps || undefined
    }
    if (form.value.project) payload.project = form.value.project
    const res = await createBug(payload)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/bug/${id}`)
    else router.push('/bug')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

watch(() => form.value.product, (id) => loadBuilds(id))

onMounted(async () => {
  const [pr, proj] = await Promise.all([getProductList({ mode: 'noclosed' }), getProjectAll()])
  products.value = pr?.data ?? pr ?? []
  projects.value = proj?.data ?? proj ?? []
  if (route.query.product) form.value.product = Number(route.query.product) || 0
  if (route.query.project) form.value.project = Number(route.query.project) || 0
  await loadBuilds(form.value.product)
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
