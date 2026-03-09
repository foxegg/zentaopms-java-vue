<template>
  <div>
    <div class="page-header">
      <h1>新建任务</h1>
      <router-link to="/task" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>项目 *</label>
          <select v-model.number="form.project" required @change="onProjectChange">
            <option :value="0">请选择</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>执行 *</label>
          <select v-model.number="form.execution" required>
            <option :value="0">请选择</option>
            <option v-for="e in executions" :key="e.id" :value="e.id">{{ e.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>预估工时</label>
          <input v-model="form.estimate" type="number" step="0.5" min="0" />
        </div>
        <div class="form-group">
          <label>截止日期</label>
          <input v-model="form.deadline" type="date" />
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/task" class="btn">取消</router-link>
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

const router = useRouter()
const route = useRoute()
const form = ref({ project: 0, execution: 0, name: '', estimate: '', deadline: '', description: '' })
const projects = ref([])
const executions = ref([])
const submitting = ref(false)

async function onProjectChange() {
  form.value.execution = 0
  if (!form.value.project) {
    executions.value = []
    return
  }
  const res = await getExecutionList(form.value.project)
  executions.value = res.data || []
}

async function onSubmit() {
  if (!form.value.project || !form.value.execution) return
  submitting.value = true
  try {
    const payload = {
      project: form.value.project,
      execution: form.value.execution,
      name: form.value.name,
      description: form.value.description || undefined
    }
    if (form.value.estimate !== '' && form.value.estimate != null) payload.estimate = Number(form.value.estimate)
    if (form.value.deadline) payload.deadline = form.value.deadline
    const res = await createTask(payload)
    router.push(`/task/${res.id}`)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProjectAll()
  projects.value = r.data || []
  const q = route.query.project || route.query.execution
  if (q) form.value.project = Number(q) || 0
  if (form.value.project) await onProjectChange()
  if (route.query.execution) form.value.execution = Number(route.query.execution) || 0
})
</script>
