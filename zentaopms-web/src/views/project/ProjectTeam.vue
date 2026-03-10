<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ projectLang.teamTitle }} · {{ project?.name || '' }}</h1>
      <router-link v-if="project" :to="`/project/${project.id}`" class="btn">{{ projectLang.backProject }}</router-link>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div v-if="project && !loading" class="table-wrap">
      <h3>{{ executionLang.currentMembers }}</h3>
      <table class="data-table" v-if="team.length">
        <thead>
          <tr><th>{{ executionLang.account }}</th><th>{{ executionLang.role }}</th><th>{{ executionLang.days }}</th><th>{{ executionLang.hours }}</th></tr>
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
      <p v-else>{{ executionLang.noMembers }}</p>
      <h3 class="mt-2">{{ executionLang.manageMembers }}</h3>
      <form @submit.prevent="onSubmit">
        <table class="data-table">
          <thead>
            <tr><th>{{ executionLang.account }}</th><th>{{ executionLang.role }}</th><th>{{ executionLang.days }}</th><th>{{ executionLang.hours }}</th><th></th></tr>
          </thead>
          <tbody>
            <tr v-for="(row, idx) in members" :key="idx">
              <td>
                <select v-model="row.account" required>
                  <option value="">{{ commonLang.pleaseSelect }}</option>
                  <option v-for="(name, account) in userPairs" :key="account" :value="account">{{ name }}</option>
                </select>
              </td>
              <td><input v-model="row.role" :placeholder="executionLang.role" /></td>
              <td><input v-model.number="row.days" type="number" min="0" :placeholder="executionLang.days" /></td>
              <td><input v-model="row.hours" type="number" min="0" step="0.5" :placeholder="executionLang.hours" /></td>
              <td><button type="button" class="btn" @click="members.splice(idx, 1)">{{ executionLang.remove }}</button></td>
            </tr>
          </tbody>
        </table>
        <button type="button" class="btn mb-2" @click="members.push({ account: '', role: '', days: '', hours: '' })">{{ executionLang.addMember }}</button>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="saving">{{ commonLang.save }}</button>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getProjectById, getProjectTeam, manageProjectMembers } from '@/api/project'
import { getUserPairs } from '@/api/user'
import { project as projectLang, execution as executionLang, common as commonLang } from '@/lang/zh-cn'

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

const errorMsg = ref('')

async function onSubmit() {
  const list = members.value.filter(m => m.account)
  if (!list.length) {
    errorMsg.value = commonLang.pleaseAddMember
    return
  }
  errorMsg.value = ''
  saving.value = true
  try {
    const res = await manageProjectMembers(projectId.value, list.map(m => ({
      account: m.account,
      role: m.role || '',
      days: m.days ?? 0,
      hours: m.hours ?? 0
    })))
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const tRes = await getProjectTeam(projectId.value)
    team.value = tRes.data || []
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || e.message || commonLang.operateFail
  } finally {
    saving.value = false
  }
}
</script>
<style scoped>
.mt-2 { margin-top: 1rem; }
.mb-2 { margin-bottom: 0.5rem; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
