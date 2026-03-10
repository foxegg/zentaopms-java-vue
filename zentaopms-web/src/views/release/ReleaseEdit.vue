<template>
  <div>
    <div class="page-header">
      <h1>{{ releaseLang.edit }}</h1>
      <router-link :to="`/release/${id}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
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
          <router-link :to="`/release/${id}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getReleaseById, updateRelease } from '@/api/release'
import { release as releaseLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  try {
    const res = await getReleaseById(id.value)
    const r = res.data
    if (r) {
      form.value = {
        name: r.name || '',
        date: r.date ? String(r.date).slice(0, 10) : '',
        description: r.description || ''
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
    const res = await updateRelease(id.value, {
      name: form.value.name,
      date: form.value.date || null,
      description: form.value.description
    })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/release/${id.value}`)
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
