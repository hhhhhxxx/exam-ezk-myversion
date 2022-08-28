<template>
  <div class="has-logo">
    <logo :collapse="isCollapse"/>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical">

        <sidebar-item
          v-for="route in routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"/>
        <!--
          class="animate__animated animate__heartBeat" -->
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapGetters(['sidebar']),
    ...mapGetters('router', ['routes']),
    activeMenu () {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    variables () {
      return variables
    },
    isCollapse () {
      return !this.sidebar.opened
    },
  },
  methods: {
    addMyClass ($event) {
      console.log($event.currentTarget)
      $event.currentTarget.className = 'animate__animated animate__heartBeat'
    },
  },
}
</script>

<style lang="scss">
  .has-logo {
    //position: relative !important;
    //z-index: 2001 !important; // 高过加载登录
  }
</style>
