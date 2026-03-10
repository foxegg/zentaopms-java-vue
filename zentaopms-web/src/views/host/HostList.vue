<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ hostLang.common }}</h1>
      <router-link to="/host/create" class="btn btn-primary">{{ commonLang.create }}</router-link>
    </div>
    <p v-if="errorMsg" class="text-danger mb-2">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ hostLang.name }}</th>
            <th>{{ hostLang.type }}</th>
            <th>{{ hostLang.status }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="h in list" :key="h.id">
            <td>{{ h.id }}</td>
            <td><router-link :to="`/host/${h.id}`">{{ h.name || '-' }}</router-link></td>
            <td>{{ h.type || '-' }}</td>
            <td>{{ h.status || '-' }}</td>
            <td>
              <router-link :to="`/host/edit/${h.id}`" class="btn">{{ commonLang.edit }}</router-link>
              <button type="button" class="btn" @click="onDelete(h.id)">{{ commonLang.delete }}</button>
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
import { getHostList, deleteHost } from '@/api/host'
import { host as hostLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const errorMsg = ref('')

async function load() {
  loading.value = true
  try {
    const res = await getHostList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm(commonLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await deleteHost(id)
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
