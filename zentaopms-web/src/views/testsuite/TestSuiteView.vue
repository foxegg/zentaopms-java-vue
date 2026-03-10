<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ testsuiteLang.common }}</h1>
      <router-link to="/testsuite" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="suite" :to="`/testsuite/edit/${suite.id}`" class="btn">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="suite" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ suite.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ suite.name }}</td></tr>
      </table>
      <h3>{{ testsuiteLang.caseList }}</h3>
      <table class="data-table" v-if="cases.length">
        <thead>
          <tr><th>ID</th><th>{{ storyLang.title }}</th><th>{{ commonLang.actions }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="c in cases" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.title }}</td>
            <td><router-link :to="`/testcase/${c.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ testsuiteLang.noCases }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ testsuiteLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTestSuiteById, getSuiteCases } from '@/api/testsuite'
import { common as commonLang, testsuite as testsuiteLang, story as storyLang } from '@/lang/zh-cn'

const route = useRoute()
const suite = ref(null)
const cases = ref([])
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getTestSuiteById(id)
    suite.value = res.data || null
    if (suite.value) {
      const cRes = await getSuiteCases(id)
      cases.value = cRes.data || []
    }
  } finally {
    loading.value = false
  }
})
</script>
