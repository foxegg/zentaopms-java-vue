<template>
  <div>
    <div class="page-header">
      <h1>{{ executionLang.edit }}</h1>
      <router-link :to="`/execution/${executionId}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ executionLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ executionLang.code }}</label>
          <input v-model="form.code" />
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
          <router-link :to="`/execution/${executionId}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getExecutionById, updateExecution } from '@/api/execution'
import { execution as executionLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const executionId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = executionId.value
  loading.value = true
  try {
    const res = await getExecutionById(id)
    const e = res.data
    if (e) form.value = { name: e.name, code: e.code || '', begin: e.begin || '', end: e.end || '' }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  if (form.value.begin && form.value.end && form.value.begin > form.value.end) {
    errorMsg.value = commonLang.dateRangeError
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateExecution(executionId.value, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/execution/${executionId.value}`)
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
