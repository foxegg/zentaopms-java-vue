<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ adminLang.cron }}</h1>
      <router-link to="/admin" class="btn">{{ commonLang.backAdmin }}</router-link>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ adminLang.remark }}</th>
            <th>{{ projectLang.type }}</th>
            <th>{{ commonLang.status }}</th>
            <th>{{ adminLang.lastRun }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in list" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.remark || '-' }}</td>
            <td>{{ c.type || '-' }}</td>
            <td>{{ c.status || '-' }}</td>
            <td>{{ c.lastTime || '-' }}</td>
            <td>
              <button class="btn" @click="runOne(c.id)">{{ adminLang.runBtn }}</button>
              <button class="btn" @click="toggleOne(c)">{{ adminLang.toggleBtn }}</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCronList, runCron, toggleCron } from '@/api/cron'
import { common as commonLang, project as projectLang, admin as adminLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const errorMsg = ref('')

async function load() {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await getCronList()
    list.value = res?.data ?? []
  } finally {
    loading.value = false
  }
}

async function runOne(id) {
  errorMsg.value = ''
  try {
    const res = await runCron(id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || e.message || commonLang.operateFail
  }
}

async function toggleOne(c) {
  const next = c.status === 'normal' ? 'stop' : 'normal'
  errorMsg.value = ''
  try {
    const res = await toggleCron(c.id, { status: next })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || e.message || commonLang.operateFail
  }
}

onMounted(load)
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
