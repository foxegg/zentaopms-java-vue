<template>
  <div class="table-wrap" v-if="!loading">
    <div class="form-group" style="display:flex;gap:12px;flex-wrap:wrap;">
      <label>{{ userLang.type }}</label>
      <select v-model="type" @change="load(1)" style="width:auto;">
        <option value="assignedTo">{{ userLang.assignedTo }}</option>
        <option value="openedBy">{{ userLang.openedBy }}</option>
        <option value="closedBy">{{ userLang.closedBy }}</option>
        <option value="finishedBy">{{ userLang.finishedBy }}</option>
      </select>
      <label>{{ userLang.sort }}</label>
      <select v-model="orderBy" @change="load(1)" style="width:auto;">
        <option value="id_desc">{{ userLang.idDesc }}</option>
        <option value="id_asc">{{ userLang.idAsc }}</option>
      </select>
    </div>
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>{{ taskLang.name }}</th>
          <th>{{ taskLang.status }}</th>
          <th>{{ taskLang.pri }}</th>
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
    <div class="pager" v-if="pager && pager.recTotal > 0">
      <span>{{ commonLang.totalCount.replace('{total}', pager.recTotal) }}</span>
      <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">{{ commonLang.prevPage }}</button>
      <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">{{ commonLang.nextPage }}</button>
    </div>
  </div>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUserTask } from '@/api/user'
import { task as taskLang, common as commonLang, user as userLang } from '@/lang/zh-cn'

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
