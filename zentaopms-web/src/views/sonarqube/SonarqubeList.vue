<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">SonarQube</h1>
      <router-link to="/sonarqube/create" class="btn btn-primary">新建</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>URL</th>
            <th>账号</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td><router-link :to="`/sonarqube/${s.id}`">{{ s.name || '-' }}</router-link></td>
            <td>{{ s.url || '-' }}</td>
            <td>{{ s.account || '-' }}</td>
            <td>
              <router-link :to="`/sonarqube/edit/${s.id}`" class="btn">编辑</router-link>
              <button type="button" class="btn" @click="onDelete(s.id)">删除</button>
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
import { getSonarqubeList, deleteSonarqube } from '@/api/sonarqube'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getSonarqubeList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm('确定删除？')) return
  await deleteSonarqube(id)
  load()
}

onMounted(() => load())
</script>
