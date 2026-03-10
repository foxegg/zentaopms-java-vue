<template>
  <div>
    <div class="page-header"><h1>{{ myLang.todo }}</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ todoLang.id }}</th>
            <th>{{ todoLang.name }}</th>
            <th>{{ todoLang.status }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in list" :key="t.id">
            <td>{{ t.id }}</td>
            <td>{{ t.name || t.title }}</td>
            <td>{{ t.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ myLang.noTodo }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyTodo } from '@/api/my'
import { my as myLang, todo as todoLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyTodo()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
