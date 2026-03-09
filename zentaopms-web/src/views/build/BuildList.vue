<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">构建列表</h1>
      <router-link to="/build/create" class="btn btn-primary">新建构建</router-link>
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
            <th>日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="b in list" :key="b.id">
            <td>{{ b.id }}</td>
            <td>{{ b.name }}</td>
            <td>{{ b.date }}</td>
            <td>
            <router-link :to="`/build/${b.id}`" class="btn">查看</router-link>
            <router-link :to="`/build/edit/${b.id}`" class="btn">编辑</router-link>
          </td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getBuildByProject } from '@/api/build'
import { getProjectAll } from '@/api/project'

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
