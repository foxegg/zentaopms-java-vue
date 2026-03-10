<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ executionLang.list }}</h1>
      <router-link v-if="projectId" :to="`/execution/create?project=${projectId}`" class="btn btn-primary">{{ executionLang.create }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ executionLang.project }}</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">{{ executionLang.selectProject }}</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading && projectId">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ executionLang.id }}</th>
            <th>{{ executionLang.name }}</th>
            <th>{{ executionLang.code }}</th>
            <th>{{ executionLang.status }}</th>
            <th>{{ executionLang.dateRange }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="e in list" :key="e.id">
            <td>{{ e.id }}</td>
            <td>{{ e.name }}</td>
            <td>{{ e.code }}</td>
            <td>{{ e.status }}</td>
            <td>{{ e.begin || '-' }} / {{ e.end || '-' }}</td>
            <td>
              <router-link :to="`/execution/${e.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/execution/edit/${e.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ executionLang.noExecution }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else-if="!projectId">{{ executionLang.selectProject }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getExecutionList } from '@/api/execution'
import { getProjectAll } from '@/api/project'
import { execution as executionLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const list = ref([])
const loading = ref(true)
const projectId = ref(0)
const projects = ref([])

async function load() {
  if (!projectId.value) {
    list.value = []
    loading.value = false
    return
  }
  loading.value = true
  try {
    const res = await getExecutionList(projectId.value)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const q = route.query.project
  if (q) projectId.value = Number(q) || 0
  const r = await getProjectAll()
  projects.value = r.data || []
  load()
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.filter select { margin-left: 8px; }
</style>
