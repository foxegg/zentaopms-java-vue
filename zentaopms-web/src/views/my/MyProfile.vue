<template>
  <div>
    <div class="page-header"><h1>我的档案</h1></div>
    <div class="table-wrap profile-wrap" v-if="!loading && profile">
      <table class="data-table">
        <tbody>
          <tr><th width="120">账号</th><td>{{ profile.account }}</td></tr>
          <tr><th>姓名</th><td>{{ profile.realname }}</td></tr>
          <tr><th>部门</th><td>{{ profile.dept }}</td></tr>
          <tr><th>邮箱</th><td>{{ profile.email }}</td></tr>
          <tr><th>角色</th><td>{{ profile.role }}</td></tr>
        </tbody>
      </table>
      <p><router-link to="/my/preference" class="btn">偏好设置</router-link></p>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyProfile } from '@/api/my'

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
