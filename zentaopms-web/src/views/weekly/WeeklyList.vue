<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ weeklyLang.common }}</h1>
      <router-link to="/weekly/create" class="btn btn-primary">{{ weeklyLang.create }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ weeklyLang.project }}</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">{{ executionLang.selectProject }}</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading && projectId > 0">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ weeklyLang.weekStart }}</th>
            <th>PV</th>
            <th>EV</th>
            <th>AC</th>
            <th>{{ weeklyLang.progress }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="w in list" :key="w.id">
            <td>{{ w.id }}</td>
            <td>{{ w.weekStart }}</td>
            <td>{{ w.pv }}</td>
            <td>{{ w.ev }}</td>
            <td>{{ w.ac }}</td>
            <td>{{ w.progress || '-' }}</td>
            <td>
              <router-link :to="`/weekly/${w.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/weekly/edit/${w.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ weeklyLang.emptyTip }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else-if="projectId <= 0">{{ weeklyLang.selectProjectFirst }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getWeeklyList } from '@/api/weekly'
import { getProjectAll } from '@/api/project'
import { common as commonLang, weekly as weeklyLang, execution as executionLang } from '@/lang/zh-cn'

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
    const res = await getWeeklyList(projectId.value)
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
