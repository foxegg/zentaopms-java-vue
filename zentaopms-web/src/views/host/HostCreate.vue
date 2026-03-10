<template>
  <div>
    <div class="page-header">
      <h1>{{ hostLang.create }}</h1>
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
        <input v-model="form.type" placeholder="normal" />
      </div>
      <div class="form-group">
        <label>{{ hostLang.status }}</label>
        <input v-model="form.status" placeholder="online" />
      </div>
      <div class="form-actions">
        <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
        <router-link to="/host" class="btn">{{ commonLang.cancel }}</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createHost } from '@/api/host'
import { host as hostLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ name: '', type: 'normal', status: 'online' })
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createHost(form.value)
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
