<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.cacheTitle }}</h1>
      <router-link to="/admin" class="btn">{{ commonLang.backAdmin }}</router-link>
    </div>
    <p v-if="successMsg" class="text-success">{{ successMsg }}</p>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <div class="mb-2">
        <button class="btn btn-primary" :disabled="flushing" @click="doFlush">{{ adminLang.flushCache }}</button>
      </div>
      <div v-if="setting && Object.keys(setting).length">
        <h3>{{ adminLang.cacheConfig }}</h3>
        <pre>{{ JSON.stringify(setting, null, 2) }}</pre>
      </div>
      <p v-else>{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCacheSetting, flushCache } from '@/api/cache'
import { common as commonLang, admin as adminLang } from '@/lang/zh-cn'

const setting = ref(null)
const loading = ref(true)
const flushing = ref(false)
const successMsg = ref('')
const errorMsg = ref('')

async function load() {
  loading.value = true
  try {
    const res = await getCacheSetting()
    setting.value = res.data || {}
  } finally {
    loading.value = false
  }
}

async function doFlush() {
  successMsg.value = ''
  errorMsg.value = ''
  flushing.value = true
  try {
    const res = await flushCache()
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    successMsg.value = commonLang.cacheCleared
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || e.message || commonLang.operateFail
  } finally {
    flushing.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.text-success { color: #0a0; margin-bottom: 0.5rem; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
