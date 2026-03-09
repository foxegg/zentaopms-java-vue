<template>
  <div>
    <div class="page-header">
      <h1>测试单列表</h1>
    </div>
    <div class="filter mb-2">
      <label>项目</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">请选择项目</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in list" :key="t.id">
            <td>{{ t.id }}</td>
            <td>{{ t.name }}</td>
            <td>{{ t.status }}</td>
            <td><router-link :to="`/testtask/${t.id}`" class="btn">查看</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getTestTaskList } from '@/api/testtask'
import { getProjectAll } from '@/api/project'

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
