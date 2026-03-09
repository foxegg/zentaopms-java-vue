<template>
  <div>
    <div class="page-header">
      <h1>测试用例列表</h1>
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
            <th>标题</th>
            <th>模块</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in list" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.title }}</td>
            <td>{{ c.module }}</td>
            <td><router-link :to="`/testcase/${c.id}`" class="btn">查看</router-link></td>
          </tr>
        </tbody>
      </table>
      <div class="pager" v-if="list.length">共 {{ list.length }} 条</div>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getTestCaseList } from '@/api/testcase'
import { getProductList } from '@/api/product'

const list = ref([])
const pager = ref(null)
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
    const res = await getTestCaseList({ product: productId.value })
    list.value = res.data || []
    pager.value = res.pager || {}
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
