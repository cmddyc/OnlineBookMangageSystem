<template>
  <el-container class="main-container">
    <el-header>
      <el-menu default-active="1"
        mode="horizontal"
        class="el-menu-demo"
        background-color="#373d41"
        text-color="#fff"
        active-text-color="#9cdbfd"
        style="height: 100%">
        <el-menu-item index="1" @click="OpenHomePage">
          主页
        </el-menu-item>
        <el-menu-item index="2" @click="OpenBookManagePage">
          图书管理
        </el-menu-item>
        <el-menu-item index="3" @click="OpenUserManagePage">
          用户管理
        </el-menu-item>
        <el-menu-item index="4" @click="OpenFeedbackManagePage">
          反馈管理
        </el-menu-item>
        
        <el-dropdown class="log-out">
          <span class="el-dropdown-link">
            <i class="el-icon-user"></i>
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <el-link type="warning" @click="editAccount">账号修改</el-link>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-link type="danger" @click="logOut">退出登录</el-link>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu>
    </el-header>

    <el-main class="router-main">
      <transition-group appear name="animate__animated animated__bounce" enter-active-class="animate__fadeInDown"
          leave-active-class="animate__fadeOutDown">
        <router-view key="2"></router-view>  
      </transition-group>
    </el-main>
  </el-container>
</template>

<script>
  import 'animate.css'
  import {
    mapState,
    mapMutations,
    mapGetters,
    mapActions
  } from 'vuex'
  export default {
    data() {
      return {
        // 菜单数据
        menuList: [],
        // 被激活的链接地址
        activePath: ''
      }
    },
    computed: {

    },
    methods: {
      ...mapMutations('userAbout', {
        saveInfo: 'SAVE'
      }),
      // 用户主页
      OpenHomePage() {
        this.$router.replace('./adminWelcome')
      },
      OpenBookManagePage() {
        this.$router.replace('./bookManage')
      },      
      OpenUserManagePage() {
        this.$router.replace('./userManage')
      },
      OpenFeedbackManagePage() {
        this.$router.replace('./feedbackManage')
      },
      // 用户登出
      async logOut() {
        window.localStorage.removeItem('token')
        window.localStorage.removeItem('id')
        window.localStorage.removeItem('name')
        this.$router.replace('/login')
      }
    },
    created() {
      window.sessionStorage.removeItem('_one_key')
      this.saveInfo()
    }
  }
</script>

<style scoped>
  .footer {
    color: gray;
    user-select: none;
    float: right;
    font-weight: lighter;
    font-style: italic;
  }

  .el-header {
    background-color: #373d41;
    color: #E9EEF3;
    line-height: 60px;
    padding: 0;
    user-select: none;
  }

  /* .el-footer {
    background-image: linear-gradient(#373d41, black);
    color: #333;
    text-align: center;
    line-height: 60px;
  } */

  .el-main {
    background-color: #f2f5f8;
    color: #333;
  }

  .el-icon-s-home {
    font-family: element-icons!important;
    color: black;
    font-style: normal;
    font-weight: 400;
    font-variant: normal;
    text-transform: none;
    line-height: 1;
    vertical-align: baseline;
    display: inline-block;
  }

    .el-icon-s-home:hover {
    font-family: element-icons!important;
    color: #9cdafc;
    font-style: normal;
    font-weight: 400;
    font-variant: normal;
    text-transform: none;
    line-height: 1;
    vertical-align: baseline;
    display: inline-block;
  }

  body>.el-container {
    margin-bottom: 40px;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }

  .main-container {
    height: 100%;
    background-image: url('https://s4.ax1x.com/2021/12/15/TSGwWt.jpg');
  }

  .el-menu {
    display: flex;
    justify-content: space-around;
    margin-left: 23rem;
    width:60%;
    height: 3.7rem;
  }

  .home:hover {
    cursor: pointer;
  }

  .router-main {
    margin: 0 8rem 0 8rem;
  }

  .log-out {
    cursor: pointer;
    height: 2rem;
    margin-top: 1rem;
    margin-left:10rem;
  }

</style>