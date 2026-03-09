<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">执行详情</h1>
      <router-link :to="`/execution?project=${execution?.project}`" class="btn">返回列表</router-link>
      <router-link v-if="execution" :to="`/execution/edit/${execution.id}`" class="btn">编辑</router-link>
      <template v-if="execution">
        <button v-if="execution.status === 'wait'" class="btn btn-primary" @click="doStart">开始</button>
        <button v-else-if="execution.status === 'doing'" class="btn" @click="doSuspend">挂起</button>
        <button v-else-if="['suspended','closed'].includes(execution.status)" class="btn" @click="doActivate">激活</button>
        <button v-else-if="execution.status === 'doing'" class="btn" @click="doClose">关闭</button>
        <button class="btn" @click="doDelete">删除</button>
      </template>
    </div>
    <div v-if="execution" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ execution.id }}</td></tr>
        <tr><th>名称</th><td>{{ execution.name }}</td></tr>
        <tr><th>代号</th><td>{{ execution.code }}</td></tr>
        <tr><th>状态</th><td>{{ execution.status }}</td></tr>
        <tr><th>开始日期</th><td>{{ execution.begin || '-' }}</td></tr>
        <tr><th>结束日期</th><td>{{ execution.end || '-' }}</td></tr>
      </table>
      <div class="sub-nav mb-2">
        <router-link :to="`/execution/${execution.id}/team`" class="btn">团队</router-link>
        <router-link :to="`/execution/${execution.id}/dynamic`" class="btn">动态</router-link>
        <router-link :to="`/story?execution=${execution.id}`" class="btn">需求</router-link>
        <router-link :to="`/task?execution=${execution.id}`" class="btn">任务</router-link>
        <router-link :to="`/bug?execution=${execution.id}`" class="btn">Bug</router-link>
      </div>
      <h3>任务</h3>
      <table class="data-table" v-if="tasks.length">
        <thead>
          <tr><th>ID</th><th>名称</th><th>状态</th></tr>
        </thead>
        <tbody>
          <tr v-for="t in tasks" :key="t.id">
            <td>{{ t.id }}</td>
            <td><router-link :to="`/task/${t.id}`">{{ t.name }}</router-link></td>
            <td>{{ t.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无任务</p>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>执行不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getExecutionById, getExecutionTasks, startExecution, closeExecution, suspendExecution, activateExecution, deleteExecution } from '@/api/execution'

const route = useRoute()
const router = useRouter()
const execution = ref(null)
const tasks = ref([])
const loading = ref(true)

async function load() {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getExecutionById(id)
    execution.value = res.data || null
    if (execution.value) {
      const taskRes = await getExecutionTasks(id)
      tasks.value = taskRes.data || []
    }
  } finally {
    loading.value = false
  }
}

async function doStart() {
  try {
    await startExecution(execution.value.id)
    load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失败')
  }
}
async function doClose() {
  if (!confirm('确定关闭？')) return
  try {
    await closeExecution(execution.value.id)
    load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失败')
  }
}
async function doSuspend() {
  try {
    await suspendExecution(execution.value.id)
    load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失败')
  }
}
async function doActivate() {
  try {
    await activateExecution(execution.value.id)
    load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失败')
  }
}
async function doDelete() {
  if (!confirm('确定删除？')) return
  try {
    await deleteExecution(execution.value.id)
    router.push(`/execution?project=${execution.value.project}`)
  } catch (e) {
    alert(e.response?.data?.message || '操作失败')
  }
}

onMounted(load)
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.sub-nav { display: flex; gap: 8px; flex-wrap: wrap; }
</style>
