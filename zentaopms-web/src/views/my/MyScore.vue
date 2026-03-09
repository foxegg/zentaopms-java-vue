<template>
  <div>
    <div class="page-header"><h1>我的积分</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table" v-if="list.length">
        <thead>
          <tr><th>时间</th><th>类型</th><th>积分</th><th>说明</th></tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id || s.date">
            <td>{{ s.date || s.time || '-' }}</td>
            <td>{{ s.type || '-' }}</td>
            <td>{{ s.score ?? '-' }}</td>
            <td>{{ s.desc || s.extra || '-' }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无积分记录</p>
      <div class="pager mt-2" v-if="pager && pager.recTotal > 0">
        <span>共 {{ pager.recTotal }} 条</span>
        <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">上一页</button>
        <span>第 {{ pageID }} 页</span>
        <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">下一页</button>
      </div>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyScore } from '@/api/my'

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const pageID = ref(1)
const recPerPage = 20

async function load(page = 1) {
  pageID.value = page
  loading.value = true
  try {
    const res = await getMyScore({ pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

onMounted(() => load())
</script>
<style scoped>
.mt-2 { margin-top: 1rem; }
</style>
