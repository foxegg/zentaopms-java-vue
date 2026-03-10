<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ chartLang.common }}</h1>
      <router-link to="/chart/create" class="btn btn-primary">{{ chartLang.create }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ projectLang.type }}</th>
            <th>{{ commonLang.status }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in list" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.name }}</td>
            <td>{{ c.type || '-' }}</td>
            <td>{{ c.stage || 'draft' }}</td>
            <td>
              <router-link :to="`/chart/${c.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/chart/edit/${c.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
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
import { getChartList } from '@/api/chart'
import { common as commonLang, chart as chartLang, project as projectLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getChartList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
