<template>
  <div>
    <div class="page-header">
      <h1>{{ productplanLang.create }}</h1>
      <router-link :to="backUrl" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ productLang.common }} *</label>
          <select v-model.number="form.product" required>
            <option :value="0">{{ commonLang.pleaseSelect }}</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ commonLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ executionLang.begin }}</label>
          <input v-model="form.begin" type="date" />
        </div>
        <div class="form-group">
          <label>{{ executionLang.end }}</label>
          <input v-model="form.end" type="date" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link :to="backUrl" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createProductPlan } from '@/api/productplan'
import { getProductList } from '@/api/product'
import { common as commonLang, product as productLang, productplan as productplanLang, execution as executionLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const form = ref({ product: 0, name: '', begin: '', end: '' })
const products = ref([])
const submitting = ref(false)
const errorMsg = ref('')

const backUrl = computed(() => form.value.product ? `/productplan?product=${form.value.product}` : '/productplan')

async function onSubmit() {
  if (!form.value.product) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createProductPlan(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/productplan/${id}`)
    else router.push(backUrl.value)
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProductList({ mode: 'noclosed' })
  products.value = r?.data ?? []
  const q = route.query.product
  if (q) form.value.product = Number(q) || 0
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
