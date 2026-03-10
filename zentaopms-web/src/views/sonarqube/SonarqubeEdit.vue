<template>
  <div v-if="form">
    <div class="page-header">
      <h1>SonarQube</h1>
      <router-link to="/sonarqube" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <form @submit.prevent="onSubmit" class="form-wrap">
      <div class="form-group">
        <label>{{ commonLang.name }} *</label>
        <input v-model="form.name" required />
      </div>
      <div class="form-group">
        <label>URL *</label>
        <input v-model="form.url" required />
      </div>
      <div class="form-group">
        <label>{{ commonLang.account }}</label>
        <input v-model="form.account" />
      </div>
      <div class="form-actions">
        <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
        <router-link to="/sonarqube" class="btn">{{ commonLang.cancel }}</router-link>
      </div>
    </form>
  </div>
  <p v-else-if="!loading">{{ commonLang.notFound }}</p>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSonarqubeById, updateSonarqube } from '@/api/sonarqube'
import { common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  try {
    const res = await getSonarqubeById(route.params.id)
    form.value = res?.data ?? res
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateSonarqube(route.params.id, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push('/sonarqube')
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
