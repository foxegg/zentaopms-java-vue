<template>
  <div>
    <div class="page-header">
      <h1>{{ branchLang.create }}</h1>
      <router-link to="/branch" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ branchLang.product }} *</label>
          <select v-model.number="form.product" required>
            <option :value="0">{{ commonLang.pleaseSelect }}</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ branchLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/branch" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createBranch } from '@/api/branch'
import { getProductList } from '@/api/product'
import { branch as branchLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const form = ref({ product: 0, name: '' })
const products = ref([])
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  if (!form.value.product) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createBranch(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/branch/${id}`)
    else router.push('/branch')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProductList({ mode: 'noclosed' })
  products.value = r.data || []
  const q = route.query.product
  if (q) form.value.product = Number(q) || 0
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
