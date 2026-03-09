<template>
  <div>
    <div class="page-header">
      <h1>树模块</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <p v-if="!list.length">暂无数据（可传 productID/projectID 等参数）</p>
      <table v-else class="data-table">
        <thead>
          <tr><th>ID</th><th>名称</th><th>父级</th><th>操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="n in list" :key="n.id">
            <td>{{ n.id }}</td>
            <td>{{ n.name }}</td>
            <td>{{ n.parent || '-' }}</td>
            <td><router-link :to="`/tree/edit/${n.id}`" class="btn">编辑</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTreeBrowse } from '@/api/tree'

const route = useRoute()
const list = ref([])
const loading = ref(true)

onMounted(async () => {
  loading.value = true
  try {
    const params = { ...route.query }
    const res = await getTreeBrowse(params)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
