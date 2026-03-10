<template>
  <div>
    <div class="page-header">
      <h1>{{ stageLang.create }}</h1>
      <router-link to="/stage" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ commonLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ stageLang.workflowGroup }}</label>
          <input v-model.number="form.workflowGroup" type="number" />
        </div>
        <div class="form-group">
          <label>{{ stageLang.percent }}</label>
          <input v-model="form.percent" />
        </div>
        <div class="form-group">
          <label>{{ userLang.type }}</label>
          <input v-model="form.type" />
        </div>
        <div class="form-group">
          <label>{{ stageLang.projectType }}</label>
          <input v-model="form.projectType" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/stage" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createStage } from '@/api/stage'
import { common as commonLang, stage as stageLang, user as userLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ name: '', workflowGroup: 0, percent: '', type: '', projectType: '' })
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createStage(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/stage/${id}`)
    else router.push('/stage')
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
.form-group input { width: 100%; max-width: 320px; }
.form-actions { margin-top: 1rem; display: flex; gap: 8px; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
