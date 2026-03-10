<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">Jenkins</h1>
      <router-link to="/jenkins/create" class="btn btn-primary">{{ commonLang.create }}</router-link>
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
          <tr v-for="j in list" :key="j.id">
            <td>{{ j.id }}</td>
            <td><router-link :to="`/jenkins/${j.id}`">{{ j.name || '-' }}</router-link></td>
            <td>{{ j.url || '-' }}</td>
            <td>{{ j.account || '-' }}</td>
            <td>
              <router-link :to="`/jenkins/edit/${j.id}`" class="btn">{{ commonLang.edit }}</router-link>
              <button type="button" class="btn" @click="onDelete(j.id)">{{ commonLang.delete }}</button>
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
import { getJenkinsList, deleteJenkins } from '@/api/jenkins'
import { common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const errorMsg = ref('')

async function load() {
  loading.value = true
  try {
    const res = await getJenkinsList()
    list.value = (res.data || [])
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm(commonLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await deleteJenkins(id)
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

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
</style>
