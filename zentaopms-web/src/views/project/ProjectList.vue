<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ projectLang.browse }}</h1>
      <router-link to="/project/create" class="btn btn-primary">{{ projectLang.create }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ projectLang.id }}</th>
            <th>{{ projectLang.name }}</th>
            <th>{{ projectLang.code }}</th>
            <th>{{ projectLang.status }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in list" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.name }}</td>
            <td>{{ p.code }}</td>
            <td>{{ p.status || '-' }}</td>
            <td>
              <router-link :to="`/project/${p.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/project/edit/${p.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pager" v-if="pager && pager.recTotal > 0">
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
import { getProjectList } from '@/api/project'
import { project as projectLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const pageID = ref(1)
const recPerPage = 20

async function load(page = 1) {
  pageID.value = page
  loading.value = true
  try {
    const res = await getProjectList({ pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

onMounted(() => load())
</script>
