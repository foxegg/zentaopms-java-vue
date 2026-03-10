<template>
  <div>
    <div class="page-header">
      <h1>{{ bugLang.edit }}</h1>
      <router-link :to="`/bug/${id}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
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
          <router-link :to="`/bug/${id}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBugById, updateBug } from '@/api/bug'
import { getBuildByProduct } from '@/api/build'
import { getProductList } from '@/api/product'
import { bug as bugLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const products = ref([])
const builds = ref([])
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

async function loadBuilds(productId) {
  if (!productId) {
    builds.value = []
    if (form.value) form.value.openedBuild = ''
    return
  }
  try {
    const res = await getBuildByProduct(productId)
    builds.value = res?.data ?? res ?? []
    if (form.value && !builds.value.some(b => (b.name || b.id) === form.value.openedBuild)) form.value.openedBuild = ''
  } catch {
    builds.value = []
    if (form.value) form.value.openedBuild = ''
  }
}

watch(() => form.value?.product, (productId) => {
  if (form.value) loadBuilds(productId)
})

onMounted(async () => {
  try {
    const [bugRes, prRes] = await Promise.all([getBugById(id.value), getProductList({ mode: 'noclosed' })])
    const b = bugRes?.data ?? null
    products.value = prRes?.data ?? prRes ?? []
    if (b) {
      const productId = b.product ?? 0
      const openedBuild = b.openedBuild != null && b.openedBuild !== '' ? String(b.openedBuild).split(',')[0].trim() : ''
      form.value = {
        product: productId,
        openedBuild,
        title: b.title || '',
        severity: b.severity ?? 3,
        pri: b.pri ?? 3,
        steps: b.steps || ''
      }
      await loadBuilds(productId)
      if (form.value && builds.value.length && !form.value.openedBuild && b.openedBuild) {
        const first = String(b.openedBuild).split(',')[0].trim()
        if (builds.value.some(x => (x.name || x.id) === first)) form.value.openedBuild = first
      }
    }
  } catch {
    form.value = null
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  if (!form.value.title || !form.value.openedBuild) {
    errorMsg.value = commonLang.operateFail
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateBug(id.value, {
      product: form.value.product,
      openedBuild: form.value.openedBuild,
      title: form.value.title,
      severity: form.value.severity,
      pri: form.value.pri,
      steps: form.value.steps
    })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/bug/${id.value}`)
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
