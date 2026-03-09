<template>
  <div class="table-wrap" v-if="!loading">
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>名称</th>
          <th>状态</th>
          <th>开始/结束</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="e in list" :key="e.id">
          <td>{{ e.id }}</td>
          <td>{{ e.name }}</td>
          <td>{{ e.status }}</td>
          <td>{{ e.begin }} / {{ e.end }}</td>
        </tr>
      </tbody>
    </table>
    <div class="pager" v-if="pager">
      <span>共 {{ pager.recTotal }} 条</span>
      <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">上一页</button>
      <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">下一页</button>
    </div>
  </div>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUserExecution } from '@/api/user'

const props = defineProps({ user: Object })
const list = ref([])
const pager = ref(null)
const loading = ref(true)
const pageID = ref(1)
const recPerPage = 20

async function load(page = 1) {
  if (!props.user?.id) return
  pageID.value = page
  loading.value = true
  try {
    const res = await getUserExecution(props.user.id, { status: 'all', pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, () => load(1))
onMounted(() => load())
</script>
