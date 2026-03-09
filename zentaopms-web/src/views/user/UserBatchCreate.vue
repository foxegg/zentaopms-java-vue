<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">批量创建用户</h1>
      <router-link to="/user" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <p class="mb-2">每行一个用户，账号、姓名、密码为必填。</p>
      <form @submit.prevent="onSubmit">
        <div class="form-actions mb-2">
          <button type="button" class="btn" @click="addRow">+ 增加一行</button>
          <button type="button" class="btn" @click="rows = []">清空</button>
        </div>
        <table class="data-table">
          <thead>
            <tr>
              <th>账号 *</th>
              <th>姓名 *</th>
              <th>密码 *</th>
              <th>邮箱</th>
              <th>角色</th>
              <th>部门ID</th>
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
              <td><button type="button" class="btn btn-sm" @click="removeRow(i)">删除</button></td>
            </tr>
          </tbody>
        </table>
        <div v-if="!rows.length" class="py-2 text-muted">请点击「增加一行」后填写用户信息。</div>
        <div class="form-actions mt-2">
          <button type="submit" class="btn btn-primary" :disabled="!rows.length || submitting">保存</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { batchCreateUsers } from '@/api/user'

const router = useRouter()
const rows = ref([{ account: '', realname: '', password: '', email: '', role: 'dev', dept: 0 }])
const submitting = ref(false)

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
  submitting.value = true
  try {
    await batchCreateUsers(users)
    router.push('/user')
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
</style>
