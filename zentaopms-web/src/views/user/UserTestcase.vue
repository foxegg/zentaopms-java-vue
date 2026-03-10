<template>
  <div class="table-wrap" v-if="!loading">
    <div class="form-group">
      <label>{{ userLang.type }}</label>
      <select v-model="type" @change="load(1)">
        <option value="case2Him">{{ userLang.case2Him }}</option>
        <option value="caseByHim">{{ userLang.caseByHim }}</option>
      </select>
    </div>
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th v-if="type === 'caseByHim'">{{ bugLang.title }}</th>
          <th v-else>{{ commonLang.testtask }}/{{ commonLang.testcase }}</th>
          <th>{{ commonLang.status }}</th>
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
import { getUserTestcase } from '@/api/user'
import { common as commonLang, user as userLang, bug as bugLang } from '@/lang/zh-cn'

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
