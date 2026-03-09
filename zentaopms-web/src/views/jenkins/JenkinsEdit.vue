<template>
  <div v-if="form">
    <div class="page-header">
      <h1>编辑 Jenkins</h1>
      <router-link to="/jenkins" class="btn">返回列表</router-link>
    </div>
    <form @submit.prevent="onSubmit" class="form-wrap">
      <div class="form-group">
        <label>名称 *</label>
        <input v-model="form.name" required />
      </div>
      <div class="form-group">
        <label>URL *</label>
        <input v-model="form.url" required />
      </div>
      <div class="form-group">
        <label>账号</label>
        <input v-model="form.account" />
      </div>
      <div class="form-group">
        <label>Token/密码</label>
        <input v-model="form.token" type="password" placeholder="留空不修改" />
      </div>
      <div class="form-actions">
        <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
        <router-link to="/jenkins" class="btn">取消</router-link>
      </div>
    </form>
  </div>
  <p v-else-if="!loading">未找到</p>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getJenkinsById, updateJenkins } from '@/api/jenkins'

const route = useRoute()
const router = useRouter()
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  try {
    const res = await getJenkinsById(route.params.id)
    form.value = res.data || res
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  submitting.value = true
  try {
    await updateJenkins(route.params.id, form.value)
    router.push('/jenkins')
  } finally {
    submitting.value = false
  }
}
</script>
