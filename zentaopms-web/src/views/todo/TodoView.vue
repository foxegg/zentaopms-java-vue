<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">待办详情</h1>
      <router-link to="/todo" class="btn">返回列表</router-link>
      <router-link v-if="todo" :to="`/todo/edit/${todo.id}`" class="btn">编辑</router-link>
      <button v-if="todo && todo.status !== 'done' && todo.status !== 'closed'" class="btn btn-primary" @click="doFinish">完成</button>
    </div>
    <div v-if="todo" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ todo.id }}</td></tr>
        <tr><th>名称/内容</th><td>{{ todo.name || todo.title || todo.content }}</td></tr>
        <tr><th>状态</th><td>{{ todo.status }}</td></tr>
        <tr><th>指派给</th><td>{{ todo.assignedToName || todo.assignedTo || '-' }}</td></tr>
        <tr><th>日期</th><td>{{ todo.date || '-' }}</td></tr>
        <tr><th>类型</th><td>{{ todo.type || '-' }}</td></tr>
        <tr v-if="todo.desc"><th>描述</th><td>{{ todo.desc }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>待办不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTodoById, finishTodo } from '@/api/todo'

const route = useRoute()
const todo = ref(null)
const loading = ref(true)

async function load() {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getTodoById(id)
    todo.value = res.data || null
  } finally {
    loading.value = false
  }
}

async function doFinish() {
  try {
    await finishTodo(todo.value.id)
    load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失败')
  }
}

onMounted(load)
</script>
