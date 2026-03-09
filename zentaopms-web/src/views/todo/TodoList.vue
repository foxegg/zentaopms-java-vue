<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">待办列表</h1>
      <router-link to="/todo/create" class="btn btn-primary">新建待办</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>状态</th>
            <th>指派给</th>
            <th>日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in list" :key="t.id">
            <td>{{ t.id }}</td>
            <td>{{ t.name || t.title || t.content }}</td>
            <td>{{ t.status }}</td>
            <td>{{ t.assignedToName || t.assignedTo || '-' }}</td>
            <td>{{ t.date || '-' }}</td>
            <td>
              <router-link :to="`/todo/${t.id}`" class="btn">查看</router-link>
              <router-link :to="`/todo/edit/${t.id}`" class="btn">编辑</router-link>
              <button v-if="t.status !== 'done' && t.status !== 'closed'" class="btn" @click="finish(t.id)">完成</button>
              <button class="btn" @click="del(t.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">暂无待办</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getTodoList, finishTodo, deleteTodo } from '@/api/todo'

const router = useRouter()
const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getTodoList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function finish(id) {
  try {
    await finishTodo(id)
    load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失败')
  }
}

async function del(id) {
  if (!confirm('确定删除？')) return
  try {
    await deleteTodo(id)
    load()
  } catch (e) {
    alert(e.response?.data?.message || '操作失败')
  }
}

onMounted(load)
</script>
