<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.backupTitle }}</h1>
      <router-link to="/admin" class="btn">{{ commonLang.backAdmin }}</router-link>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <div class="mb-2">
        <button class="btn btn-primary" :disabled="backing" @click="runBackup">{{ adminLang.runBackup }}</button>
      </div>
      <div v-if="diskSpace" class="mb-2">
        <span>磁盘空间：空闲 {{ diskSpace.free }} / 总计 {{ diskSpace.total }}</span>
      </div>
      <table class="data-table" v-if="list.length">
        <thead>
          <tr>
            <th>{{ adminLang.backupFile }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="f in list" :key="f">
            <td>{{ f }}</td>
            <td>
              <button class="btn" @click="restore(f)">{{ adminLang.restore }}</button>
              <button class="btn" @click="del(f)">{{ commonLang.delete }}</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getBackupList, runBackup as apiBackup, restoreBackup, deleteBackup, getBackupDiskSpace } from '@/api/backup'
import { common as commonLang, admin as adminLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const backing = ref(false)
const diskSpace = ref(null)

async function load() {
  loading.value = true
  try {
    const [r1, r2] = await Promise.all([getBackupList(), getBackupDiskSpace()])
    list.value = r1.data || []
    diskSpace.value = r2.data || null
  } finally {
    loading.value = false
  }
}

async function runBackup() {
  backing.value = true
  try {
    await apiBackup()
    load()
  } finally {
    backing.value = false
  }
}

async function restore(fileName) {
  if (!confirm(commonLang.confirmRestore)) return
  errorMsg.value = ''
  try {
    const res = await restoreBackup({ fileName })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || e.message || commonLang.operateFail
  }
}

async function del(fileName) {
  if (!confirm(commonLang.confirmDeleteBackup)) return
  errorMsg.value = ''
  try {
    const res = await deleteBackup(fileName)
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
.mb-2 { margin-bottom: 0.5rem; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
