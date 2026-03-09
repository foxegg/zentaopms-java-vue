<template>
  <div class="table-wrap" v-if="!loading">
    <div class="form-group" style="display:flex;gap:12px;flex-wrap:wrap;">
      <label>类型</label>
      <select v-model="type" @change="load(1)" style="width:auto;">
        <option value="assignedTo">指派给我</option>
        <option value="openedBy">由我创建</option>
        <option value="closedBy">由我关闭</option>
        <option value="finishedBy">由我完成</option>
      </select>
      <label>排序</label>
      <select v-model="orderBy" @change="load(1)" style="width:auto;">
        <option value="id_desc">ID 降序</option>
        <option value="id_asc">ID 升序</option>
      </select>
    </div>
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>名称</th>
          <th>状态</th>
          <th>优先级</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="t in list" :key="t.id">
          <td>{{ t.id }}</td>
          <td>{{ t.name }}</td>
          <td>{{ t.status }}</td>
          <td>{{ t.pri }}</td>
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
import { getUserTask } from '@/api/user'

const props = defineProps({ user: Object })
const list = ref([])
const pager = ref(null)
const loading = ref(true)
const type = ref('assignedTo')
const orderBy = ref('id_desc')
const pageID = ref(1)
const recPerPage = 20

async function load(page) {
  if (!props.user?.id) return
  pageID.value = page === undefined ? 1 : page
  loading.value = true
  try {
    const res = await getUserTask(props.user.id, { type: type.value, orderBy: orderBy.value, pageID: pageID.value, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, () => load(1))
onMounted(() => load(1))
</script>
