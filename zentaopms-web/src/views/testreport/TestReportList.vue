<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">测试报告</h1>
      <router-link to="/testreport/create" class="btn btn-primary">新建报告</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>项目</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in list" :key="r.id">
            <td>{{ r.id }}</td>
            <td>{{ r.name || r.title }}</td>
            <td>{{ r.projectName || r.project || '-' }}</td>
            <td>
              <router-link :to="`/testreport/${r.id}`" class="btn">查看</router-link>
              <router-link :to="`/testreport/edit/${r.id}`" class="btn">编辑</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">暂无测试报告</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTestReportList } from '@/api/testreport'

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
