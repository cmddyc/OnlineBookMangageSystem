<template>
  <div class="welcome-main">
    <h3 class="greet">亲爱的用户
      <span style="color:gray;font-style: italic;">{{user_name}}</span> , 欢迎使用</h3>
    <div class="title">在线图书系统</div>
    <!-- <div v-if= "user_name == 'admin'"></div> -->
    <el-descriptions class="margin-top" title="个人信息" :column="4" :size="size" border>
      <template slot="extra">
        <el-button type="primary" size="small">操作</el-button>
      </template>
      <el-descriptions-item width="40%">
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        {{user_name}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          电话
        </template>
        18100000000
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-male"></i>
          性别
        </template>
        男
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-medal-1"></i>
          年龄
        </template>
        20
        <!-- <el-tag size="small">学校</el-tag> -->
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          邮箱
        </template>
        江苏省苏州市吴中区吴中大道 1188 号
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-paperclip"></i>
          信誉值
        </template>
        10
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-reading"></i>
          当前借阅图书册数
        </template>
        2
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        user_name: ''
      }
    },
    methods: {

    },
    created() {
      this.user_name = window.localStorage.getItem('name')
    }
  }
</script>

<style scoped>
  .welcome-main {
    padding: 0;
    margin: 0;
    background-color: #f2f5f8;
    text-align: center;
  }

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .swiper-img {
    height: 100%;
    width: 60%;
  }

  .title {
    position: relative;
    user-select: none;
    font-size: 2rem;
    text-shadow: 5px 5px 5px gray;
    transform: translate(-5.8rem, 0);
    padding: 0;
    margin: 0.5rem 0 4rem 0;
  }

  .greet {
    position: relative;
    user-select: none;
    font-size: 1rem;
    float: left;
  }
</style>