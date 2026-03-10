<template>
  <div>
    <div class="page-header">
      <h1>{{ taskLang.edit }}</h1>
      <router-link :to="`/task/${id}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ taskLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ taskLang.estimate }}</label>
          <input v-model="form.estimate" type="number" step="0.5" min="0" />
        </div>
        <div class="form-group">
          <label>{{ taskLang.deadline }}</label>
          <input v-model="form.deadline" type="date" />
        </div>
        <div class="form-group">
          <label>{{ taskLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link :to="`/task/${id}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTaskById, updateTask } from '@/api/task'
import { task as taskLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  try {
    const res = await getTaskById(id.value)
    const t = res.data
    if (t) {
      form.value = {
        name: t.name || '',
        estimate: t.estimate ?? '',
        deadline: t.deadline ? String(t.deadline).slice(0, 10) : '',
        description: t.description || ''
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
    const payload = { name: form.value.name, description: form.value.description }
    if (form.value.estimate !== '' && form.value.estimate != null) payload.estimate = Number(form.value.estimate)
    if (form.value.deadline !== undefined) payload.deadline = form.value.deadline || null
    const res = await updateTask(id.value, payload)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/task/${id.value}`)
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
