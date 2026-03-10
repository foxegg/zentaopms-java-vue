<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ productLang.browse }}</h1>
      <router-link to="/product/create" class="btn btn-primary">{{ productLang.create }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ productLang.name }}</th>
            <th>{{ productLang.code }}</th>
            <th>{{ productLang.status }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in list" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.name }}</td>
            <td>{{ p.code }}</td>
            <td>{{ p.status || '-' }}</td>
            <td>
              <router-link :to="`/product/${p.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/product/edit/${p.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProductList } from '@/api/product'
import { product as productLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getProductList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => load())
</script>
