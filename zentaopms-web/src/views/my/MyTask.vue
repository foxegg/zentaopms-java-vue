<template>
  <div>
    <div class="page-header"><h1>{{ myLang.task }}</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ taskLang.id }}</th>
            <th>{{ taskLang.name }}</th>
            <th>{{ taskLang.status }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in list" :key="t.id">
            <td>{{ t.id }}</td>
            <td>{{ t.name }}</td>
            <td>{{ t.status }}</td>
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
import { getMyTask } from '@/api/my'
import { my as myLang, task as taskLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyTask()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
