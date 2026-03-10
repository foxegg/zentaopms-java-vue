<template>
  <div>
    <div class="page-header">
      <h1>{{ commonLang.testcase }}</h1>
    </div>
    <div class="filter mb-2">
      <label>{{ productLang.common }}</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">{{ productLang.select }}</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ bugLang.title }}</th>
            <th>{{ testcaseLang.module }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in list" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.title }}</td>
            <td>{{ c.module }}</td>
            <td><router-link :to="`/testcase/${c.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
      <div class="pager" v-if="list.length">{{ commonLang.totalCount.replace('{total}', list.length) }}</div>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getTestCaseList } from '@/api/testcase'
import { getProductList } from '@/api/product'
import { common as commonLang, product as productLang, bug as bugLang, testcase as testcaseLang } from '@/lang/zh-cn'

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
