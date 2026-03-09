<template>
  <div>
    <div class="page-header">
      <h1>组织</h1>
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
          <tr v-for="c in list" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.name }}</td>
            <td><router-link :to="`/company/${c.id}`" class="btn">查看</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCompanyList } from '@/api/company'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getCompanyList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
