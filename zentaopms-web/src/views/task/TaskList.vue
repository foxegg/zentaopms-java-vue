<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">任务列表</h1>
      <router-link to="/task/create" class="btn btn-primary">新建任务</router-link>
    </div>
    <div class="filter mb-2">
      <label>项目</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">全部</option>
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
            <th>执行</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in list" :key="t.id">
            <td>{{ t.id }}</td>
            <td>{{ t.name }}</td>
            <td>{{ t.status }}</td>
            <td>{{ t.executionName || t.execution }}</td>
            <td>
              <router-link :to="`/task/${t.id}`" class="btn">查看</router-link>
              <router-link :to="`/task/edit/${t.id}`" class="btn">编辑</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pager" v-if="pager">
        <span>共 {{ pager.recTotal }} 条</span>
        <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">上一页</button>
        <span>第 {{ pageID }} 页</span>
        <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">下一页</button>
      </div>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTaskList } from '@/api/task'
import { getProjectAll } from '@/api/project'

const route = useRoute()

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const projectId = ref(0)
const projects = ref([])
const pageID = ref(1)
const recPerPage = 20

async function load(page) {
  if (page) pageID.value = page
  loading.value = true
  try {
    const params = { pageID: pageID.value, recPerPage }
    if (projectId.value > 0) params.project = projectId.value
    const res = await getTaskList(params)
    list.value = res.data || []
    pager.value = res.pager || {}
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
