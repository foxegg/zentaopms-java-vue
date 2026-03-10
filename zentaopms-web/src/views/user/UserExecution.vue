<template>
  <div class="table-wrap" v-if="!loading">
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>{{ executionLang.name }}</th>
          <th>{{ executionLang.status }}</th>
          <th>{{ executionLang.dateRange }}</th>
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
import { getUserExecution } from '@/api/user'
import { execution as executionLang, common as commonLang } from '@/lang/zh-cn'

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
