<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.ssoTitle }}</h1>
      <router-link to="/admin" class="btn">{{ commonLang.backAdmin }}</router-link>
    </div>
    <p v-if="successMsg" class="text-success">{{ successMsg }}</p>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ adminLang.ssoTurnon || 'turnon' }}</label>
          <input v-model="config.turnon" />
        </div>
        <div class="form-group">
          <label>{{ adminLang.ssoAddr || 'addr' }}</label>
          <input v-model="config.addr" />
        </div>
        <div class="form-group">
          <label>{{ adminLang.ssoCode || 'code' }}</label>
          <input v-model="config.code" />
        </div>
        <div class="form-group">
          <label>{{ adminLang.ssoKey || 'key' }}</label>
          <input v-model="config.key" type="password" autocomplete="off" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
        </div>
      </form>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { getSsoSetting, saveSsoSetting } from '@/api/sso'
import { common as commonLang, admin as adminLang } from '@/lang/zh-cn'

const config = reactive({ turnon: '0', addr: '', code: '', key: '' })
const loading = ref(true)
const submitting = ref(false)
const successMsg = ref('')
const errorMsg = ref('')

onMounted(async () => {
  try {
    const res = await getSsoSetting()
    const data = res.data || res || {}
    config.turnon = data.turnon != null ? String(data.turnon) : '0'
    config.addr = data.addr != null ? String(data.addr) : ''
    config.code = data.code != null ? String(data.code) : ''
    config.key = data.key != null ? String(data.key) : ''
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  successMsg.value = ''
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await saveSsoSetting({
      turnon: config.turnon,
      addr: config.addr,
      code: config.code,
      key: config.key
    })
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
