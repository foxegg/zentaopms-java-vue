<template>
  <div>
    <div class="page-header">
      <h1>{{ treeLang.browse }}</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <p v-if="!list.length">{{ commonLang.noData }}{{ treeLang.browseHint }}</p>
      <table v-else class="data-table">
        <thead>
          <tr><th>ID</th><th>{{ commonLang.name }}</th><th>{{ treeLang.parent }}</th><th>{{ commonLang.actions }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="n in list" :key="n.id">
            <td>{{ n.id }}</td>
            <td>{{ n.name }}</td>
            <td>{{ n.parent || '-' }}</td>
            <td><router-link :to="`/tree/edit/${n.id}`" class="btn">{{ commonLang.edit }}</router-link></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTreeBrowse } from '@/api/tree'
import { common as commonLang, tree as treeLang } from '@/lang/zh-cn'

const route = useRoute()
const list = ref([])
const loading = ref(true)

onMounted(async () => {
  loading.value = true
  try {
    const params = { ...route.query }
    const res = await getTreeBrowse(params)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
