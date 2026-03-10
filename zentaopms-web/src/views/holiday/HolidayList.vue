<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ holidayLang.common }}</h1>
      <router-link to="/holiday/create" class="btn btn-primary">{{ commonLang.create }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ holidayLang.checkYear }}</label>
      <select v-model="currentYear" @change="load">
        <option v-for="y in yearList" :key="y" :value="y">{{ y }}</option>
      </select>
    </div>
    <p v-if="errorMsg" class="text-danger mb-2">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ holidayLang.name }}</th>
            <th>{{ holidayLang.type }}</th>
            <th>{{ holidayLang.beginShort }}</th>
            <th>{{ holidayLang.endShort }}</th>
            <th>{{ holidayLang.desc }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="h in list" :key="h.id">
            <td>{{ h.id }}</td>
            <td>{{ h.name }}</td>
            <td>{{ holidayLang.typeList[h.type] || h.type }}</td>
            <td>{{ h.begin }}</td>
            <td>{{ h.end }}</td>
            <td>{{ h.description || '-' }}</td>
            <td>
              <router-link :to="`/holiday/edit/${h.id}`" class="btn">{{ commonLang.edit }}</router-link>
              <button type="button" class="btn" @click="onDelete(h.id)">{{ commonLang.delete }}</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0" class="text-muted">{{ holidayLang.emptyTip }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getHolidayList, deleteHoliday } from '@/api/holiday'
import { holiday as holidayLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const yearList = ref([])
const currentYear = ref(new Date().getFullYear().toString())
const loading = ref(true)
const errorMsg = ref('')

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
  if (!confirm(holidayLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await deleteHoliday(id)
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
.text-muted { color: #666; }
.filter select { margin-left: 8px; }
</style>
