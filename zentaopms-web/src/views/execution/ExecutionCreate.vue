<template>
  <div>
    <div class="page-header">
      <h1>新建执行</h1>
      <router-link :to="backUrl" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>项目 *</label>
          <select v-model.number="form.project" required>
            <option :value="0">请选择</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>代号</label>
          <input v-model="form.code" />
        </div>
        <div class="form-group">
          <label>开始日期</label>
          <input v-model="form.begin" type="date" />
        </div>
        <div class="form-group">
          <label>结束日期</label>
          <input v-model="form.end" type="date" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="backUrl" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createExecution } from '@/api/execution'
import { getProjectAll } from '@/api/project'

const route = useRoute()
const router = useRouter()
const form = ref({ project: 0, name: '', code: '', begin: '', end: '' })
const projects = ref([])
const submitting = ref(false)

const backUrl = computed(() => form.value.project ? `/execution?project=${form.value.project}` : '/execution')

async function onSubmit() {
  if (!form.value.project) return
  submitting.value = true
  try {
    const res = await createExecution(form.value)
    router.push(`/execution/${res.id}`)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProjectAll()
  projects.value = r.data || []
  const q = route.query.project
  if (q) form.value.project = Number(q) || 0
})
</script>
