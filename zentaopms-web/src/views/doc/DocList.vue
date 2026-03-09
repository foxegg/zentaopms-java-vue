<template>
  <div>
    <div class="page-header">
      <h1>文档</h1>
    </div>
    <div class="filter mb-2">
      <label>产品</label>
      <select v-model.number="productId" @change="loadLibs">
        <option :value="0">请选择产品</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
      <template v-if="libs.length">
        <label class="ml-2">文档库</label>
        <select v-model.number="libId" @change="loadDocs">
          <option :value="0">请选择文档库</option>
          <option v-for="l in libs" :key="l.id" :value="l.id">{{ l.name }}</option>
        </select>
      </template>
    </div>
    <div class="table-wrap" v-if="!loading && libId">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>标题</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="d in docs" :key="d.id">
            <td>{{ d.id }}</td>
            <td>{{ d.title }}</td>
            <td><router-link :to="`/doc/${d.id}`" class="btn">查看</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getDocLibsByProduct, getDocList } from '@/api/doc'
import { getProductList } from '@/api/product'

const productId = ref(0)
const libId = ref(0)
const products = ref([])
const libs = ref([])
const docs = ref([])
const loading = ref(false)

async function loadLibs() {
  libId.value = 0
  docs.value = []
  if (!productId.value) {
    libs.value = []
    return
  }
  loading.value = true
  try {
    const res = await getDocLibsByProduct(productId.value)
    libs.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function loadDocs() {
  if (!libId.value) {
    docs.value = []
    return
  }
  loading.value = true
  try {
    const res = await getDocList(libId.value)
    docs.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const r = await getProductList()
  products.value = r.data || []
})
watch(libId, loadDocs)
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.ml-2 { margin-left: 8px; }
.filter select { margin-left: 4px; }
</style>
