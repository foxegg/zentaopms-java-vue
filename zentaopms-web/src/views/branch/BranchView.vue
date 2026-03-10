<template>
  <div>
    <div class="page-header">
      <h1>{{ branchLang.common }}</h1>
      <router-link to="/branch" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="branch" :to="`/branch/edit/${branch.id}`" class="btn">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="branch" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ branch.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ branch.name }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ branchLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getBranchById } from '@/api/branch'
import { common as commonLang, branch as branchLang } from '@/lang/zh-cn'

const route = useRoute()
const branch = ref(null)
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  try {
    const res = await getBranchById(id)
    branch.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
