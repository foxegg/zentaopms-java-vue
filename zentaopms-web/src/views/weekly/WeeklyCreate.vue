<template>
  <div>
    <div class="page-header">
      <h1>{{ weeklyLang.create }}</h1>
      <router-link to="/weekly" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ weeklyLang.project }} *</label>
          <select v-model.number="form.project" required>
            <option :value="0">{{ commonLang.pleaseSelect }}</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ weeklyLang.weekStart }} *</label>
          <input v-model="form.weekStart" type="date" required />
        </div>
        <div class="form-group">
          <label>PV</label>
          <input v-model.number="form.pv" type="number" step="0.01" />
        </div>
        <div class="form-group">
          <label>EV</label>
          <input v-model.number="form.ev" type="number" step="0.01" />
        </div>
        <div class="form-group">
          <label>AC</label>
          <input v-model.number="form.ac" type="number" step="0.01" />
        </div>
        <div class="form-group">
          <label>{{ weeklyLang.progress }}</label>
          <input v-model="form.progress" />
        </div>
        <div class="form-group">
          <label>{{ weeklyLang.workload }}</label>
          <input v-model="form.workload" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/weekly" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { createWeekly } from '@/api/weekly'
import { getProjectAll } from '@/api/project'
import { weekly as weeklyLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({
  project: 0,
  weekStart: '',
  pv: 0,
  ev: 0,
  ac: 0,
  sv: 0,
  cv: 0,
  progress: '',
  workload: '',
})
const submitting = ref(false)
const projects = ref([])
const errorMsg = ref('')

onMounted(async () => {
  const res = await getProjectAll()
  projects.value = res?.data ?? res ?? []
})

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createWeekly(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/weekly/${id}`)
    else router.push('/weekly')
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
.form-group input, .form-group select { width: 100%; max-width: 320px; }
.form-actions { margin-top: 1rem; display: flex; gap: 8px; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
