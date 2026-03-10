<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ userLang.batchEditTitle }}</h1>
      <router-link to="/user" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <p v-if="errorMsg" class="text-danger mb-2">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ userLang.selectUsersPrompt }}</label>
          <table class="data-table">
            <thead>
              <tr>
                <th><input type="checkbox" v-model="selectAll" @change="toggleSelectAll" /></th>
                <th>ID</th>
                <th>{{ userLang.account }}</th>
                <th>{{ userLang.realname }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="u in list" :key="u.id">
                <td><input type="checkbox" :value="u.id" v-model="selectedIds" /></td>
                <td>{{ u.id }}</td>
                <td>{{ u.account }}</td>
                <td>{{ u.realname }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="form-group">
          <label>{{ userLang.batchModifyFields }}</label>
          <div class="form-row">
            <div class="form-group"><label>{{ userLang.deptId }}</label><input v-model.number="fields.dept" type="number" :placeholder="commonLang.leaveEmptyToKeep" /></div>
            <div class="form-group"><label>{{ userLang.role }}</label><input v-model="fields.role" :placeholder="userLang.rolePlaceholder" /></div>
            <div class="form-group"><label>{{ userLang.email }}</label><input v-model="fields.email" type="email" /></div>
          </div>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="!selectedIds.length || submitting">{{ commonLang.save }}</button>
        </div>
      </form>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getUserList, batchEditUsers } from '@/api/user'
import { common as commonLang, user as userLang } from '@/lang/zh-cn'

const router = useRouter()
const list = ref([])
const loading = ref(true)
const selectedIds = ref([])
const selectAll = ref(false)
const submitting = ref(false)
const fields = ref({ dept: '', role: '', email: '' })

async function load() {
  loading.value = true
  try {
    const res = await getUserList({ recPerPage: 200 })
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

function toggleSelectAll() {
  if (selectAll.value) {
    selectedIds.value = list.value.map((u) => u.id)
  } else {
    selectedIds.value = []
  }
}

watch(
  () => list.value.length,
  () => {
    if (selectedIds.value.length === list.value.length && list.value.length) selectAll.value = true
    else selectAll.value = false
  }
)
watch(selectedIds, (ids) => {
  selectAll.value = ids.length === list.value.length && list.value.length > 0
})

const errorMsg = ref('')

async function onSubmit() {
  const userIds = selectedIds.value
  if (!userIds.length) return
  const f = {}
  if (fields.value.dept !== '' && fields.value.dept != null) f.dept = fields.value.dept
  if (fields.value.role) f.role = fields.value.role
  if (fields.value.email !== undefined && fields.value.email !== '') f.email = fields.value.email
  if (Object.keys(f).length === 0) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await batchEditUsers({ userIds, fields: f })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push('/user')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(() => load())
</script>

<style scoped>
.form-row { display: flex; gap: 1rem; flex-wrap: wrap; }
.form-row .form-group { min-width: 120px; }
.text-danger { color: #c00; }
.mb-2 { margin-bottom: 0.5rem; }
</style>
