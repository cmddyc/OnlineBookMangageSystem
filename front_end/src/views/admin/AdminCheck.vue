<template>
  <div>
    <div style="text-align:center;user-select:none;"><span class="mytitle">反馈管理</span></div>
    <!-- 空状态显示区 -->
    <el-empty description="暂无反馈" v-if="!isFind" :image-size="200" style="margin-top: 5rem;user-select:none;"></el-empty>
    <!-- 列表显示区域 -->
    <el-table class="book-table" :data="feedbackList" v-if="isFind" v-loading="loading" style="width: 100%;">
      <el-table-column type="index" align="center" label="#"></el-table-column>
      <el-table-column label="用户ID" align="center" prop="userId"></el-table-column>
      <el-table-column label="反馈时间" width="150" align="center" prop="feedbackTime"></el-table-column>
      <el-table-column label="反馈内容" align="center" prop="feedbackContent"></el-table-column>
      <el-table-column label="状态" width="120" align="center" prop="feedbackState">
        <!-- <el-tag type="info">审核中</el-tag> -->
      </el-table-column>
      <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-link type="success" @click="agreeWith(scope.row)">通过</el-link>
            <h7>&nbsp</h7>
            <el-link type="danger" @click="disagreeWith(scope.row)">拒绝</el-link>
          </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import qs from 'qs'
  import {
    mapState,
    mapMutations,
    mapGetters,
    mapActions
  } from 'vuex'
  export default {
    inject: ['reload'],
    data() {
      return {
        isFind: false,
        loading: true,
        feedbackList: [],
        timestamp: []
      }
    },
    computed: {
      ...mapState('userAbout', ['id'])
    },
    methods: {
      async getFeedback() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": "",
          "startTime": Date.parse("2022/12/1 10:00"),
          "endTime": Date.parse(new Date()), 
          "state": "no",
        }
        await this.$http.post(this.baseUrl+'/searchFeedBack', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取反馈失败',
              duration: 1000
            })
          } else {
            this.feedbackList = res.data.result
            for (var i = 0; i < this.feedbackList.length; i++) {
              this.timestamp[i] = this.feedbackList[i].feedbackTimeId
              var date = new Date(parseInt(this.feedbackList[i].feedbackTimeId))
              this.feedbackList[i].feedbackTime = date.getFullYear() + '-' + (date.getMonth()+1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes())
            }
            if (this.feedbackList.length !== 0) {
              this.isFind = true
              this.loading = false
            }
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取反馈失败' + err})})
      },
      async agreeWith(row) {
        console.log(row.feedbackTimeId.substring(0, 13))
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": row.userId,
          "feedbackTimeId": row.feedbackTimeId.substring(0, 13),
          "feedbackState": 'agree',
        }
        await this.$http.post(this.baseUrl+'/admin/handleFeedBack', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '处理反馈失败',
              duration: 1000
            })
          } else {
            this.$message.success({message: '处理反馈成功'})
            this.getFeedback()
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]处理反馈失败' + err})})
      },
      async disagreeWith(row) {
        console.log(row.feedbackTimeId.substring(0, 13))
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": row.userId,
          "feedbackTimeId": row.feedbackTimeId.substring(0, 13),
          "feedbackState": 'disagree',
        }
        await this.$http.post(this.baseUrl+'/admin/handleFeedBack', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '处理反馈失败',
              duration: 1000
            })
          } else {
            this.$message.success({message: '处理反馈成功'})
            this.getFeedback()
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]处理反馈失败' + err})})
      }
    },
    created() {
      this.getFeedback()
    }
  }
</script>

<style scoped>

  .book-table {
    margin-top: 2rem;
    border-radius: 0.5rem;
  }

  .admin-ctrl {
    display: flex;
    justify-content: space-around;
  }
</style>