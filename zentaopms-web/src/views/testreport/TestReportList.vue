<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ testreportLang.common }}</h1>
      <router-link to="/testreport/create" class="btn btn-primary">{{ testreportLang.create }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ projectLang.common }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in list" :key="r.id">
            <td>{{ r.id }}</td>
            <td>{{ r.name || r.title }}</td>
            <td>{{ r.projectName || r.project || '-' }}</td>
            <td>
              <router-link :to="`/testreport/${r.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/testreport/edit/${r.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ testreportLang.emptyTip }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTestReportList } from '@/api/testreport'
import { common as commonLang, project as projectLang, testreport as testreportLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getTestReportList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
