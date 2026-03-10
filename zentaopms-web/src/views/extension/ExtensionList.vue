<template>
  <div>
    <div class="page-header">
      <h1>{{ extensionLang.title }}</h1>
    </div>
    <p class="mb-2">{{ extensionLang.descTip }}</p>
    <div class="table-wrap">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ extensionLang.module }}</th>
            <th>{{ extensionLang.desc }}</th>
            <th>{{ commonLang.status }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in modules" :key="m.id">
            <td>{{ m.name }}</td>
            <td>{{ m.desc }}</td>
            <td>
              <router-link v-if="m.path" :to="m.path">{{ m.status }}</router-link>
              <span v-else>{{ m.status }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { common as commonLang, extension as extensionLang } from '@/lang/zh-cn'

const modules = [
  { id: 'ai', name: 'AI', desc: 'AI 应用', status: '占位' },
  { id: 'aiapp', name: 'AI 应用', desc: 'AI 应用扩展', status: '占位' },
  { id: 'bi', name: 'BI', desc: '商业智能', status: '占位' },
  { id: 'cache', name: '缓存', desc: '缓存管理', status: '占位' },
  { id: 'chart', name: '图表', desc: '图表', status: '占位' },
  { id: 'ci', name: 'CI', desc: '持续集成', status: '占位' },
  { id: 'design', name: '设计', desc: '设计', status: '已实现', path: '/design' },
  { id: 'feedback', name: '反馈', desc: '反馈', status: '已实现', path: '/feedback' },
  { id: 'git', name: 'Git', desc: 'Git 集成', status: '占位' },
  { id: 'gitea', name: 'Gitea', desc: 'Gitea 集成', status: '占位' },
  { id: 'gitlab', name: 'GitLab', desc: 'GitLab 集成', status: '占位' },
  { id: 'gogs', name: 'Gogs', desc: 'Gogs 集成', status: '占位' },
  { id: 'holiday', name: '节假日', desc: '节假日设置', status: '已实现', path: '/holiday' },
  { id: 'host', name: '主机', desc: '主机管理', status: '已实现', path: '/host' },
  { id: 'install', name: '安装', desc: '系统安装', status: '占位' },
  { id: 'jenkins', name: 'Jenkins', desc: 'Jenkins 集成', status: '已实现', path: '/jenkins' },
  { id: 'mr', name: '合并请求', desc: 'MR', status: '已实现', path: '/mr' },
  { id: 'sonarqube', name: 'SonarQube', desc: 'SonarQube 集成', status: '已实现', path: '/sonarqube' },
  { id: 'space', name: '空间', desc: '空间', status: '占位' },
  { id: 'sso', name: '单点登录', desc: 'SSO', status: '占位' },
  { id: 'store', name: '应用市场', desc: '应用市场', status: '占位' },
  { id: 'svn', name: 'SVN', desc: 'SVN 集成', status: '占位' },
  { id: 'tutorial', name: '教程', desc: '使用教程', status: '占位' },
  { id: 'upgrade', name: '升级', desc: '系统升级', status: '占位' },
]
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
</style>
