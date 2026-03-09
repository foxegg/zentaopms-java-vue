<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">测试报告详情</h1>
      <router-link to="/testreport" class="btn">返回列表</router-link>
      <router-link v-if="report" :to="`/testreport/edit/${report.id}`" class="btn">编辑</router-link>
    </div>
    <div v-if="report" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ report.id }}</td></tr>
        <tr><th>名称</th><td>{{ report.name || report.title }}</td></tr>
        <tr><th>项目</th><td>{{ report.projectName || report.project }}</td></tr>
        <tr v-for="(v, k) in report" :key="k" v-show="!['id','name','title','project','projectName'].includes(k)">
          <th>{{ k }}</th><td>{{ v }}</td>
        </tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>报告不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTestReportById } from '@/api/testreport'

const route = useRoute()
const report = ref(null)
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  try {
    const res = await getTestReportById(id)
    report.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
