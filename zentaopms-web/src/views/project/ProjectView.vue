<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">项目详情</h1>
      <router-link to="/project" class="btn">返回列表</router-link>
    </div>
    <div v-if="project" class="table-wrap">
      <div class="sub-nav mb-2">
        <router-link :to="`/project/edit/${project.id}`" class="btn">编辑</router-link>
        <router-link :to="`/project/${project.id}/team`" class="btn">团队</router-link>
        <router-link :to="`/project/${project.id}/products`" class="btn">关联产品</router-link>
      </div>
      <table class="data-table">
        <tr><th>ID</th><td>{{ project.id }}</td></tr>
        <tr><th>名称</th><td>{{ project.name }}</td></tr>
        <tr><th>代号</th><td>{{ project.code }}</td></tr>
        <tr><th>状态</th><td>{{ project.status }}</td></tr>
      </table>
      <div class="sub-nav mb-2">
        <router-link :to="`/task?project=${project.id}`" class="btn">任务</router-link>
        <router-link :to="`/bug?project=${project.id}`" class="btn">Bug</router-link>
        <router-link :to="`/build?project=${project.id}`" class="btn">构建</router-link>
      </div>
      <h3>执行/迭代</h3>
      <table class="data-table" v-if="executions.length">
        <thead>
          <tr><th>ID</th><th>名称</th><th>代号</th><th>状态</th></tr>
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
      <p v-else>暂无执行</p>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>项目不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getProjectById } from '@/api/project'
import { getExecutionList } from '@/api/execution'

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
