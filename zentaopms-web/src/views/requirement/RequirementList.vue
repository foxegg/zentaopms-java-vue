<template>
  <div>
    <div class="page-header">
      <h1>{{ storyLang.common }}（Requirement）</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ storyLang.title }}</th>
            <th>{{ commonLang.status }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in list" :key="r.id">
            <td>{{ r.id }}</td>
            <td>{{ r.title }}</td>
            <td>{{ r.status }}</td>
            <td><router-link :to="`/requirement/${r.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
      <div class="pager" v-if="pager && pager.recTotal > 0">
        <span>{{ commonLang.totalCount.replace('{total}', pager.recTotal) }}</span>
      </div>
      <p v-if="list.length === 0">{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRequirementList } from '@/api/requirement'
import { common as commonLang, story as storyLang } from '@/lang/zh-cn'

const list = ref([])
const pager = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getRequirementList({ pageID: 1, recPerPage: 20 })
    list.value = res.data || []
    pager.value = res.pager || null
  } finally {
    loading.value = false
  }
})
</script>
