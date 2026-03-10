<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ projectLang.view }}</h1>
      <router-link to="/project" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="project" class="table-wrap">
      <div class="sub-nav mb-2">
        <router-link :to="`/project/edit/${project.id}`" class="btn">{{ commonLang.edit }}</router-link>
        <router-link :to="`/project/${project.id}/team`" class="btn">{{ projectLang.team }}</router-link>
        <router-link :to="`/project/${project.id}/products`" class="btn">{{ projectLang.products }}</router-link>
      </div>
      <table class="data-table">
        <tr><th>{{ projectLang.id }}</th><td>{{ project.id }}</td></tr>
        <tr><th>{{ projectLang.name }}</th><td>{{ project.name }}</td></tr>
        <tr><th>{{ projectLang.code }}</th><td>{{ project.code }}</td></tr>
        <tr><th>{{ projectLang.status }}</th><td>{{ project.status }}</td></tr>
      </table>
      <div class="sub-nav mb-2">
        <router-link :to="`/task?project=${project.id}`" class="btn">{{ taskLang.common }}</router-link>
        <router-link :to="`/bug?project=${project.id}`" class="btn">{{ bugLang.common }}</router-link>
        <router-link :to="`/build?project=${project.id}`" class="btn">{{ buildLang.common }}</router-link>
      </div>
      <h3>{{ executionLang.list }}</h3>
      <table class="data-table" v-if="executions.length">
        <thead>
          <tr><th>{{ executionLang.id }}</th><th>{{ executionLang.name }}</th><th>{{ executionLang.code }}</th><th>{{ executionLang.status }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="e in executions" :key="e.id">
            <td>{{ e.id }}</td>
            <td>{{ e.name }}</td>
            <td>{{ e.code }}</td>
            <td>{{ e.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ executionLang.noExecution }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getProjectById } from '@/api/project'
import { getExecutionList } from '@/api/execution'
import { project as projectLang, execution as executionLang, common as commonLang, task as taskLang, bug as bugLang, build as buildLang } from '@/lang/zh-cn'

const route = useRoute()
const project = ref(null)
const executions = ref([])
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getProjectById(id)
    project.value = res.data || null
    if (project.value) {
      const execRes = await getExecutionList(id)
      executions.value = execRes.data || []
    }
  } finally {
    loading.value = false
  }
})
</script>
<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.sub-nav { display: flex; gap: 8px; flex-wrap: wrap; }
</style>
