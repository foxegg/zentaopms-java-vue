<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ userLang.browse }}</h1>
      <router-link to="/user/create" class="btn btn-primary">{{ userLang.create }}</router-link>
      <router-link to="/user/batchCreate" class="btn">{{ userLang.batchCreate }}</router-link>
      <router-link to="/user/batchEdit" class="btn">{{ userLang.batchEdit }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ userLang.realname }}</th>
            <th>{{ userLang.account }}</th>
            <th>{{ userLang.dept }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="u in list" :key="u.id">
            <td>{{ u.id }}</td>
            <td>{{ u.realname }}</td>
            <td>{{ u.account }}</td>
            <td>{{ u.dept }}</td>
            <td>
              <router-link :to="`/user/${u.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/user/edit/${u.id}`" class="btn">{{ commonLang.edit }}</router-link>
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
import { getUserList } from '@/api/user'
import { user as userLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const pageID = ref(1)
const recPerPage = 20

async function load(page = 1) {
  pageID.value = page
  loading.value = true
  try {
    const res = await getUserList({ pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

onMounted(() => load())
</script>
