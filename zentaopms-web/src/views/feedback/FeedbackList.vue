<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ commonLang.feedback }}</h1>
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
          <tr v-for="f in list" :key="f.id">
            <td>{{ f.id }}</td>
            <td>{{ f.title || '-' }}</td>
            <td>{{ f.status || '-' }}</td>
            <td>
              <button type="button" class="btn" @click="onDelete(f.id)">{{ commonLang.delete }}</button>
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
import { getFeedbackList, deleteFeedback } from '@/api/feedback'
import { common as commonLang, doc as docLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const errorMsg = ref('')

async function load() {
  loading.value = true
  try {
    const res = await getFeedbackList()
    list.value = (res.data || [])
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm(commonLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await deleteFeedback(id)
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
