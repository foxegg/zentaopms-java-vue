<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ userLang.batchCreateTitle }}</h1>
      <router-link to="/user" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger mb-2">{{ errorMsg }}</p>
      <p class="mb-2">{{ userLang.batchCreateHint }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-actions mb-2">
          <button type="button" class="btn" @click="addRow">{{ userLang.addRow }}</button>
          <button type="button" class="btn" @click="rows = []">{{ userLang.clear }}</button>
        </div>
        <table class="data-table">
          <thead>
            <tr>
              <th>{{ userLang.account }} *</th>
              <th>{{ userLang.realname }} *</th>
              <th>{{ userLang.password }} *</th>
              <th>{{ userLang.email }}</th>
              <th>{{ userLang.role }}</th>
              <th>{{ userLang.deptId }}</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, i) in rows" :key="i">
              <td><input v-model="row.account" required class="w-full" /></td>
              <td><input v-model="row.realname" required class="w-full" /></td>
              <td><input v-model="row.password" type="password" required class="w-full" /></td>
              <td><input v-model="row.email" type="email" class="w-full" /></td>
              <td><input v-model="row.role" placeholder="dev" class="w-full" /></td>
              <td><input v-model.number="row.dept" type="number" class="w-full" /></td>
              <td><button type="button" class="btn btn-sm" @click="removeRow(i)">{{ commonLang.delete }}</button></td>
            </tr>
          </tbody>
        </table>
        <div v-if="!rows.length" class="py-2 text-muted">{{ userLang.emptyBatchCreateHint }}</div>
        <div class="form-actions mt-2">
          <button type="submit" class="btn btn-primary" :disabled="!rows.length || submitting">{{ commonLang.save }}</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { batchCreateUsers } from '@/api/user'
import { common as commonLang, user as userLang } from '@/lang/zh-cn'

const router = useRouter()
const rows = ref([{ account: '', realname: '', password: '', email: '', role: 'dev', dept: 0 }])
const submitting = ref(false)
const errorMsg = ref('')

function addRow() {
  rows.value.push({ account: '', realname: '', password: '', email: '', role: 'dev', dept: 0 })
}

function removeRow(i) {
  rows.value.splice(i, 1)
}

async function onSubmit() {
  const users = rows.value
    .filter(function(r) { return r.account && r.realname && r.password })
    .map(function(r) {
      return {
        account: r.account.trim(),
        realname: r.realname.trim(),
        password: r.password,
        email: r.email || undefined,
        role: r.role || 'dev',
        dept: r.dept || 0
      }
    })
  if (!users.length) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await batchCreateUsers(users)
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
</script>

<style scoped>
.w-full { width: 100%; min-width: 80px; }
.mb-2 { margin-bottom: 0.5rem; }
.mt-2 { margin-top: 0.5rem; }
.text-muted { color: #666; }
.text-danger { color: #c00; }
</style>
