<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ executionLang.view }}</h1>
      <router-link :to="`/execution?project=${execution?.project}`" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="execution" :to="`/execution/edit/${execution.id}`" class="btn">{{ commonLang.edit }}</router-link>
      <template v-if="execution">
        <button v-if="execution.status === 'wait'" class="btn btn-primary" @click="doStart">{{ executionLang.start }}</button>
        <button v-else-if="execution.status === 'doing'" class="btn" @click="doSuspend">{{ executionLang.suspend }}</button>
        <button v-else-if="['suspended','closed'].includes(execution.status)" class="btn" @click="doActivate">{{ executionLang.activate }}</button>
        <button v-else-if="execution.status === 'doing'" class="btn" @click="doClose">{{ executionLang.close }}</button>
        <button class="btn" @click="doDelete">{{ executionLang.delete }}</button>
      </template>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div v-if="execution" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ execution.id }}</td></tr>
        <tr><th>{{ executionLang.name }}</th><td>{{ execution.name }}</td></tr>
        <tr><th>{{ executionLang.code }}</th><td>{{ execution.code }}</td></tr>
        <tr><th>{{ executionLang.status }}</th><td>{{ execution.status }}</td></tr>
        <tr><th>{{ executionLang.begin }}</th><td>{{ execution.begin || '-' }}</td></tr>
        <tr><th>{{ executionLang.end }}</th><td>{{ execution.end || '-' }}</td></tr>
      </table>
      <div class="sub-nav mb-2">
        <router-link :to="`/execution/${execution.id}/team`" class="btn">{{ executionLang.team }}</router-link>
        <router-link :to="`/execution/${execution.id}/dynamic`" class="btn">{{ executionLang.dynamic }}</router-link>
        <router-link :to="`/story?execution=${execution.id}`" class="btn">{{ storyLang.common }}</router-link>
        <router-link :to="`/task?execution=${execution.id}`" class="btn">{{ taskLang.common }}</router-link>
        <router-link :to="`/bug?execution=${execution.id}`" class="btn">{{ bugLang.common }}</router-link>
      </div>
      <h3>{{ taskLang.common }}</h3>
      <table class="data-table" v-if="tasks.length">
        <thead>
          <tr><th>ID</th><th>{{ taskLang.name }}</th><th>{{ taskLang.status }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="t in tasks" :key="t.id">
            <td>{{ t.id }}</td>
            <td><router-link :to="`/task/${t.id}`">{{ t.name }}</router-link></td>
            <td>{{ t.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ myLang.noTask }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getExecutionById, getExecutionTasks, startExecution, closeExecution, suspendExecution, activateExecution, deleteExecution } from '@/api/execution'
import { execution as executionLang, common as commonLang, task as taskLang, story as storyLang, bug as bugLang, my as myLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const execution = ref(null)
const tasks = ref([])
const loading = ref(true)
const errorMsg = ref('')

async function load() {
  const id = Number(route.params.id)
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await getExecutionById(id)
    execution.value = res?.data ?? res
    if (execution.value) {
      const taskRes = await getExecutionTasks(id)
      tasks.value = taskRes?.data ?? []
    }
  } finally {
    loading.value = false
  }
}

async function doStart() {
  if (!execution.value) return
  errorMsg.value = ''
  try {
    const res = await startExecution(execution.value.id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e.response?.data?.message || e.message || commonLang.operateFail
  }
}
async function doClose() {
  if (!execution.value || !confirm(commonLang.confirmClose)) return
  errorMsg.value = ''
  try {
    const res = await closeExecution(execution.value.id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e.response?.data?.message || e.message || commonLang.operateFail
  }
}
async function doSuspend() {
  if (!execution.value) return
  errorMsg.value = ''
  try {
    const res = await suspendExecution(execution.value.id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e.response?.data?.message || e.message || commonLang.operateFail
  }
}
async function doActivate() {
  if (!execution.value) return
  errorMsg.value = ''
  try {
    const res = await activateExecution(execution.value.id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e.response?.data?.message || e.message || commonLang.operateFail
  }
}
async function doDelete() {
  if (!execution.value || !confirm(commonLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await deleteExecution(execution.value.id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/execution?project=${execution.value.project}`)
  } catch (e) {
    errorMsg.value = e.response?.data?.message || e.message || commonLang.operateFail
  }
}

onMounted(load)
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.sub-nav { display: flex; gap: 8px; flex-wrap: wrap; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
