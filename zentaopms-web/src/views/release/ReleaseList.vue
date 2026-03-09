<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">发布列表</h1>
      <router-link to="/release/create" class="btn btn-primary">新建版本</router-link>
    </div>
    <div class="filter mb-2">
      <label>产品</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">请选择产品</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in list" :key="r.id">
            <td>{{ r.id }}</td>
            <td>{{ r.name }}</td>
            <td>{{ r.date }}</td>
            <td>
            <router-link :to="`/release/${r.id}`" class="btn">查看</router-link>
            <router-link :to="`/release/edit/${r.id}`" class="btn">编辑</router-link>
          </td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getReleaseList } from '@/api/release'
import { getProductList } from '@/api/product'

const list = ref([])
const loading = ref(true)
const productId = ref(0)
const products = ref([])

async function load() {
  if (!productId.value) {
    list.value = []
    return
  }
  loading.value = true
  try {
    const res = await getReleaseList(productId.value)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const r = await getProductList()
  products.value = r.data || []
})
watch(productId, load)
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.filter select { margin-left: 8px; }
</style>
