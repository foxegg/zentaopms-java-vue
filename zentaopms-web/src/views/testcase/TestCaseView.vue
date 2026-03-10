<template>
  <div>
    <div class="page-header">
      <h1>{{ testcaseLang.viewDetail }}</h1>
      <router-link to="/testcase" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>{{ storyLang.title }}</th><td>{{ item.title }}</td></tr>
        <tr><th>{{ testcaseLang.steps }}</th><td><pre>{{ item.steps }}</pre></td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTestCaseById } from '@/api/testcase'
import { common as commonLang, story as storyLang, testcase as testcaseLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getTestCaseById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
