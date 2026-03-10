<template>
  <div>
    <div class="page-header">
      <h1>{{ stakeholderLang.common }}</h1>
    </div>
    <div class="filter mb-2">
      <label>{{ stakeholderLang.relatedObjectType }}</label>
      <select v-model="objectType" @change="load">
        <option value="project">{{ stakeholderLang.project }}</option>
        <option value="program">{{ stakeholderLang.program }}</option>
      </select>
      <label class="ml-2">{{ stakeholderLang.selectProjectProgram }}</label>
      <select v-model.number="objectId" @change="load">
        <option :value="0">{{ commonLang.pleaseSelect }}</option>
        <option v-for="p in (objectType === 'project' ? projects : programs)" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading && objectId > 0">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ stakeholderLang.user }}</th>
            <th>{{ userLang.type }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.user }}</td>
            <td>{{ s.type }}</td>
            <td><router-link :to="`/stakeholder/${s.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ commonLang.noData }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else-if="objectId <= 0">{{ programLang.selectFirst }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStakeholderList } from '@/api/stakeholder'
import { getProjectAll } from '@/api/project'
import { getProgramList } from '@/api/program'
import { common as commonLang, program as programLang, stakeholder as stakeholderLang, user as userLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const objectType = ref('project')
const objectId = ref(0)
const projects = ref([])
const programs = ref([])

async function load() {
  if (objectId.value <= 0) {
    list.value = []
    return
  }
  loading.value = true
  try {
    const res = await getStakeholderList(objectId.value, objectType.value)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const [pr, prog] = await Promise.all([getProjectAll(), getProgramList()])
  projects.value = pr.data || []
  programs.value = prog.data || []
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.ml-2 { margin-left: 8px; }
.filter select { margin-left: 4px; }
</style>
