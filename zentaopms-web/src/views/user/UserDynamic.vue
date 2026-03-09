<template>
  <div class="table-wrap" v-if="!loading">
    <table class="data-table">
      <thead>
        <tr>
          <th>时间</th>
          <th>对象</th>
          <th>动作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="a in list" :key="a.id">
          <td>{{ a.date }}</td>
          <td>{{ a.objectType }} #{{ a.objectID }}</td>
          <td>{{ a.action }}</td>
        </tr>
      </tbody>
    </table>
    <p class="muted">共 {{ list.length }} 条动态</p>
  </div>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUserDynamic } from '@/api/user'

const props = defineProps({ user: Object })
const list = ref([])
const loading = ref(true)

async function load() {
  if (!props.user?.account) return
  loading.value = true
  try {
    const res = await getUserDynamic(props.user.account, { recPerPage: 30 })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.account, load)
onMounted(load)
</script>

<style scoped>
.muted { color: #999; font-size: 13px; margin-top: 8px; }
</style>
