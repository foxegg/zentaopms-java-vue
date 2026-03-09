<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">节假日</h1>
      <router-link to="/holiday/create" class="btn btn-primary">新建</router-link>
    </div>
    <div class="filter mb-2">
      <label>年份</label>
      <select v-model="currentYear" @change="load">
        <option v-for="y in yearList" :key="y" :value="y">{{ y }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>类型</th>
            <th>开始</th>
            <th>结束</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="h in list" :key="h.id">
            <td>{{ h.id }}</td>
            <td>{{ h.name }}</td>
            <td>{{ h.type === 'working' ? '调休' : '节假日' }}</td>
            <td>{{ h.begin }}</td>
            <td>{{ h.end }}</td>
            <td>{{ h.description || '-' }}</td>
            <td>
              <router-link :to="`/holiday/edit/${h.id}`" class="btn">编辑</router-link>
              <button type="button" class="btn" @click="onDelete(h.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0" class="text-muted">暂无数据</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getHolidayList, deleteHoliday } from '@/api/holiday'

const list = ref([])
const yearList = ref([])
const currentYear = ref(new Date().getFullYear().toString())
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getHolidayList({ year: currentYear.value })
    list.value = res.data || []
    if (res.currentYear) currentYear.value = res.currentYear
    if (res.yearList && res.yearList.length) {
      yearList.value = res.yearList
    } else if (!yearList.value.includes(currentYear.value)) {
      yearList.value = [currentYear.value].concat(yearList.value)
    }
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm('确定删除？')) return
  await deleteHoliday(id)
  load()
}

onMounted(() => load())
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.text-muted { color: #666; }
.filter select { margin-left: 8px; }
</style>
