<template>
  <div>
    <div class="page-header">
      <h1>{{ docLang.viewDetail }}</h1>
      <router-link to="/doc" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>{{ docLang.title }}</th><td>{{ item.title }}</td></tr>
        <tr v-if="item.content"><th>{{ docLang.content }}</th><td><pre class="doc-content">{{ item.content }}</pre></td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getDocById } from '@/api/doc'
import { common as commonLang, doc as docLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getDocById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.doc-content { white-space: pre-wrap; max-height: 400px; overflow: auto; }
</style>
