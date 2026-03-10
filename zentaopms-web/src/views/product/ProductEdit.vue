<template>
  <div>
    <div class="page-header">
      <h1>{{ productLang.edit }}</h1>
      <router-link :to="`/product/${id}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ productLang.name }} *</label>
          <input v-model="form.name" required maxlength="110" />
        </div>
        <div class="form-group">
          <label>{{ productLang.code }}</label>
          <input v-model="form.code" maxlength="45" />
        </div>
        <div class="form-group">
          <label>{{ productLang.type }}</label>
          <select v-model="form.type">
            <option value="normal">{{ productLang.typeList.normal }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ productLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link :to="`/product/${id}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductById, updateProduct } from '@/api/product'
import { product as productLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  loading.value = true
  try {
    const res = await getProductById(id.value)
    const p = res.data
    if (p) {
      form.value = {
        name: p.name || '',
        code: p.code || '',
        type: p.type || 'normal',
        description: p.description || ''
      }
    }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateProduct(id.value, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/product/${id.value}`)
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
