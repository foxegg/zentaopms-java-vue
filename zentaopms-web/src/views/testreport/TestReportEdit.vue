<template>
  <div>
    <div class="page-header">
      <h1>编辑测试报告</h1>
      <router-link :to="`/testreport/${reportId}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>项目ID</label>
          <input v-model.number="form.project" type="number" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/testreport/${reportId}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>报告不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTestReportById, updateTestReport } from '@/api/testreport'

const route = useRoute()
const router = useRouter()
const reportId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = reportId.value
  loading.value = true
  try {
    const res = await getTestReportById(id)
    const r = res.data
    if (r) form.value = { name: r.name || r.title || '', project: r.project || '' }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateTestReport(reportId.value, form.value)
    router.push(`/testreport/${reportId.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
