<template>
  <div>
    <div class="page-header">
      <h1>{{ holidayLang.createAction }}</h1>
      <router-link to="/holiday" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ holidayLang.name }} *</label>
          <input v-model="form.name" required maxlength="30" />
        </div>
        <div class="form-group">
          <label>{{ holidayLang.type }}</label>
          <select v-model="form.type">
            <option value="holiday">{{ holidayLang.typeList.holiday }}</option>
            <option value="working">{{ holidayLang.typeList.working }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ holidayLang.begin }} *</label>
          <input v-model="form.begin" type="date" required />
        </div>
        <div class="form-group">
          <label>{{ holidayLang.end }} *</label>
          <input v-model="form.end" type="date" required />
        </div>
        <div class="form-group">
          <label>{{ holidayLang.desc }}</label>
          <textarea v-model="form.description" rows="2"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/holiday" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createHoliday } from '@/api/holiday'
import { holiday as holidayLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ name: '', type: 'holiday', begin: '', end: '', description: '' })
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  if (form.value.begin && form.value.end && form.value.begin > form.value.end) {
    errorMsg.value = commonLang.dateRangeError
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createHoliday(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push('/holiday')
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
