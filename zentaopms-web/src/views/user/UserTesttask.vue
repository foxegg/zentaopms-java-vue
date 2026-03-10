<template>
  <div class="table-wrap" v-if="!loading">
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>{{ commonLang.name }}</th>
          <th>{{ commonLang.status }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="t in list" :key="t.id">
          <td>{{ t.id }}</td>
          <td>{{ t.name }}</td>
          <td>{{ t.status }}</td>
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
import { getUserTesttask } from '@/api/user'
import { common as commonLang } from '@/lang/zh-cn'

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
    const res = await getUserTesttask(props.user.id, { pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, () => load(1))
onMounted(() => load())
</script>
