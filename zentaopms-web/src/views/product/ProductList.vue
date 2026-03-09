<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">产品列表</h1>
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
              <router-link :to="`/product/${p.id}`" class="btn">查看</router-link>
              <router-link :to="`/product/edit/${p.id}`" class="btn">编辑</router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProductList } from '@/api/product'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getProductList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(() => load())
</script>
