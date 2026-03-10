<template>
  <div>
    <div class="page-header">
      <h1>{{ releaseLang.create }}</h1>
      <router-link to="/release" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ releaseLang.product }} *</label>
          <select v-model.number="form.product" required>
            <option :value="0">{{ releaseLang.selectProduct }}</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ releaseLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ releaseLang.date }}</label>
          <input v-model="form.date" type="date" />
        </div>
        <div class="form-group">
          <label>{{ releaseLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/release" class="btn">{{ commonLang.cancel }}</router-link>
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
import { release as releaseLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const route = useRoute()
const form = ref({ product: 0, name: '', date: '', description: '' })
const products = ref([])
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  if (!form.value.product) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const payload = {
      product: form.value.product,
      name: form.value.name,
      description: form.value.description || undefined
    }
    if (form.value.date) payload.date = form.value.date
    const res = await createRelease(payload)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/release/${id}`)
    else router.push('/release')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProductList({ mode: 'noclosed' })
  products.value = r.data || []
  if (route.query.product) form.value.product = Number(route.query.product) || 0
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
