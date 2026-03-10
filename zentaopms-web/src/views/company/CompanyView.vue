<template>
  <div>
    <div class="page-header">
      <h1>{{ companyLang.viewDetail }}</h1>
      <router-link to="/company" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ item.name }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getCompanyById } from '@/api/company'
import { common as commonLang, company as companyLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getCompanyById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
