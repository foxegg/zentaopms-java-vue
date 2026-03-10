<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ productLang.view }}</h1>
      <router-link to="/product" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="product" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ product.id }}</td></tr>
        <tr><th>{{ productLang.name }}</th><td>{{ product.name }}</td></tr>
        <tr><th>{{ productLang.code }}</th><td>{{ product.code }}</td></tr>
        <tr><th>{{ productLang.status }}</th><td>{{ product.status }}</td></tr>
        <tr><th>{{ productLang.type }}</th><td>{{ product.type }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductById } from '@/api/product'
import { product as productLang, common as commonLang } from '@/lang/zh-cn'

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
