<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ executionLang.dynamicTitle }} · {{ execution?.name || '' }}</h1>
      <router-link v-if="execution" :to="`/execution/${execution.id}`" class="btn">{{ executionLang.backExecution }}</router-link>
    </div>
    <div v-if="execution && !loading" class="table-wrap">
      <table class="data-table" v-if="list.length">
        <thead>
          <tr><th>{{ executionLang.date }}</th><th>{{ executionLang.action }}</th><th>{{ executionLang.object }}</th><th>{{ executionLang.detail }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="a in list" :key="a.id">
            <td>{{ a.date || '-' }}</td>
            <td>{{ a.action || '-' }}</td>
            <td>{{ a.objectType }} #{{ a.objectID }}</td>
            <td>{{ a.extra || '-' }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ commonLang.noData }}</p>
      <div class="pager mt-2" v-if="pager && pager.recTotal > 0">
        <span>{{ commonLang.totalCount.replace('{total}', pager.recTotal) }}</span>
        <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">{{ commonLang.prevPage }}</button>
        <span>{{ commonLang.pageNum.replace('{n}', pageID) }}</span>
        <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">{{ commonLang.nextPage }}</button>
      </div>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getExecutionById, getExecutionDynamic } from '@/api/execution'
import { common as commonLang, execution as executionLang } from '@/lang/zh-cn'

const route = useRoute()
const executionId = computed(() => Number(route.params.id))
const execution = ref(null)
const list = ref([])
const pager = ref(null)
const loading = ref(true)
const pageID = ref(1)
const recPerPage = 20

async function load(page = 1) {
  pageID.value = page
  loading.value = true
  try {
    const eRes = await getExecutionById(executionId.value)
    execution.value = eRes.data || null
    if (!execution.value) return
    const res = await getExecutionDynamic(executionId.value, { pageID: page, recPerPage })
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
