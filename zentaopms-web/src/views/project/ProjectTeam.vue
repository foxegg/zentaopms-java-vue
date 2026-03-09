<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">项目团队 · {{ project?.name || '' }}</h1>
      <router-link v-if="project" :to="`/project/${project.id}`" class="btn">返回项目</router-link>
    </div>
    <div v-if="project && !loading" class="table-wrap">
      <h3>当前成员</h3>
      <table class="data-table" v-if="team.length">
        <thead>
          <tr><th>账号</th><th>角色</th><th>可用天数</th><th>可用工时</th></tr>
        </thead>
        <tbody>
          <tr v-for="m in team" :key="m.account">
            <td>{{ m.account }}</td>
            <td>{{ m.role || '-' }}</td>
            <td>{{ m.days ?? '-' }}</td>
            <td>{{ m.hours ?? '-' }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无成员</p>
      <h3 class="mt-2">管理成员</h3>
      <form @submit.prevent="onSubmit">
        <table class="data-table">
          <thead>
            <tr><th>账号</th><th>角色</th><th>可用天数</th><th>可用工时</th><th></th></tr>
          </thead>
          <tbody>
            <tr v-for="(row, idx) in members" :key="idx">
              <td>
                <select v-model="row.account" required>
                  <option value="">请选择</option>
                  <option v-for="(name, account) in userPairs" :key="account" :value="account">{{ name }}</option>
                </select>
              </td>
              <td><input v-model="row.role" placeholder="角色" /></td>
              <td><input v-model.number="row.days" type="number" min="0" placeholder="天" /></td>
              <td><input v-model="row.hours" type="number" min="0" step="0.5" placeholder="工时" /></td>
              <td><button type="button" class="btn" @click="members.splice(idx, 1)">移除</button></td>
            </tr>
          </tbody>
        </table>
        <button type="button" class="btn mb-2" @click="members.push({ account: '', role: '', days: '', hours: '' })">添加成员</button>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="saving">保存</button>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getProjectById, getProjectTeam, manageProjectMembers } from '@/api/project'
import { getUserPairs } from '@/api/user'

const route = useRoute()
const projectId = computed(() => Number(route.params.id))
const project = ref(null)
const team = ref([])
const userPairs = ref({})
const members = ref([{ account: '', role: '', days: '', hours: '' }])
const loading = ref(true)
const saving = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const [pRes, tRes, uRes] = await Promise.all([
      getProjectById(projectId.value),
      getProjectTeam(projectId.value),
      getUserPairs()
    ])
    project.value = pRes.data || null
    team.value = tRes.data || []
    const up = uRes.data || uRes
    userPairs.value = Array.isArray(up) ? Object.fromEntries((up || []).map(u => [u.account, u.realname || u.account])) : (up || {})
    if (team.value.length) {
      members.value = team.value.map(m => ({
        account: m.account || '',
        role: m.role || '',
        days: m.days ?? '',
        hours: m.hours ?? ''
      }))
    }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  const list = members.value.filter(m => m.account)
  if (!list.length) {
    alert('请至少添加一名成员')
    return
  }
  saving.value = true
  try {
    await manageProjectMembers(projectId.value, list.map(m => ({
      account: m.account,
      role: m.role || '',
      days: m.days ?? 0,
      hours: m.hours ?? 0
    })))
    const tRes = await getProjectTeam(projectId.value)
    team.value = tRes.data || []
  } finally {
    saving.value = false
  }
}
</script>
<style scoped>
.mt-2 { margin-top: 1rem; }
.mb-2 { margin-bottom: 0.5rem; }
</style>
