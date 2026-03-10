<template>
  <div>
    <div class="page-header">
      <h1>{{ weeklyLang.edit }}</h1>
      <router-link :to="`/weekly/${id}`" class="btn">{{ weeklyLang.backView }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
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
          <router-link :to="`/weekly/${id}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getWeeklyById, updateWeekly } from '@/api/weekly'
import { getProjectAll } from '@/api/project'
import { weekly as weeklyLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref({
  project: 0,
  weekStart: '',
  pv: 0,
  ev: 0,
  ac: 0,
  progress: '',
  workload: '',
})
const loading = ref(true)
const submitting = ref(false)
const projects = ref([])
const errorMsg = ref('')

onMounted(async () => {
  const [proj, res] = await Promise.all([getProjectAll(), getWeeklyById(id.value)])
  projects.value = proj?.data ?? proj ?? []
  const d = res?.data ?? res
  if (d) {
    form.value = {
      project: d.project || 0,
      weekStart: d.weekStart || '',
      pv: d.pv ?? 0,
      ev: d.ev ?? 0,
      ac: d.ac ?? 0,
      progress: d.progress || '',
      workload: d.workload || '',
    }
  }
  loading.value = false
})

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateWeekly(id.value, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/weekly/${id.value}`)
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
