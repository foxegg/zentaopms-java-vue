<template>
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
  </div>
  <p v-else-if="!loading">{{ commonLang.noData }}</p>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUserProfile } from '@/api/user'
import { common as commonLang, user as userLang } from '@/lang/zh-cn'

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
