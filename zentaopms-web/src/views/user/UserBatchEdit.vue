<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">批量编辑用户</h1>
      <router-link to="/user" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>选择用户（勾选要批量编辑的用户）</label>
          <table class="data-table">
            <thead>
              <tr>
                <th><input type="checkbox" v-model="selectAll" @change="toggleSelectAll" /></th>
                <th>ID</th>
                <th>账号</th>
                <th>姓名</th>
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
          <label>批量修改的字段（仅填写需要修改的项）</label>
          <div class="form-row">
            <div class="form-group"><label>部门ID</label><input v-model.number="fields.dept" type="number" placeholder="不修改留空" /></div>
            <div class="form-group"><label>角色</label><input v-model="fields.role" placeholder="如 dev, admin" /></div>
            <div class="form-group"><label>邮箱</label><input v-model="fields.email" type="email" /></div>
          </div>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="!selectedIds.length || submitting">保存</button>
        </div>
      </form>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getUserList, batchEditUsers } from '@/api/user'

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

async function onSubmit() {
  const userIds = selectedIds.value
  if (!userIds.length) return
  const f = {}
  if (fields.value.dept !== '' && fields.value.dept != null) f.dept = fields.value.dept
  if (fields.value.role) f.role = fields.value.role
  if (fields.value.email !== undefined && fields.value.email !== '') f.email = fields.value.email
  if (Object.keys(f).length === 0) return
  submitting.value = true
  try {
    await batchEditUsers({ userIds, fields: f })
    router.push('/user')
  } finally {
    submitting.value = false
  }
}

onMounted(() => load())
</script>

<style scoped>
.form-row { display: flex; gap: 1rem; flex-wrap: wrap; }
.form-row .form-group { min-width: 120px; }
</style>
