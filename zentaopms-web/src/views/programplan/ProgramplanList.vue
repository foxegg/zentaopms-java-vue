<template>
  <div>
    <div class="page-header">
      <h1>{{ programplanLang.title }}</h1>
    </div>
    <div class="filter mb-2">
      <label>{{ projectLang.common }}</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">{{ projectLang.selectProject }}</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading && projectId > 0">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ commonLang.status }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="e in list" :key="e.id">
            <td>{{ e.id }}</td>
            <td>{{ e.name }}</td>
            <td>{{ e.status }}</td>
            <td><router-link :to="`/programplan/${e.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ programplanLang.emptyTip }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else-if="projectId <= 0">{{ projectLang.selectProject }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProgramplanList } from '@/api/programplan'
import { getProjectAll } from '@/api/project'
import { common as commonLang, project as projectLang, programplan as programplanLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const projectId = ref(0)
const projects = ref([])

async function load() {
  if (projectId.value <= 0) {
    list.value = []
    return
  }
  loading.value = true
  try {
    const res = await getProgramplanList({ projectID: projectId.value })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const res = await getProjectAll()
  projects.value = res.data || []
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.filter select { margin-left: 4px; }
</style>
