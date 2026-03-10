<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ todoLang.view }}</h1>
      <router-link to="/todo" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="todo" :to="`/todo/edit/${todo.id}`" class="btn">{{ commonLang.edit }}</router-link>
      <button v-if="todo && todo.status !== 'done' && todo.status !== 'closed'" class="btn btn-primary" @click="doFinish">{{ todoLang.finish }}</button>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div v-if="todo" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ todo.id }}</td></tr>
        <tr><th>{{ todoLang.name }}</th><td>{{ todo.name || todo.title || todo.content }}</td></tr>
        <tr><th>{{ commonLang.status }}</th><td>{{ todo.status }}</td></tr>
        <tr><th>{{ todoLang.assignTo }}</th><td>{{ todo.assignedToName || todo.assignedTo || '-' }}</td></tr>
        <tr><th>{{ todoLang.date }}</th><td>{{ todo.date || '-' }}</td></tr>
        <tr><th>{{ todoLang.type }}</th><td>{{ todo.type || '-' }}</td></tr>
        <tr v-if="todo.desc"><th>{{ todoLang.desc }}</th><td>{{ todo.desc }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTodoById, finishTodo } from '@/api/todo'
import { common as commonLang, todo as todoLang } from '@/lang/zh-cn'

const route = useRoute()
const todo = ref(null)
const loading = ref(true)
const errorMsg = ref('')

async function load() {
  const id = Number(route.params.id)
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await getTodoById(id)
    todo.value = res?.data ?? res
  } finally {
    loading.value = false
  }
}

async function doFinish() {
  if (!todo.value) return
  errorMsg.value = ''
  try {
    const res = await finishTodo(todo.value.id)
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
