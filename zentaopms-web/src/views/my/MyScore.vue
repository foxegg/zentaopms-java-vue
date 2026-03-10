<template>
  <div>
    <div class="page-header"><h1>{{ myLang.score }}</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table" v-if="list.length">
        <thead>
          <tr><th>{{ scoreLang.time }}</th><th>{{ scoreLang.type }}</th><th>{{ scoreLang.score }}</th><th>{{ scoreLang.desc }}</th></tr>
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
      <p v-else>{{ scoreLang.noRecord }}</p>
      <div class="pager mt-2" v-if="pager && pager.recTotal > 0">
        <span>{{ commonLang.totalCount.replace('{total}', pager.recTotal) }}</span>
        <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">{{ commonLang.prevPage }}</button>
        <span>{{ commonLang.pageNum.replace('{n}', pageID) }}</span>
        <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">{{ commonLang.nextPage }}</button>
      </div>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyScore } from '@/api/my'
import { my as myLang, common as commonLang, score as scoreLang } from '@/lang/zh-cn'

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
