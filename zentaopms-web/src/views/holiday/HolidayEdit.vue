<template>
  <div>
    <div class="page-header">
      <h1>{{ holidayLang.editAction }}</h1>
      <router-link to="/holiday" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
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
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getHolidayById, updateHoliday } from '@/api/holiday'
import { holiday as holidayLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

function toDateStr(d) {
  if (!d) return ''
  if (typeof d === 'string') return d.slice(0, 10)
  return d
}

onMounted(async () => {
  const id = Number(route.params.id)
  try {
    const res = await getHolidayById(id)
    const h = res.data
    if (h) {
      form.value = {
        name: h.name,
        type: h.type || 'holiday',
        begin: toDateStr(h.begin),
        end: toDateStr(h.end),
        description: h.description || ''
      }
    }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  if (form.value.begin && form.value.end && form.value.begin > form.value.end) {
    errorMsg.value = commonLang.dateRangeError
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateHoliday(route.params.id, form.value)
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
