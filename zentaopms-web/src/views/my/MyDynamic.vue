<template>
  <div>
    <div class="page-header"><h1>我的动态</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead><tr><th>时间</th><th>对象</th><th>动作</th></tr></thead>
        <tbody>
          <tr v-for="a in list" :key="a.id"><td>{{ a.date }}</td><td>{{ a.objectType }} #{{ a.objectID }}</td><td>{{ a.action }}</td></tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyDynamic } from '@/api/my'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyDynamic({ recPerPage: 50 })
    list.value = res.data || []
  } finally { loading.value = false }
})
</script>
