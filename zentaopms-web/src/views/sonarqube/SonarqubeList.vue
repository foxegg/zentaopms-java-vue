<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">SonarQube</h1>
      <router-link to="/sonarqube/create" class="btn btn-primary">{{ commonLang.create }}</router-link>
    </div>
    <p v-if="errorMsg" class="text-danger mb-2">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>URL</th>
            <th>{{ commonLang.account }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td><router-link :to="`/sonarqube/${s.id}`">{{ s.name || '-' }}</router-link></td>
            <td>{{ s.url || '-' }}</td>
            <td>{{ s.account || '-' }}</td>
            <td>
              <router-link :to="`/sonarqube/edit/${s.id}`" class="btn">{{ commonLang.edit }}</router-link>
              <button type="button" class="btn" @click="onDelete(s.id)">{{ commonLang.delete }}</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0" class="text-muted">{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSonarqubeList, deleteSonarqube } from '@/api/sonarqube'
import { common as commonLang } from '@/lang/zh-cn'

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
  if (!confirm(commonLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await deleteSonarqube(id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  }
}

onMounted(() => load())
</script>
