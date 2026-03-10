<template>
  <div class="table-wrap" v-if="!loading">
    <div class="form-group" style="display:flex;gap:12px;flex-wrap:wrap;">
      <label>{{ userLang.type }}</label>
      <select v-model="type" @change="load(1)" style="width:auto;">
        <option value="assignedTo">{{ userLang.assignedTo }}</option>
        <option value="openedBy">{{ userLang.openedBy }}</option>
        <option value="resolvedBy">{{ userLang.resolvedBy }}</option>
        <option value="closedBy">{{ userLang.closedBy }}</option>
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
          <th>{{ bugLang.title }}</th>
          <th>{{ commonLang.status }}</th>
          <th>{{ bugLang.pri }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="b in list" :key="b.id">
          <td>{{ b.id }}</td>
          <td>{{ b.title }}</td>
          <td>{{ b.status }}</td>
          <td>{{ b.pri }}</td>
        </tr>
      </tbody>
    </table>
    <div class="pager" v-if="pager">
      <span>{{ commonLang.totalCount.replace('{total}', pager.recTotal) }}</span>
      <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">{{ commonLang.prevPage }}</button>
      <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">{{ commonLang.nextPage }}</button>
    </div>
  </div>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUserBug } from '@/api/user'
import { common as commonLang, bug as bugLang, user as userLang } from '@/lang/zh-cn'

const props = defineProps({ user: Object })
const list = ref([])
const pager = ref(null)
const loading = ref(true)
const type = ref('assignedTo')
const orderBy = ref('id_desc')
const pageID = ref(1)
const recPerPage = 20

async function load(page = 1) {
  if (!props.user?.id) return
  pageID.value = page
  loading.value = true
  try {
    const res = await getUserBug(props.user.id, { type: type.value, orderBy: orderBy.value, pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, () => load(1))
onMounted(() => load())
</script>
