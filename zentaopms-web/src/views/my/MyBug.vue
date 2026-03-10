<template>
  <div>
    <div class="page-header"><h1>{{ myLang.bug }}</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ bugLang.title }}</th>
            <th>{{ bugLang.status }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="b in list" :key="b.id">
            <td>{{ b.id }}</td>
            <td>{{ b.title }}</td>
            <td>{{ b.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ myLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyBug } from '@/api/my'
import { my as myLang, bug as bugLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyBug()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
