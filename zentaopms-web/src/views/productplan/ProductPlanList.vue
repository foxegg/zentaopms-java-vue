<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">产品计划</h1>
      <router-link v-if="productId" :to="`/productplan/create?product=${productId}`" class="btn btn-primary">新建计划</router-link>
    </div>
    <div class="filter mb-2">
      <label>产品</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">请选择产品</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading && productId">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>状态</th>
            <th>开始/结束</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in list" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.name }}</td>
            <td>{{ p.status }}</td>
            <td>{{ p.begin || '-' }} / {{ p.end || '-' }}</td>
            <td>
              <router-link :to="`/productplan/${p.id}`" class="btn">查看</router-link>
              <router-link :to="`/productplan/edit/${p.id}`" class="btn">编辑</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">暂无计划</p>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else-if="!productId">请选择产品</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductPlanList } from '@/api/productplan'
import { getProductList } from '@/api/product'

const route = useRoute()
const list = ref([])
const loading = ref(true)
const productId = ref(0)
const products = ref([])

async function load() {
  if (!productId.value) {
    list.value = []
    loading.value = false
    return
  }
  loading.value = true
  try {
    const res = await getProductPlanList(productId.value)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const q = route.query.product
  if (q) productId.value = Number(q) || 0
  const r = await getProductList()
  products.value = r.data || []
  load()
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.filter select { margin-left: 8px; }
</style>
