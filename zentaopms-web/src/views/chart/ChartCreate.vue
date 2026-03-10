<template>
  <div>
    <div class="page-header">
      <h1>{{ chartLang.create }}</h1>
      <router-link to="/chart" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ commonLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ chartLang.code }}</label>
          <input v-model="form.code" />
        </div>
        <div class="form-group">
          <label>{{ userLang.type }}</label>
          <input v-model="form.type" />
        </div>
        <div class="form-group">
          <label>{{ commonLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-group">
          <label>{{ commonLang.status }}</label>
          <select v-model="form.stage">
            <option value="draft">{{ chartLang.stageDraft }}</option>
            <option value="released">{{ chartLang.stageReleased }}</option>
          </select>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/chart" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createChart } from '@/api/chart'
import { common as commonLang, chart as chartLang, user as userLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ name: '', code: '', type: '', description: '', stage: 'draft' })
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createChart(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/chart/${id}`)
    else router.push('/chart')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.form-group { margin-bottom: 1rem; }
.form-group label { display: block; margin-bottom: 4px; }
.form-group input, .form-group select, .form-group textarea { width: 100%; max-width: 320px; }
.form-actions { margin-top: 1rem; display: flex; gap: 8px; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
