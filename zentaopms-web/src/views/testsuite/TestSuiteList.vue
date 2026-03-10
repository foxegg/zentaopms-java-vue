<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ testsuiteLang.common }}</h1>
      <router-link to="/testsuite/create" class="btn btn-primary">{{ testsuiteLang.create }}</router-link>
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
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td>
              <router-link :to="`/testsuite/${s.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/testsuite/edit/${s.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ testsuiteLang.emptyTip }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTestSuiteList } from '@/api/testsuite'
import { common as commonLang, testsuite as testsuiteLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getTestSuiteList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
