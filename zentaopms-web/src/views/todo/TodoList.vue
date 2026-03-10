<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;flex-wrap:wrap;">
      <h1 style="margin:0;">{{ todoLang.index }}</h1>
      <router-link to="/todo/create" class="btn btn-primary">{{ todoLang.create }}</router-link>
      <select v-model="filterStatus" @change="load" class="form-control" style="width:auto;">
        <option value="all">{{ todoLang.allStatus }}</option>
        <option value="wait">{{ todoLang.statusList.wait }}</option>
        <option value="doing">{{ todoLang.statusList.doing }}</option>
        <option value="done">{{ todoLang.statusList.done }}</option>
        <option value="closed">{{ todoLang.statusList.closed }}</option>
      </select>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ todoLang.id }}</th>
            <th>{{ todoLang.name }}</th>
            <th>{{ todoLang.status }}</th>
            <th>{{ todoLang.assignTo }}</th>
            <th>{{ todoLang.date }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in list" :key="t.id">
            <td>{{ t.id }}</td>
            <td>{{ t.name || t.title || t.content }}</td>
            <td>{{ statusText(t.status) }}</td>
            <td>{{ t.assignedToName || t.assignedTo || '-' }}</td>
            <td>{{ t.date || '-' }}</td>
            <td>
              <router-link :to="`/todo/${t.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/todo/edit/${t.id}`" class="btn">{{ commonLang.edit }}</router-link>
              <button v-if="t.status !== 'done' && t.status !== 'closed'" class="btn" @click="finish(t.id)">{{ todoLang.finish }}</button>
              <button class="btn" @click="del(t.id)">{{ commonLang.delete }}</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ todoLang.noTodo }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getTodoList, finishTodo, deleteTodo } from '@/api/todo'
import { todo as todoLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const list = ref([])
const loading = ref(true)
const filterStatus = ref('all')
const errorMsg = ref('')

const statusText = (status) => (status && todoLang.statusList[status]) || status || '-'

async function load() {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await getTodoList({ status: filterStatus.value === 'all' ? undefined : filterStatus.value })
    list.value = res?.data ?? []
  } finally {
    loading.value = false
  }
}

async function finish(id) {
  errorMsg.value = ''
  try {
    const res = await finishTodo(id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e.response?.data?.message || e.message || commonLang.operateFail
  }
}

async function del(id) {
  if (!confirm(todoLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await deleteTodo(id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e.response?.data?.message || e.message || commonLang.operateFail
  }
}

onMounted(load)
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
