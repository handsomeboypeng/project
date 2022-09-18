<template>
  <div id="app">
    <el-container style="height: 500px; border: 1px solid #eee">
<!--app是主页面路由切换时候这部分内容是不变的，只有切换路由的时候看路由对应的哪个组件，那个组件就会挂载到router-view上面-->
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
<!--menu标签与router绑定
    el-menu添加router属性
    在页面添加router-view标签，它是一个容器，动态渲染你选择的router
    el-menu-item标签的index值就是要跳转的router，也就是说我点击这里的时候就会跳转到index值的路径
-->
        <el-menu :default-openeds="['0']" router>
<!--          遍历routes拿到一个个item，index是拿到的下标-->
          <el-submenu v-for="(item,index) in $router.options.routes" :index="index+''" v-if="item.show">
            <template slot="title"><i class="el-icon-bell"></i>{{item.name}}</template>
              <el-menu-item v-for="(item2,index2) in item.children" :index="item2.path"
              :class="$route.path==item2.path?'is-active':''">
                {{item2.name}}
              </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-main>
        <router-view></router-view>
      </el-main>
      </el-container>
  </div>
</template>

<script>
  export default {

  };
</script>

<style>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }
</style>
