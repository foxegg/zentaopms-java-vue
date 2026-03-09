<template>
  <div>
    <div class="page-header">
      <h1>搜索</h1>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSearch" class="mb-2">
        <input v-model="keyword" type="text" placeholder="输入关键词" style="margin-right:8px;padding:6px;" />
        <select v-model="module">
          <option value="">全部</option>
          <option value="task">任务</option>
          <option value="bug">Bug</option>
          <option value="story">需求</option>
          <option value="project">项目</option>
          <option value="product">产品</option>
        </select>
        <button type="submit" class="btn btn-primary" style="margin-left:8px;">搜索</button>
      </form>
      <div v-if="result">
        <p v-if="!result.data || result.data.length === 0">无结果</p>
        <table v-else class="data-table">
          <thead>
            <tr><th>类型</th><th>ID</th><th>标题/名称</th><th>操作</th></tr>
          </thead>
          <tbody>
            <tr v-for="r in result.data" :key="r.type + r.id">
              <td>{{ r.type }}</td>
              <td>{{ r.id }}</td>
              <td>{{ r.title || r.name }}</td>
              <td>
                <router-link v-if="r.type === 'task'" :to="`/task/${r.id}`" class="btn">查看</router-link>
                <router-link v-else-if="r.type === 'bug'" :to="`/bug/${r.id}`" class="btn">查看</router-link>
                <router-link v-else-if="r.type === 'story'" :to="`/story/${r.id}`" class="btn">查看</router-link>
                <router-link v-else-if="r.type === 'project'" :to="`/project/${r.id}`" class="btn">查看</router-link>
                <router-link v-else-if="r.type === 'product'" :to="`/product/${r.id}`" class="btn">查看</router-link>
                <span v-else>-</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { searchQuery } from '@/api/search'

const keyword = ref('')
const module = ref('')
const result = ref(null)

async function onSearch() {
  if (!keyword.value.trim()) return
  try {
    const res = await searchQuery({ words: keyword.value, module: module.value })
    result.value = res
  } catch (e) {
    result.value = { data: [] }
  }
}
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
</style>
