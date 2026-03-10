<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ buildLang.browse }}</h1>
      <router-link to="/build/create" class="btn btn-primary">{{ buildLang.create }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ buildLang.project }}</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">{{ buildLang.selectProject }}</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ buildLang.id }}</th>
            <th>{{ buildLang.name }}</th>
            <th>{{ buildLang.date }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="b in list" :key="b.id">
            <td>{{ b.id }}</td>
            <td>{{ b.name }}</td>
            <td>{{ b.date }}</td>
            <td>
              <router-link :to="`/build/${b.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/build/edit/${b.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getBuildByProject } from '@/api/build'
import { getProjectAll } from '@/api/project'
import { build as buildLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()

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
    const res = await getBuildByProject(projectId.value)
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
  if (projectId.value) load()
})
watch(projectId, load)
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.filter select { margin-left: 8px; }
</style>
