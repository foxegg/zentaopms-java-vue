<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">代码库详情</h1>
      <router-link to="/repo" class="btn">返回列表</router-link>
    </div>
    <div v-if="repo" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ repo.id }}</td></tr>
        <tr><th>名称</th><td>{{ repo.name || repo.path }}</td></tr>
        <tr><th>路径</th><td>{{ repo.path || '-' }}</td></tr>
        <tr><th>SCM</th><td>{{ repo.scm || '-' }}</td></tr>
        <tr><th>编码</th><td>{{ repo.encoding || '-' }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>代码库不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getRepoById } from '@/api/repo'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const repo = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getRepoById(id.value)
    repo.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
