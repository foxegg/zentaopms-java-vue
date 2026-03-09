<template>
  <div class="table-wrap" v-if="!loading">
    <div class="form-group" style="display:flex;gap:12px;flex-wrap:wrap;">
      <label>类型</label>
      <select v-model="type" @change="load(1)" style="width:auto;">
        <option value="assignedTo">指派给我</option>
        <option value="openedBy">由我创建</option>
        <option value="closedBy">由我关闭</option>
        <option value="reviewedBy">由我评审</option>
      </select>
      <label>需求类型</label>
      <select v-model="storyType" @change="load(1)" style="width:auto;">
        <option value="story">需求</option>
        <option value="requirement">用户需求</option>
        <option value="epic">史诗</option>
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
          <th>标题</th>
          <th>状态</th>
          <th>优先级</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="s in list" :key="s.id">
          <td>{{ s.id }}</td>
          <td>{{ s.title }}</td>
          <td>{{ s.status }}</td>
          <td>{{ s.pri }}</td>
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
import { getUserStory } from '@/api/user'

const props = defineProps({ user: Object })
const list = ref([])
const loading = ref(true)
const type = ref('assignedTo')
const storyType = ref('story')
const orderBy = ref('id_desc')
const pageID = ref(1)
const recPerPage = 20
const pager = ref(null)

async function load(page = 1) {
  if (!props.user?.id) return
  pageID.value = page
  loading.value = true
  try {
    const res = await getUserStory(props.user.id, { storyType: storyType.value, type: type.value, orderBy: orderBy.value, pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || null
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, load)
onMounted(load)
</script>

<style scoped>
.muted { color: #999; font-size: 13px; margin-top: 8px; }
</style>
