<template>
  <div>
    <div style="text-align:center;user-select:none;"><span class="mytitle">用户管理</span></div>
    <!-- 管理员查询区 -->
    <el-row :gutter="20" style="margin:2rem 4rem 0 4rem">
      <el-col :span="20">
        <el-input @keydown.enter.native="findUser" placeholder="请输入查询的用户学号" prefix-icon="el-icon-search"
          v-model="userName">
        </el-input>
      </el-col>
      <el-col :span="3">
        <el-button type="primary" plain @click="findUser">点我查询</el-button>
      </el-col>
    </el-row>
    <!-- 列表显示区域 -->
    <el-table class="user-table" :data="userList" style="width: 100%;">
      <el-table-column type="index" align="center" label="#"></el-table-column>
      <el-table-column label="ID" align="center" prop="id"></el-table-column>
      <el-table-column label="昵称" align="center" prop="name"></el-table-column>
      <el-table-column label="性别" align="center" prop="sex"></el-table-column>
      <el-table-column label="年龄" align="center" prop="age"></el-table-column>
      <el-table-column label="信誉值" align="center" prop="credit"></el-table-column>
      <el-table-column label="电话" align="center" prop="telephone"></el-table-column>
      <el-table-column label="邮箱" align="center" prop="mail_num"></el-table-column>
      <el-table-column label="借阅信息" align="center" prop="book_id" width = 500></el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="120">
        <template slot-scope="scope">
          <el-link type="warning" @click="editUserCredit(scope.row)">编辑</el-link>
          <h7>&nbsp &nbsp</h7>
          <el-link type="danger" @click="delUser(scope.row)">删除</el-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        userName: '',
        userList: []
      }
    },
    methods: {
      async findUser() {
        const res = await this.$http.get(this.baseUrl+`/adminGetUsers?stu_id=${this.userName}`)
        this.userList = res.data.user
        this.userList[0].hasBook = this.userList[0].hasBook.replace(/&/g, ' ')
      },
      async resetPass(row) {
        let submit = {
          id:row.id
        }
        const res = await this.$http.post(this.baseUrl+'/resetPassword',submit)
        if(res.status === 200) {
          this.$message.success({
            message:'重置成功',
            duration:500
          })
        } else {
            this.$message.error({
            message:'重置失败,请稍后再试',
            duration:500
          })
        }
      }
    }
  }
</script>

<style scoped>

  .user-table {
    margin-top: 2rem;
    border-radius: 0.5rem;
  }

  .admin-ctrl {
    display: flex;
    justify-content: space-around;
  }
</style>