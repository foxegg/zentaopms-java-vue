<template>
  <div v-if="form">
    <div class="page-header">
      <h1>{{ hostLang.edit }}</h1>
      <router-link to="/host" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <form @submit.prevent="onSubmit" class="form-wrap">
      <div class="form-group">
        <label>{{ hostLang.name }} *</label>
        <input v-model="form.name" required />
      </div>
      <div class="form-group">
        <label>{{ hostLang.type }}</label>
        <input v-model="form.type" />
      </div>
      <div class="form-group">
        <label>{{ hostLang.status }}</label>
        <input v-model="form.status" />
      </div>
      <div class="form-actions">
        <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
        <router-link to="/host" class="btn">{{ commonLang.cancel }}</router-link>
      </div>
    </form>
  </div>
  <p v-else-if="!loading">{{ commonLang.notFound }}</p>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getHostById, updateHost } from '@/api/host'
import { host as hostLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  try {
    const res = await getHostById(route.params.id)
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
    const res = await updateHost(route.params.id, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push('/host')
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
