<template>
  <div>
    <div class="page-header">
      <h1>{{ myLang.indexAction }}</h1>
    </div>
    <div v-if="!loading && data" class="my-dashboard">
      <section class="table-wrap">
        <h3>{{ myLang.todo }}</h3>
        <p v-if="!data.todos?.length">{{ myLang.noData }}</p>
        <ul v-else>
          <li v-for="t in (data.todos || []).slice(0, 10)" :key="t.id">{{ t.name || t.title }}</li>
        </ul>
        <router-link to="/my/todo">{{ myLang.viewAll }}</router-link>
      </section>
      <section class="table-wrap">
        <h3>{{ myLang.task }}</h3>
        <p v-if="!data.tasks?.length">{{ myLang.noData }}</p>
        <ul v-else>
          <li v-for="t in (data.tasks || []).slice(0, 10)" :key="t.id">{{ t.name }}</li>
        </ul>
        <router-link to="/my/task">{{ myLang.viewAll }}</router-link>
      </section>
      <section class="table-wrap">
        <h3>{{ myLang.bug }}</h3>
        <p v-if="!data.bugs?.length">{{ myLang.noData }}</p>
        <ul v-else>
          <li v-for="b in (data.bugs || []).slice(0, 10)" :key="b.id">{{ b.title }}</li>
        </ul>
        <router-link to="/my/bug">{{ myLang.viewAll }}</router-link>
      </section>
      <section class="table-wrap">
        <h3>{{ myLang.story }}</h3>
        <p v-if="!data.stories?.length">{{ myLang.noData }}</p>
        <ul v-else>
          <li v-for="s in (data.stories || []).slice(0, 10)" :key="s.id">{{ s.title }}</li>
        </ul>
        <router-link to="/my/story">{{ myLang.viewAll }}</router-link>
      </section>
      <p>
        <router-link to="/my/dynamic">{{ myLang.dynamic }}</router-link> ·
        <router-link to="/my/score">{{ myLang.score }}</router-link> ·
        <router-link to="/my/work">{{ myLang.work }}</router-link> ·
        <router-link to="/my/calendar">{{ myLang.calendar }}</router-link> ·
        <router-link to="/my/profile">{{ myLang.profile }}</router-link> ·
        <router-link to="/my/preference">{{ myLang.preference }}</router-link>
      </p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyIndex } from '@/api/my'
import { my as myLang, common as commonLang } from '@/lang/zh-cn'

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
