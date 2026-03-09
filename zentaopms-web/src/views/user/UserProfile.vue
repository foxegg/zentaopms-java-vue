<template>
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
  </div>
  <p v-else-if="!loading">无档案数据</p>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUserProfile } from '@/api/user'

const props = defineProps({ user: Object })
const profile = ref(null)
const loading = ref(true)

async function load() {
  if (!props.user?.id) return
  loading.value = true
  try {
    const res = await getUserProfile(props.user.id)
    profile.value = res.data || null
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, load)
onMounted(load)
</script>

<style scoped>
.profile-wrap .data-table th { width: 120px; }
</style>
