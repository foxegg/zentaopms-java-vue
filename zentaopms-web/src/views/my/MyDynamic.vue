<template>
  <div>
    <div class="page-header"><h1>{{ myLang.dynamic }}</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead><tr><th>{{ userLang.dynamicTime }}</th><th>{{ userLang.dynamicObject }}</th><th>{{ userLang.dynamicAction }}</th></tr></thead>
        <tbody>
          <tr v-for="a in list" :key="a.id"><td>{{ a.date }}</td><td>{{ a.objectType }} #{{ a.objectID }}</td><td>{{ a.action }}</td></tr>
        </tbody>
      </table>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyDynamic } from '@/api/my'
import { common as commonLang, my as myLang, user as userLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyDynamic({ recPerPage: 50 })
    list.value = res.data || []
  } finally { loading.value = false }
})
</script>
