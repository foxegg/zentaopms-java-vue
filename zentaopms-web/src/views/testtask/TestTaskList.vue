<template>
  <div>
    <div class="page-header">
      <h1>{{ testtaskLang.list }}</h1>
    </div>
    <div class="filter mb-2">
      <label>{{ projectLang.common }}</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">{{ projectLang.selectProject }}</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
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
          <tr v-for="t in list" :key="t.id">
            <td>{{ t.id }}</td>
            <td>{{ t.name }}</td>
            <td>{{ t.status }}</td>
            <td><router-link :to="`/testtask/${t.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getTestTaskList } from '@/api/testtask'
import { getProjectAll } from '@/api/project'
import { common as commonLang, project as projectLang, testtask as testtaskLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const projectId = ref(0)
const projects = ref([])

async function load() {
  if (!projectId.value) {
    list.value = []
    return
  }
  loading.value = true
  try {
    const res = await getTestTaskList({ project: projectId.value })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const r = await getProjectAll()
  projects.value = r.data || []
})
watch(projectId, load)
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.filter select { margin-left: 8px; }
</style>
