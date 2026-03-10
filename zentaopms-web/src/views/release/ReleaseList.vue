<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ releaseLang.browse }}</h1>
      <router-link to="/release/create" class="btn btn-primary">{{ releaseLang.create }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ releaseLang.product }}</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">{{ releaseLang.selectProduct }}</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ releaseLang.id }}</th>
            <th>{{ releaseLang.name }}</th>
            <th>{{ releaseLang.date }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in list" :key="r.id">
            <td>{{ r.id }}</td>
            <td>{{ r.name }}</td>
            <td>{{ r.date }}</td>
            <td>
              <router-link :to="`/release/${r.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/release/edit/${r.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getReleaseList } from '@/api/release'
import { getProductList } from '@/api/product'
import { release as releaseLang, common as commonLang } from '@/lang/zh-cn'

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
