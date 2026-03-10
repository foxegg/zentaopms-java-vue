<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ mrLang.common }}</h1>
    </div>
    <p v-if="errorMsg" class="text-danger mb-2">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ docLang.title }}</th>
            <th>{{ commonLang.status }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in list" :key="m.id">
            <td>{{ m.id }}</td>
            <td>{{ m.title || '-' }}</td>
            <td>{{ m.status || '-' }}</td>
            <td>
              <button type="button" class="btn" @click="onDelete(m.id)">{{ commonLang.delete }}</button>
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
import { getMrList, deleteMr } from '@/api/mr'
import { common as commonLang, doc as docLang, mr as mrLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const errorMsg = ref('')

async function load() {
  loading.value = true
  try {
    const res = await getMrList()
    list.value = (res.data || [])
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm(commonLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await deleteMr(id)
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
