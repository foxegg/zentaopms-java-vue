<template>
  <div>
    <div class="page-header">
      <h1>{{ deptLang.common }}</h1>
    </div>
    <div v-if="!loading" class="table-wrap">
      <table class="data-table">
        <thead>
          <tr><th>ID</th><th>{{ commonLang.name }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="d in tree" :key="d.id">
            <td>{{ d.id }}</td>
            <td>{{ d.name }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDeptTree } from '@/api/dept'
import { common as commonLang, dept as deptLang } from '@/lang/zh-cn'

const tree = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getDeptTree()
    tree.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>

