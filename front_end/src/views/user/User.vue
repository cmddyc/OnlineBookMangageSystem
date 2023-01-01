<template>
  <div>
    <div style="text-align:center;"><span class="mytitle">在线图书系统</span></div>
    <el-button class="space" type="primary">添加反馈</el-button>
    <el-empty description="暂无反馈" v-if="!isFind" :image-size="200" style="margin-top: 6.5rem;user-select:none;"></el-empty>    
    <el-table class="book-table" :data="feedbackList" v-if="isFind" v-loading="loading" style="width: 100%;">
      <el-table-column type="index" align="center" label="#"></el-table-column>
      <el-table-column label="反馈时间" width="120" align="center" prop="feedback_time_id"></el-table-column>
      <el-table-column label="反馈内容" align="center" prop="feedback_content"></el-table-column>
      <el-table-column label="状态" width="80" align="center" prop="feedback_state"><el-tag type="info">审核中</el-tag></el-table-column>
      <el-table-column label="操作" width="80" align="center">
        <template slot-scope="scope">
          <el-link type="danger" @click="delFeedback(scope.row)">删除</el-link>
        </template>
      </el-table-column>
    </el-table>
</div>
</template>

<script>
  import {
    mapState,
    mapMutations,
    mapGetters,
    mapActions
  } from 'vuex'
  export default {
    data() {
      return {
        feedbackList: [],
        loading:true,
        isFind:false
      }
    },
    computed:{
      ...mapState('userAbout', ['id'])
    },
    methods: {
      async getFeedback() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "user_id": window.localStorage.getItem('id'),
          "state": "no",
        }
        await this.$http.post(this.baseUrl+'/searchFeedBack', submit).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取反馈失败',
              duration: 1000
            })
          } else {
            this.feedbackList = res.data.result.TFeedBack
            if (this.feedbackList.length !== 0) {
              this.isFind = true
              this.loading = false
            }
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取反馈失败'})})
      },
      async delFeedback(row) {
        this.$confirm(`要删除这条反馈吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(async () => {
          let submit = {
            "id": window.localStorage.getItem('id'),
            "token": window.localStorage.getItem('token'),
          }
          await this.$http.post(this.baseUrl+'/deleteFeedBack', submit).then(res => {

          })
        })
      }
    },
    created() {
      this.getFeedback()
    }
  }
</script>

<style scoped>

  .saying {
    position: absolute;
    color: #ddd;
    bottom: 0;
    font-size: 2rem;
    font-family: STXingkai;
    writing-mode: tb-rl;
  }

    .book-table {
    margin-top: 2rem;
    border-radius: 0.5rem;
  }

  .user-ctrl {
    display: flex;
    justify-content: space-around;
  }
</style>