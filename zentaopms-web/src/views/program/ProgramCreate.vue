<template>
  <div>
    <div class="page-header">
      <h1>{{ programLang.create }}</h1>
      <router-link to="/program" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ programLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/program" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createProgram } from '@/api/program'
import { program as programLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ name: '' })
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createProgram(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/program/${id}`)
    else router.push('/program')
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
