<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">产品详情</h1>
      <router-link to="/product" class="btn">返回列表</router-link>
    </div>
    <div v-if="product" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ product.id }}</td></tr>
        <tr><th>名称</th><td>{{ product.name }}</td></tr>
        <tr><th>代号</th><td>{{ product.code }}</td></tr>
        <tr><th>状态</th><td>{{ product.status }}</td></tr>
        <tr><th>类型</th><td>{{ product.type }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>产品不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductById } from '@/api/product'

const route = useRoute()
const product = ref(null)
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getProductById(id)
    product.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
