<template>
  <div>
    <div class="page-header">
      <h1>新建用户</h1>
      <router-link to="/user" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group"><label>账号 *</label><input v-model="form.account" required /></div>
        <div class="form-group"><label>姓名 *</label><input v-model="form.realname" required /></div>
        <div class="form-group"><label>密码 *</label><input v-model="form.password" type="password" required /></div>
        <div class="form-group"><label>邮箱</label><input v-model="form.email" type="email" /></div>
        <div class="form-group"><label>部门</label><input v-model.number="form.dept" type="number" /></div>
        <div class="form-group"><label>角色</label><input v-model="form.role" /></div>
        <button type="submit" class="btn btn-primary">保存</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createUser } from '@/api/user'

const router = useRouter()
const form = ref({ account: '', realname: '', password: '', email: '', dept: 0, role: 'dev' })

async function onSubmit() {
  await createUser(form.value)
  router.push('/user')
}
</script>
