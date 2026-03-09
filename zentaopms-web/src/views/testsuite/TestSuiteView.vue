<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">测试套件详情</h1>
      <router-link to="/testsuite" class="btn">返回列表</router-link>
      <router-link v-if="suite" :to="`/testsuite/edit/${suite.id}`" class="btn">编辑</router-link>
    </div>
    <div v-if="suite" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ suite.id }}</td></tr>
        <tr><th>名称</th><td>{{ suite.name }}</td></tr>
      </table>
      <h3>用例列表</h3>
      <table class="data-table" v-if="cases.length">
        <thead>
          <tr><th>ID</th><th>标题</th><th>操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="c in cases" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.title }}</td>
            <td><router-link :to="`/testcase/${c.id}`" class="btn">查看</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无用例</p>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>套件不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTestSuiteById, getSuiteCases } from '@/api/testsuite'

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
