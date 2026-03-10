<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.message }}</h1>
      <router-link to="/admin" class="btn">{{ commonLang.backAdmin }}</router-link>
    </div>
    <p v-if="successMsg" class="text-success">{{ successMsg }}</p>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <form @submit.prevent="onSubmit">
        <div class="form-group" v-for="(v, k) in config" :key="k">
          <label>{{ k }}</label>
          <input v-model="config[k]" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
        </div>
      </form>
      <p v-if="Object.keys(config).length === 0">{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMessageConfig, saveMessageConfig } from '@/api/message'
import { common as commonLang, admin as adminLang } from '@/lang/zh-cn'

const config = ref({})
const loading = ref(true)
const submitting = ref(false)
const successMsg = ref('')
const errorMsg = ref('')

onMounted(async () => {
  try {
    const res = await getMessageConfig()
    config.value = res.data || {}
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  successMsg.value = ''
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await saveMessageConfig(config.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    successMsg.value = commonLang.saveSuccess
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || e.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.form-group { margin-bottom: 1rem; }
.form-group label { display: block; margin-bottom: 4px; }
.form-group input { width: 100%; max-width: 320px; }
.form-actions { margin-top: 1rem; }
.text-success { color: #0a0; margin-bottom: 0.5rem; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
