<template>
  <div>
    <div class="page-header">
      <h1>文档详情</h1>
      <router-link to="/doc" class="btn">返回</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>标题</th><td>{{ item.title }}</td></tr>
        <tr v-if="item.content"><th>内容</th><td><pre class="doc-content">{{ item.content }}</pre></td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getDocById } from '@/api/doc'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getDocById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.doc-content { white-space: pre-wrap; max-height: 400px; overflow: auto; }
</style>
