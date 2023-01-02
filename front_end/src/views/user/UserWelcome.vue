<template>
  <div class="welcome-main">
    <h3 class="greet">亲爱的用户
      <span style="color:gray;font-style: italic;">{{user_id}}</span> , 欢迎使用</h3>
    <div class="title">在线图书系统</div>
    <el-descriptions class="margin-top" title="个人信息" :column="1" :size="size" border>
      <template slot="extra">
        <div v-if="!isEdit">
          <el-button type="warning" size="small" @click="editInfo()">编辑</el-button>
        </div>
        <div v-else>
          <el-button type="success" size="small" @click="editInfo()">完成</el-button>
        </div>
      </template>
      <el-descriptions-item label="test" :labelStyle='labelStyle'>
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        <div v-if="!isEdit">{{ userInfo.username }}</div>
        <div v-else>
          <el-input v-model="userInfo.username"></el-input>
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          电话
        </template>
        <div v-if="!isEdit">{{ userInfo.telephone }}</div>
        <div v-else>
          <el-input v-model="userInfo.telephone"></el-input>
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-male"></i>
          性别
        </template>
        <div v-if="!isEdit">{{ userInfo.sex }}</div>
        <div v-else>
          <el-input v-model="userInfo.sex"></el-input>
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-medal-1"></i>
          年龄
        </template>        
        <div v-if="!isEdit">{{ userInfo.age }}</div>
        <div v-else>
          <el-input v-model="userInfo.age"></el-input>
        </div>
        <!-- <el-tag size="small">学校</el-tag> -->
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>
          邮箱
        </template>
        <div v-if="!isEdit">{{ userInfo.email }}</div>
        <div v-else>
          <el-input v-model="userInfo.email"></el-input>
        </div>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-paperclip"></i>
          信誉值
        </template>
        {{userInfo.credit}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-reading"></i>
          当前借阅图书册数
        </template>
        -
      </el-descriptions-item>
    </el-descriptions>

    <!-- <el-dialog title="编辑个人信息" :visible.sync="editDialogVisible" width="50%">
      <span>
        <el-form :model="adminInfo" ref="adminInfo" :rules="editFormRules" label-width="70px">
          <el-form-item label="用户名">
            <el-input v-model="adminInfo.admin_id" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="adminInfo.admin_email"></el-input>
          </el-form-item>
          <el-form-item label="手机">
            <el-input v-model="editInfo.admin_phone"></el-input>
          </el-form-item>
        </el-form>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="closeEditDialog()">确 定</el-button>
      </span>
    </el-dialog> -->
  </div>
</template>

<script>
  import qs from 'qs'
  export default {
    data() {
      return {
        labelStyle: {'width': '200px'},
        user_id: '',
        userInfo: {
          username: "",
          telephone: "",
          sex: "",
          age: "",
          email: ""
        },
        isEdit: false,
      }
    },
    methods: {
      async getUserInfo() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": window.localStorage.getItem('id')
        }
        await this.$http.post(this.baseUrl+'/userInfoSearch', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          console.log(res.data.state)
          console.log(res.data.result)
          if (res.data.state === 'fail') {
            this.$message.error({
            message: '用户信息获取失败',
            duration: 1500
          })
          } else {
            this.userInfo.username = res.data.result[0].userName
            this.userInfo.sex = res.data.result[0].sex // test2
            this.userInfo.age = res.data.result[0].age // test
            this.userInfo.telephone = res.data.result[0].telephone
            this.userInfo.email = res.data.result[0].email
            this.userInfo.credit = res.data.result[0].credit
            console.log("username: "+this.userInfo.username)
          }
        }).catch(err => {
          if (err) {
            this.$message.error({
              message: '[CATCH]用户信息获取失败' + err,
              duration: 5000
            })
          }
        })        
      },
      editInfo() {
        this.isEdit = !this.isEdit
        if (!this.isEdit) //完成修改
        {
          let submit = {
            "id": window.localStorage.getItem('id'),
            "token": window.localStorage.getItem('token'),
            "newId": "no",
            "name": this.userInfo.username,
            "password": "no",
            "age": this.userInfo.age,
            "sex": this.userInfo.sex,
            "telephone": this.userInfo.telephone,
            "email": this.userInfo.email,
            "userId": "no",
            "credit": "no"
          }
          this.$http.post(this.baseUrl+'/userInfoChange', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
            console.log(res.data.result)
            if (res.data.state === 'fail') {
              this.$message.error({
                message: "个人信息修改失败"
              })
            } else {
              this.$message.success({
                message: "个人信息修改成功"
              })
              this.getUserInfo()
            }
          }).catch(err => {
            if (err) {
              this.$message.error({
                message: '[CATCH]用户信息修改失败' + err,
                duration: 5000
              })
            }
          })
        }
      }
    },
    created() {
      this.user_id = window.localStorage.getItem('id')
      this.getUserInfo()
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

  .title {
    position: relative;
    user-select: none;
    font-size: 2rem;
    text-shadow: 5px 5px 5px gray;
    transform: translate(-5.8rem, 0);
    padding: 0;
    margin: 0.5rem 0 4rem 0;
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

  .greet {
    position: relative;
    user-select: none;
    font-size: 1rem;
    float: left;
  }
</style>