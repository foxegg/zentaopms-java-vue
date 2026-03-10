<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ branchLang.common }}</h1>
      <router-link v-if="productId" :to="`/branch/create?product=${productId}`" class="btn btn-primary">{{ branchLang.create }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ productLang.common }}</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">{{ productLang.select }}</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading && productId">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="b in list" :key="b.id">
            <td>{{ b.id }}</td>
            <td>{{ b.name }}</td>
            <td>
              <router-link :to="`/branch/${b.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/branch/edit/${b.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ branchLang.emptyTip }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else-if="!productId">{{ productLang.select }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getBranchList } from '@/api/branch'
import { getProductList } from '@/api/product'
import { common as commonLang, product as productLang, branch as branchLang } from '@/lang/zh-cn'

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
    const res = await getBranchList({ productID: productId.value })
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
