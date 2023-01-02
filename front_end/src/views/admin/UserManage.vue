<template>
  <div>
    <div style="text-align:center;user-select:none;"><span class="mytitle">用户管理</span></div>
    <!-- 管理员查询区 -->
    <el-row :gutter="20" style="margin:2rem 4rem 0 4rem">
      <el-col :span="20">
        <el-input @keydown.enter.native="getUserList(userName)" placeholder="请输入查询的用户ID" prefix-icon="el-icon-search"
          v-model="userName">
        </el-input>
      </el-col>
      <el-col :span="3">
        <el-button type="primary" @click="getUserList(userName)">查询</el-button>
      </el-col>
    </el-row>
    <!-- 列表显示区域 -->
    <el-table class="user-table" :data="userList" style="width: 100%;">
      <el-table-column type="index" align="center" label="#"></el-table-column>
      <el-table-column label="ID" align="center" prop="userId"></el-table-column>
      <el-table-column label="昵称" align="center" prop="userName"></el-table-column>
      <el-table-column label="性别" align="center" prop="sex"></el-table-column>
      <el-table-column label="年龄" align="center" prop="age"></el-table-column>
      <el-table-column label="信誉值" align="center" prop="credit"></el-table-column>
      <el-table-column label="电话" align="center" prop="telephone"></el-table-column>
      <el-table-column label="邮箱" align="center" prop="email"></el-table-column>
      <!-- <el-table-column label="借阅信息" align="center" prop="book_id" width = 500></el-table-column> -->
      <el-table-column label="操作" fixed="right" align="center" width="180">
        <template slot-scope="scope">
          <el-link type="primary" @click="getBookList(scope.row)">借阅查询</el-link>
          <h7>&nbsp &nbsp</h7>
          <el-link type="warning" @click="editUser(scope.row)">编辑</el-link>
          <h7>&nbsp &nbsp</h7>
          <el-link type="danger" @click="delUser(scope.row)">删除</el-link>
        </template>
      </el-table-column>
    </el-table>

    <!-- 用户借阅记录查询弹窗 -->
    <el-dialog :title="formTitle" :visible.sync="formVisible" width="60%">
      <span>
        <el-table class="book-table" :data="returnBookList" style="width: 100%;">      
          <el-table-column type="index" align="center" label="#"></el-table-column>
          <el-table-column label="书名" align="center" prop="bookName"></el-table-column>
          <el-table-column label="图书编号" align="center" prop="borrow.bookId"></el-table-column>
          <el-table-column label="作者" align="center" prop="bookAuthor"></el-table-column>
          <el-table-column label="还书时间" align="center" prop="borrow.borrowEndTime"></el-table-column>
          <!-- <el-table-column label="是否电子书" align="center" prop="borrow.type"></el-table-column> -->
          <el-table-column label="操作" align="center" width="180">
            <template slot-scope="scope">
              <el-link type="primary" @click="borrowConfirm(scope.row)">借阅码</el-link>
            </template>
          </el-table-column>    
        </el-table>
      </span>
    </el-dialog>

    <!-- 用户信息编辑弹窗 -->
    <el-dialog title="编辑用户信息" :visible.sync="form2Visible" @close="getUserList()" width="50%">
      <span>
        <el-form :model="userInfo" ref="userList" label-width="70px">
          <el-form-item label="昵称">
            <el-input v-model="userInfo.userName"></el-input>
          </el-form-item>
          <el-form-item label="信誉值">
            <el-input v-model="userInfo.credit"></el-input>
          </el-form-item>          
          <el-form-item label="性别">
            <el-input v-model="userInfo.sex"></el-input>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="userInfo.age"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="userInfo.email"></el-input>
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="userInfo.telephone"></el-input>
          </el-form-item>
        </el-form>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeEditDialog()">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import qs from 'qs'
  export default {
    data() {
      return {
        userName: '',
        borrowCode: '',
        userList: [],
        userInfo: {},
        returnBookList: [],

        dict: {},
        returnResult: [],

        formTitle: '',
        formVisible: false,
        form2Visible: false
      }
    },
    methods: {
      async getUserList(id = 'no') {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": id
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
            this.userList = res.data.result
            //console.log("username: "+this.userList[0].userName)
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
      async getBookList(row) {
        this.returnBookList = []
        this.returnResult = []
        this.formTitle = '用户借阅信息查询：' + row.userId
        // this.formVisible = true
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": row.userId,
          "bookId": "no",
          "type": "normal" //+ "outtime"
        }
        await this.$http.post(this.baseUrl+'/borrowSearch', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取用户图书列表失败',
              duration: 1000
            })
          } else {
            //console.log(res.data.result[0].bookId)
            this.returnBookList = res.data.result
            this.handleInfo()
            this.formVisible = true           
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取用户图书列表失败 ' + err})})        
      },
      async handleInfo() {
        for (var i = 0; i < this.returnBookList.length; i++) {
          let submit = {
            "id": window.localStorage.getItem('id'),
            "token": window.localStorage.getItem('token'),
            "name": "no",
            "isbn": "no",
            "bookId": this.returnBookList[i].bookId,
            "author": "no",
            "type": "no",
            "brokenType": "no",
            "borrowType": "no"
          }
          await this.$http.post(this.baseUrl + '/bookSearch', qs.stringify(submit),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res2 => {
              console.log(res2.data.result[0].tBookInfo.bookName)

              var date = new Date(parseInt(this.returnBookList[i].borrowEndTime))
              this.returnBookList[i].borrowEndTime = date.getFullYear() + '-' + (date.getMonth()+1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes())

              this.dict["borrow"] = this.returnBookList[i]
              this.dict["bookName"] = res2.data.result[0].tBookInfo.bookName
              this.dict["isbn"] = res2.data.result[0].tBookInfo.isbn
              this.dict["bookAuthor"] = res2.data.result[0].tBookInfo.bookAuthor
              this.returnResult.push(this.dict)
              console.log('2222' + this.returnResult[0].bookName)
              //this.returnBookList2[i] = res2.data.result[0]
            })
        }
        console.log('2233' + this.returnResult.length)
        this.returnBookList = this.returnResult
      },
      editUser(row) {
        this.userInfo = row
        this.form2Visible = true
      },
      async closeEditDialog() {
        this.editDialogVisible = false
        let submit = {
            "id": window.localStorage.getItem('id'),
            "token": window.localStorage.getItem('token'),
            "newId": "no",
            "name": this.userInfo.userName,
            "password": "no",
            "age": this.userInfo.age,
            "sex": this.userInfo.sex,
            "telephone": this.userInfo.telephone,
            "email": this.userInfo.email,
            "userId": this.userInfo.userId,
            "credit": this.userInfo.credit,
          }
          this.$http.post(this.baseUrl+'/userInfoChange', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
            console.log(res.data.result)
            if (res.data.state === 'fail') {
              this.$message.error({
                message: "用户信息修改失败"
              })
            } else {
              this.$message.success({
                message: "用户信息修改成功"
              })
              this.getUserList()
              this.form2Visible = false
            }
          }).catch(err => {
            if (err) {
              this.$message.error({
                message: '[CATCH]用户信息修改失败' + err,
                duration: 5000
              })
            }
          })
      },
      delUser(row) {
        this.$confirm('确定删除用户' + row.userId + '吗？', '警告').then(async () => {
          let submit = {
            "id": window.localStorage.getItem('id'),
            "token": window.localStorage.getItem('token'),
            "userId": row.userId
          }
          await this.$http.post(this.baseUrl + '/userInfoDelete', qs.stringify(submit),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
              if (res.data.state === 'fail') {
                this.$message.error({ message: '删除失败' })
              } else {
                this.$message.success({ message: '删除成功' })
                this.getUserList()
              }
            })
        }).catch((err) => {
          this.$message.info({message: '[CATCH]取消删除 ' + err})
        })
      },
      borrowConfirm(row) {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "bookId": row.borrow.bookId,
          "userId": row.borrow.userId,
        }
        this.$http.post(this.baseUrl+'/borrowCode', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取借阅码失败',
              duration: 1000
            })
          } else {
            this.borrowCode = res.data.result
            this.$confirm('请扫描用户的借阅码核对：' + res.data.result, '提示').then(
              this.returnConfirm(row)
            )
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取借阅码失败' + err})}) 
      },
      returnConfirm(row) {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": row.borrow.userId,
          "bookId": row.borrow.bookId,
          "type": row.borrow.type,
          "note": "no"
        }
        this.$http.post(this.baseUrl+'/admin/return', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '还书失败',
              duration: 1000
            })
          } else {
            this.$message.success({message:'还书成功！'})
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]还书失败' + err})})
      }
    },
    created() {
      this.getUserList()
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