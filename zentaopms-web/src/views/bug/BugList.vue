<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">Bug 列表</h1>
      <router-link to="/bug/create" class="btn btn-primary">新建 Bug</router-link>
    </div>
    <div class="filter mb-2">
      <label>产品</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">全部</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
      <label class="ml-2">项目</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">全部</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>标题</th>
            <th>状态</th>
            <th>指派</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="b in list" :key="b.id">
            <td>{{ b.id }}</td>
            <td>{{ b.title }}</td>
            <td>{{ b.status }}</td>
            <td>{{ b.assignedTo }}</td>
            <td>
            <router-link :to="`/bug/${b.id}`" class="btn">查看</router-link>
            <router-link :to="`/bug/edit/${b.id}`" class="btn">编辑</router-link>
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
import { useRoute } from 'vue-router'
import { getBugList } from '@/api/bug'
import { getProductList } from '@/api/product'
import { getProjectAll } from '@/api/project'

const route = useRoute()

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const productId = ref(0)
const projectId = ref(0)
const products = ref([])
const projects = ref([])
const pageID = ref(1)
const recPerPage = 20

async function load(page) {
  if (page) pageID.value = page
  loading.value = true
  try {
    const params = { pageID: pageID.value, recPerPage }
    if (productId.value > 0) params.product = productId.value
    if (projectId.value > 0) params.project = projectId.value
    const res = await getBugList(params)
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const q = route.query.project
  if (q) projectId.value = Number(q) || 0
  const [pr, proj] = await Promise.all([getProductList(), getProjectAll()])
  products.value = pr.data || []
  projects.value = proj.data || []
  load()
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.ml-2 { margin-left: 8px; }
.filter select { margin-left: 4px; }
</style>
