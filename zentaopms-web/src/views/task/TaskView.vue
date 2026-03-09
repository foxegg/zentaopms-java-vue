<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">任务详情</h1>
      <router-link to="/task" class="btn">返回列表</router-link>
      <router-link v-if="item" :to="`/task/edit/${item.id}`" class="btn">编辑</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>名称</th><td>{{ item.name }}</td></tr>
        <tr><th>状态</th><td>{{ item.status }}</td></tr>
        <tr><th>执行</th><td>{{ item.execution }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTaskById } from '@/api/task'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getTaskById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
