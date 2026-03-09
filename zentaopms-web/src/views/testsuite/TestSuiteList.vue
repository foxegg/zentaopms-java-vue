<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">测试套件</h1>
      <router-link to="/testsuite/create" class="btn btn-primary">新建套件</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td>
              <router-link :to="`/testsuite/${s.id}`" class="btn">查看</router-link>
              <router-link :to="`/testsuite/edit/${s.id}`" class="btn">编辑</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">暂无测试套件</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTestSuiteList } from '@/api/testsuite'

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
