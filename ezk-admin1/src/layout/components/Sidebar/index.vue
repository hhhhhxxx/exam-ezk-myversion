<template>
  <div class="has-logo">
    <logo :collapse="isCollapse"/>
    <el-scrollbar ref="scrollbarRef" wrap-class="scrollbar-wrapper">
      <el-menu ref="menuRef"
        @open="select"
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

    select(index) {
      // 点击最后一个的时候滑动到最底下
      if(this.routes[this.routes.length-1].path === index) {
        const el = this.$refs.scrollbarRef.wrap;
        const beginTime = Date.now();
        const beginValue = el.scrollTop;

        const rAF = window.requestAnimationFrame || (func => setTimeout(func, 16));

        // 一开始高度更新是有延迟 触发了这个事件高度仍然是600多 最终展开为800多
        const oldHeight = this.$refs.scrollbarRef.wrap.scrollHeight;

        const frameFunc = () => {
          const progress = (Date.now() - beginTime) / 500;

          if (progress < 1) {
            el.scrollTop += 15;
            rAF(frameFunc);
          } else {
            const newHeight = el.scrollHeight;
            el.scrollTop = newHeight - oldHeight + 1;
          }
        };
        rAF(frameFunc);
      }

      // const oldHeight = this.$refs.scrollbarRef.wrap.scrollHeight;
      //
      // setTimeout(() => {
      //   if(this.routes[this.routes.length-1].path === index) {
      //
      //     const newHeight = this.$refs.scrollbarRef.wrap.scrollHeight;
      //     console.log(newHeight - oldHeight)
      //     this.$refs.scrollbarRef.wrap.scrollTop = newHeight - oldHeight + 1
      //   }
      // },300)

      // this.$refs.scrollbarRef.wrap.scrollHeight = 154;

    },
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
