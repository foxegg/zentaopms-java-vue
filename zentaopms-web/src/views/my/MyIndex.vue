<template>
  <div>
    <div class="page-header">
      <h1>我的地盘</h1>
    </div>
    <div v-if="!loading && data" class="my-dashboard">
      <section class="table-wrap">
        <h3>待办</h3>
        <p v-if="!data.todos?.length">暂无</p>
        <ul v-else>
          <li v-for="t in (data.todos || []).slice(0, 10)" :key="t.id">{{ t.name || t.title }}</li>
        </ul>
        <router-link to="/my/todo">查看全部</router-link>
      </section>
      <section class="table-wrap">
        <h3>任务</h3>
        <p v-if="!data.tasks?.length">暂无</p>
        <ul v-else>
          <li v-for="t in (data.tasks || []).slice(0, 10)" :key="t.id">{{ t.name }}</li>
        </ul>
        <router-link to="/my/task">查看全部</router-link>
      </section>
      <section class="table-wrap">
        <h3>Bug</h3>
        <p v-if="!data.bugs?.length">暂无</p>
        <ul v-else>
          <li v-for="b in (data.bugs || []).slice(0, 10)" :key="b.id">{{ b.title }}</li>
        </ul>
        <router-link to="/my/bug">查看全部</router-link>
      </section>
      <section class="table-wrap">
        <h3>需求</h3>
        <p v-if="!data.stories?.length">暂无</p>
        <ul v-else>
          <li v-for="s in (data.stories || []).slice(0, 10)" :key="s.id">{{ s.title }}</li>
        </ul>
        <router-link to="/my/story">查看全部</router-link>
      </section>
      <p>
        <router-link to="/my/dynamic">动态</router-link> ·
        <router-link to="/my/score">积分</router-link> ·
        <router-link to="/my/work">工作</router-link> ·
        <router-link to="/my/calendar">日历</router-link> ·
        <router-link to="/my/profile">档案</router-link> ·
        <router-link to="/my/preference">偏好</router-link>
      </p>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyIndex } from '@/api/my'

const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getMyIndex()
    data.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.my-dashboard section { margin-bottom: 20px; }
.my-dashboard h3 { margin: 0 0 10px; font-size: 16px; }
.my-dashboard ul { margin: 0; padding-left: 20px; }
.my-dashboard li { margin: 4px 0; }
</style>
