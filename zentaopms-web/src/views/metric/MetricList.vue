<template>
  <div>
    <div class="page-header">
      <h1>统计</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in list" :key="m.id">
            <td>{{ m.id }}</td>
            <td>{{ m.name }}</td>
            <td><router-link :to="`/metric/${m.id}`" class="btn">查看</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">暂无统计项</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMetricList } from '@/api/metric'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getMetricList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
