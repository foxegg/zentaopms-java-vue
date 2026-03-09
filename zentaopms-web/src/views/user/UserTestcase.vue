<template>
  <div class="table-wrap" v-if="!loading">
    <div class="form-group">
      <label>类型</label>
      <select v-model="type" @change="load(1)">
        <option value="case2Him">指派给我的</option>
        <option value="caseByHim">我创建的</option>
      </select>
    </div>
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th v-if="type === 'caseByHim'">标题</th>
          <th v-else>执行/用例</th>
          <th>状态</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(row, i) in list" :key="type === 'caseByHim' ? row.id : i">
          <td>{{ row.id }}</td>
          <td>{{ type === 'caseByHim' ? row.title : (row.caseId || row.taskId) }}</td>
          <td>{{ row.status }}</td>
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
import { getUserTestcase } from '@/api/user'

const props = defineProps({ user: Object })
const list = ref([])
const pager = ref(null)
const loading = ref(true)
const type = ref('case2Him')
const pageID = ref(1)
const recPerPage = 20

async function load(page) {
  if (!props.user?.id) return
  pageID.value = page === undefined ? 1 : page
  loading.value = true
  try {
    const res = await getUserTestcase(props.user.id, { type: type.value, pageID: pageID.value, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, () => load(1))
onMounted(() => load(1))
</script>
