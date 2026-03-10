<template>
  <div>
    <div class="page-header">
      <h1>{{ dataviewLang.common }}</h1>
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
          <tr v-for="d in list" :key="d.id">
            <td>{{ d.id }}</td>
            <td>{{ d.name }}</td>
            <td><router-link :to="`/dataview/${d.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDataviewList } from '@/api/dataview'
import { common as commonLang, dataview as dataviewLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getDataviewList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
