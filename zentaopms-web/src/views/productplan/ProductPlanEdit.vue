<template>
  <div>
    <div class="page-header">
      <h1>{{ productplanLang.edit }}</h1>
      <router-link :to="`/productplan/${planId}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
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
          <router-link :to="`/productplan/${planId}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ productplanLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductPlanById, updateProductPlan } from '@/api/productplan'
import { common as commonLang, productplan as productplanLang, execution as executionLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const planId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  const id = planId.value
  loading.value = true
  try {
    const res = await getProductPlanById(id)
    const p = res?.data ?? res
    if (p) form.value = { name: p.name, begin: p.begin || '', end: p.end || '' }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateProductPlan(planId.value, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/productplan/${planId.value}`)
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
