<template>
  <div>
    <div class="page-header">
      <h1>{{ companyLang.common }}</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in list" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.name }}</td>
            <td><router-link :to="`/company/${c.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCompanyList } from '@/api/company'
import { common as commonLang, company as companyLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getCompanyList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
