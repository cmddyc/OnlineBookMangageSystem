<template>
  <div>
    <div style="text-align:center;"><span class="mytitle">在线图书系统</span></div>
    <el-button class="space" type="primary" @click="addFeedback()">添加反馈</el-button>
    <el-empty description="暂无反馈" v-if="!isFind" :image-size="200" style="margin-top: 6.5rem;user-select:none;"></el-empty>    
    <el-table class="book-table" :data="feedbackList" v-if="isFind" v-loading="loading" style="width: 100%;">
      <el-table-column type="index" align="center" label="#"></el-table-column>
      <el-table-column label="反馈时间" width="150" align="center" prop="feedbackTimeId"></el-table-column>
      <el-table-column label="反馈内容" align="center" prop="feedbackContent"></el-table-column>
      <el-table-column label="状态" width="120" align="center" prop="feedbackState">
        <!-- <el-tag type="info">审核中</el-tag> -->
      </el-table-column>
      <!-- <el-table-column label="操作" width="80" align="center">
        <template slot-scope="scope">
          <el-link type="danger" @click="delFeedback(scope.row)">删除</el-link>
        </template>
      </el-table-column> -->
    </el-table>

    <el-dialog title="添加反馈" :visible.sync="formVisible" width="60%">
      <span>
        <el-form :model="content" ref="content" label-width="70px">
          <el-form-item label="反馈内容">
            <el-input type="textarea" :autosize="{ minRows: 3 }" v-model="content"></el-input>
          </el-form-item>
        </el-form>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="postFeedback()">提 交</el-button>
      </span>
    </el-dialog>
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
    data() {
      return {
        content: '',
        feedbackList: [],
        formVisible: false,
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
          "userId": window.localStorage.getItem('id'),
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
              var date = new Date(parseInt(this.feedbackList[i].feedbackTimeId))
              this.feedbackList[i].feedbackTimeId = date.getFullYear() + '-' + (date.getMonth()+1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes())
            }
            if (this.feedbackList.length !== 0) {
              this.isFind = true
              this.loading = false
            }
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取反馈失败' + err})})
      },
      addFeedback() {
        this.content = ''
        this.formVisible = true
      },
      async postFeedback() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "content": this.content,
        }
        await this.$http.post(this.baseUrl+'/user/feedback', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '提交反馈失败',
              duration: 1000
            })
          } else {
            this.$message.success({message:'提交反馈成功'})
            this.formVisible = false
            this.getFeedback()
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]提交反馈失败' + err})})
      },
      // async delFeedback(row) {
      //   this.$confirm(`要删除这条反馈吗？`, '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'info'
      //   }).then(async () => {
      //     let submit = {
      //       "id": window.localStorage.getItem('id'),
      //       "token": window.localStorage.getItem('token'),
      //     }
      //     await this.$http.post(this.baseUrl+'/deleteFeedBack', submit).then(res => {

      //     })
      //   })
      // }
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