<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">项目列表</h1>
      <router-link to="/project/create" class="btn btn-primary">新建项目</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>代号</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in list" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.name }}</td>
            <td>{{ p.code }}</td>
            <td>{{ p.status || '-' }}</td>
            <td>
              <router-link :to="`/project/${p.id}`" class="btn">查看</router-link>
              <router-link :to="`/project/edit/${p.id}`" class="btn">编辑</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pager" v-if="pager">
        <span>共 {{ pager.recTotal }} 条</span>
        <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">上一页</button>
        <span>第 {{ pageID }} 页</span>
        <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">下一页</button>
      </div>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProjectList } from '@/api/project'

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const pageID = ref(1)
const recPerPage = 20

async function load(page = 1) {
  pageID.value = page
  loading.value = true
  try {
    const res = await getProjectList({ pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

onMounted(() => load())
</script>
