<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">项目集详情</h1>
      <router-link to="/program" class="btn">返回列表</router-link>
      <router-link v-if="program" :to="`/program/edit/${program.id}`" class="btn">编辑</router-link>
    </div>
    <div v-if="program" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ program.id }}</td></tr>
        <tr><th>名称</th><td>{{ program.name }}</td></tr>
        <tr><th>状态</th><td>{{ program.status }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>项目集不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProgramById } from '@/api/program'

const route = useRoute()
const program = ref(null)
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getProgramById(id)
    program.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
