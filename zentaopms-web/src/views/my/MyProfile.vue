<template>
  <div>
    <div class="page-header"><h1>{{ myLang.profile }}</h1></div>
    <div class="table-wrap profile-wrap" v-if="!loading && profile">
      <table class="data-table">
        <tbody>
          <tr><th width="120">{{ userLang.account }}</th><td>{{ profile.account }}</td></tr>
          <tr><th>{{ userLang.realname }}</th><td>{{ profile.realname }}</td></tr>
          <tr><th>{{ userLang.dept }}</th><td>{{ profile.dept }}</td></tr>
          <tr><th>{{ userLang.email }}</th><td>{{ profile.email }}</td></tr>
          <tr><th>{{ userLang.role }}</th><td>{{ profile.role }}</td></tr>
        </tbody>
      </table>
      <p><router-link to="/my/preference" class="btn">{{ myLang.preference }}</router-link></p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyProfile } from '@/api/my'
import { my as myLang, user as userLang, common as commonLang } from '@/lang/zh-cn'

const profile = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyProfile()
    profile.value = res.data || null
  } finally { loading.value = false }
})
</script>

<style scoped>
.profile-wrap .data-table th { width: 120px; }
</style>
