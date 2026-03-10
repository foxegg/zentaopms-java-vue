<template>
  <div>
    <div class="page-header">
      <h1>{{ taskLang.create }}</h1>
      <router-link to="/task" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ taskLang.project }} *</label>
          <select v-model.number="form.project" required @change="onProjectChange">
            <option :value="0">{{ taskLang.selectProject }}</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ taskLang.execution }} *</label>
          <select v-model.number="form.execution" required>
            <option :value="0">{{ taskLang.selectExecution }}</option>
            <option v-for="e in executions" :key="e.id" :value="e.id">{{ e.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ taskLang.type }} *</label>
          <select v-model="form.type" required>
            <option value="">{{ commonLang.pleaseSelect }}</option>
            <option v-for="(label, key) in taskLang.typeList" :key="key" :value="key">{{ label }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ taskLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ taskLang.estimate }}</label>
          <input v-model="form.estimate" type="number" step="0.5" min="0" :placeholder="taskLang.estimateLabel" />
        </div>
        <div class="form-group">
          <label>{{ taskLang.deadline }}</label>
          <input v-model="form.deadline" type="date" />
        </div>
        <div class="form-group">
          <label>{{ taskLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/task" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { createTask } from '@/api/task'
import { getProjectAll } from '@/api/project'
import { getExecutionList } from '@/api/execution'
import { task as taskLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const route = useRoute()
const form = ref({ project: 0, execution: 0, type: 'devel', name: '', estimate: '', deadline: '', description: '' })
const projects = ref([])
const executions = ref([])
const submitting = ref(false)
const errorMsg = ref('')

async function onProjectChange() {
  form.value.execution = 0
  if (!form.value.project) {
    executions.value = []
    return
  }
  const res = await getExecutionList(form.value.project)
  executions.value = res?.data ?? res ?? []
}

async function onSubmit() {
  if (!form.value.project || !form.value.execution || !form.value.type || !form.value.name?.trim()) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const payload = {
      project: form.value.project,
      execution: form.value.execution,
      type: form.value.type,
      name: form.value.name,
      description: form.value.description || undefined
    }
    if (form.value.estimate !== '' && form.value.estimate != null) payload.estimate = Number(form.value.estimate)
    if (form.value.deadline) payload.deadline = form.value.deadline
    const res = await createTask(payload)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/task/${id}`)
    else router.push('/task')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProjectAll()
  projects.value = r?.data ?? r ?? []
  const q = route.query.project || route.query.execution
  if (q) form.value.project = Number(q) || 0
  if (form.value.project) await onProjectChange()
  if (route.query.execution) form.value.execution = Number(route.query.execution) || 0
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
