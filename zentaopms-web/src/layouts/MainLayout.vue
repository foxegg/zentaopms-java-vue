<template>
  <div class="main-layout">
    <header class="topbar">
      <div class="topbar-left">
        <router-link to="/" class="logo">禅道</router-link>
        <nav class="nav">
          <router-link to="/">首页</router-link>
          <router-link to="/my">我的地盘</router-link>
          <router-link to="/project">项目</router-link>
          <router-link to="/execution">执行</router-link>
          <router-link to="/product">产品</router-link>
          <router-link to="/productplan">计划</router-link>
          <router-link to="/program">项目集</router-link>
          <router-link to="/task">任务</router-link>
          <router-link to="/bug">Bug</router-link>
          <router-link to="/story">需求</router-link>
          <router-link to="/todo">待办</router-link>
          <router-link to="/kanban">看板</router-link>
          <router-link to="/build">构建</router-link>
          <router-link to="/release">发布</router-link>
          <router-link to="/testcase">用例</router-link>
          <router-link to="/testtask">测试单</router-link>
          <router-link to="/testreport">测试报告</router-link>
          <router-link to="/testsuite">测试套件</router-link>
          <router-link to="/caselib">用例库</router-link>
          <router-link to="/chart">图表</router-link>
          <router-link to="/weekly">周报</router-link>
          <router-link to="/report">报表</router-link>
          <router-link to="/metric">统计</router-link>
          <router-link to="/search">搜索</router-link>
          <router-link to="/doc">文档</router-link>
          <router-link to="/branch">分支</router-link>
          <router-link to="/tree">树</router-link>
          <router-link to="/company">组织</router-link>
          <router-link to="/dept">部门</router-link>
          <router-link to="/group">权限组</router-link>
          <router-link to="/holiday">节假日</router-link>
          <router-link to="/admin">后台</router-link>
          <router-link to="/system">系统</router-link>
          <router-link to="/setting">设置</router-link>
          <router-link to="/extension">扩展</router-link>
          <router-link to="/user">用户</router-link>
        </nav>
      </div>
      <div class="topbar-right">
        <button class="btn" @click="logout">退出</button>
      </div>
    </header>
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const { setToken } = useAuth()

function logout() {
  setToken(null)
  router.push('/login')
}
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.topbar {
  height: 50px;
  background: #1a73e8;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,.1);
}
.topbar-left {
  display: flex;
  align-items: center;
  gap: 24px;
}
.logo {
  font-weight: 700;
  font-size: 18px;
  color: #fff;
}
.logo:hover {
  color: #fff;
  text-decoration: none;
  opacity: 0.9;
}
.nav a {
  color: rgba(255,255,255,.9);
  padding: 4px 10px;
  border-radius: 4px;
}
.nav a:hover,
.nav a.router-link-active {
  color: #fff;
  background: rgba(255,255,255,.15);
  text-decoration: none;
}
.topbar-right .btn {
  border-color: rgba(255,255,255,.6);
  color: #fff;
}
.topbar-right .btn:hover {
  background: rgba(255,255,255,.15);
  color: #fff;
  border-color: transparent;
}
.main-content {
  flex: 1;
  padding: 20px;
}
</style>
