<template>
  <div>
    <div class="page-header">
      <h1>权限组</h1>
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
          <tr v-for="g in list" :key="g.id">
            <td>{{ g.id }}</td>
            <td>{{ g.name }}</td>
            <td><router-link :to="`/group/${g.id}`" class="btn">查看</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getGroupList } from '@/api/group'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getGroupList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
