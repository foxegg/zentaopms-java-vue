<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">代码库</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>路径</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in list" :key="r.id">
            <td>{{ r.id }}</td>
            <td>{{ r.name || r.path }}</td>
            <td>{{ r.path || '-' }}</td>
            <td><router-link :to="`/repo/${r.id}`" class="btn">查看</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-if="!list.length">暂无代码库</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRepoList } from '@/api/repo'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getRepoList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
