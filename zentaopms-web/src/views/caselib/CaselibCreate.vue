<template>
  <div>
    <div class="page-header">
      <h1>{{ caselibLang.create }}</h1>
      <router-link to="/caselib" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ commonLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ productLang.common }}</label>
          <select v-model.number="form.product">
            <option :value="0">{{ caselibLang.noProduct }}</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ projectLang.common }}</label>
          <select v-model.number="form.project">
            <option :value="0">无</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ productLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/caselib" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { createCaselib } from '@/api/caselib'
import { getProductList } from '@/api/product'
import { getProjectAll } from '@/api/project'
import { common as commonLang, product as productLang, project as projectLang, caselib as caselibLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ name: '', product: 0, project: 0, description: '' })
const submitting = ref(false)
const errorMsg = ref('')
const products = ref([])
const projects = ref([])

onMounted(async () => {
  const [pr, proj] = await Promise.all([getProductList({ mode: 'noclosed' }), getProjectAll()])
  products.value = pr?.data ?? []
  projects.value = proj?.data ?? []
})

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createCaselib(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/caselib/${id}`)
    else router.push('/caselib')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.form-group { margin-bottom: 1rem; }
.form-group label { display: block; margin-bottom: 4px; }
.form-group input, .form-group select, .form-group textarea { width: 100%; max-width: 320px; }
.form-actions { margin-top: 1rem; display: flex; gap: 8px; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
