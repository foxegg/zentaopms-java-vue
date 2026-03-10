<template>
  <div>
    <div class="page-header">
      <h1>{{ groupLang.viewDetail }}</h1>
      <router-link to="/group" class="btn">{{ commonLang.backList }}</router-link>
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
import { getGroupById } from '@/api/group'
import { common as commonLang, group as groupLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getGroupById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
