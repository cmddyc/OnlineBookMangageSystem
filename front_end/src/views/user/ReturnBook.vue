<template>
  <div>
    <!-- <div class="qrcode" ref="qrCodeUrl"></div> -->
    <div style="text-align:center;"><span class="mytitle">在线图书系统</span></div>
    <!-- 空状态显示区 -->
    <el-empty description="快去借本书吧~" v-if="!isFind" :image-size="200" style="margin-top: 6.5rem;user-select:none;"></el-empty>
    <!-- 还书列表显示区 -->
    <el-table class="book-table" :data="returnBookList" v-if="isFind" v-loading="loading" style="width: 100%;">
      <el-table-column type="index" align="center" label="#"></el-table-column>
      <el-table-column label="书名" align="center" prop="bookName"></el-table-column>
      <el-table-column label="图书编号" align="center" prop="borrow.bookId"></el-table-column>
      <el-table-column label="作者" align="center" prop="bookAuthor"></el-table-column>
      <el-table-column label="还书时间" align="center" prop="borrow.borrowEndTime"></el-table-column>
      <el-table-column label="isbn" align="center" prop="isbn"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <div class="user-ctrl">
            <el-link type="success" @click="comment(scope.row)">书评</el-link>
            <el-link type="primary" @click="ebookRead(scope.row)">电子书阅览</el-link>
            <el-link type="danger" @click="returnMyBook(scope.row)">还书</el-link>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div style="space">
      <el-link type="primary" @click="searchBorrowHistory()">历史借阅记录查询</el-link>
    </div> 
    
    <!-- 添加、查看书评 -->
    <el-dialog :title=formTitle :visible.sync="formVisible" width="60%">
      <span>
        <el-form :model="bookComment" ref="bookComment" :rules="editFormRules" label-width="70px">
          <el-form-item label="书评内容">
            <el-input type="textarea" :autosize="{ minRows: 3 }" v-model="bookComment"></el-input>
          </el-form-item>
        </el-form>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">返 回</el-button>
        <el-button type="primary" @click="postComment()">提 交</el-button>
      </span>
    </el-dialog>

    <!-- 还书 -->
    <el-dialog :title=formTitle @close="closeCode" :visible.sync="form2Visible" width="50%">
      <!-- <vue-qr text="https://blog.csdn.net/weixin_42601136" :size="200"></vue-qr> -->
      <el-descriptions title="请向管理员出示此借阅码！" :column="1">
        <el-descriptions-item label="图书借阅码">{{borrowCode}}
          <vue-qr text="https://blog.csdn.net/weixin_42601136" :size="200"></vue-qr>
        </el-descriptions-item>     
      </el-descriptions>
      <div class="qrcode" ref="qrCodeUrl"></div>
    </el-dialog>

    <!-- 历史借阅记录查询 -->
    <el-dialog title="历史借阅记录查询" :visible.sync="form3Visible" width="60%">
      <span>
        <el-table :data="historyList" height="400" border style="width: 100%">
          <el-table-column type="index" align="center" label="#"></el-table-column>
          <el-table-column label="书名" align="center" prop="book_name"></el-table-column>
          <el-table-column label="图书编号" align="center" prop="book_id"></el-table-column>
          <el-table-column label="作者" align="center" prop="book_author"></el-table-column>
          <el-table-column label="借书时间" align="center" prop="startTime"></el-table-column>
          <el-table-column label="还书时间" align="center" prop="endTime"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <div class="user-ctrl">
                <el-link type="primary" @click="comment(scope.row, false)">查看书评</el-link>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import qs from 'qs'
  import QRCode from 'qrcodejs2'
  //import vueQr from 'vue-qr'
  import {
    mapState,
    mapMutations,
    mapGetters,
    mapActions
  } from 'vuex'
  export default {
    inject: ['reload'],
    components: {
      QRCode
    },
    data() {
      return {
        returnBookList: [{
          bookName: '',
        }],
        //returnBookList2: [],

        returnResult: [],
        dict: {},

        historyList: [],
        ebook:{},
        formTitle: '',
        bookComment: '',
        isbn: '',
        borrowCode: '',
        formVisible: false,
        form2Visible: false,
        form3Visible: false,
        isFind:false,
        loading:true
      }
    },
    computed: {
      ...mapState('userAbout', ['id'])
    },
    methods: {
      async getMyBooklist() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": "no",
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
            console.log('1 ' + res.data.result[1].bookId)
            console.log('0 ' + res.data.result[0].bookId)
            this.returnBookList = res.data.result

            this.handleInfo()           
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取用户图书列表失败 ' + err})})        
      },
      async handleInfo() {
        for (var i = 0; i < this.returnBookList.length; i++) {
          console.log('00 ' + this.returnBookList[0].bookId)
          console.log('11 ' + this.returnBookList[1].bookId)
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

              var dict={}
              dict["borrow"] = this.returnBookList[i]
              dict["bookName"] = res2.data.result[0].tBookInfo.bookName
              dict["isbn"] = res2.data.result[0].tBookInfo.isbn
              dict["bookAuthor"] = res2.data.result[0].tBookInfo.bookAuthor
              this.returnResult.push(dict)
              console.log('2222' + this.returnResult[0].bookName)
              //this.returnBookList2[i] = res2.data.result[0]
            })
        }
        console.log('2233' + this.returnResult.length)
        this.returnBookList = this.returnResult

        if (this.returnResult.length !== 0) {
          this.isFind = true
          this.loading = false
        }
      },
      async comment(row, willEdit = true) {
        this.isbn = row.isbn
        
        if (willEdit)
          this.formTitle = "书评：《" + row.bookName + "》"
        else
          this.formTitle = "您的书评：《" + row.bookName + "》"
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": window.localStorage.getItem('id'),
          "isbn": row.isbn,
          "startTime": Date.parse("2022/12/1 10:00"),
          "endTime": Date.parse(new Date()) 
        }
        await this.$http.post(this.baseUrl+'/bookCommentSearch', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取书评失败',
              duration: 1000
            })
          } else {
            this.bookComment = res.data.result[0].comment
            this.isbn = row.isbn
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取书评失败' + err})})

        this.formVisible = true
      },
      async postComment() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "isbn": this.isbn,
          "comment": this.bookComment
        }
        await this.$http.post(this.baseUrl+'/user/bookComment', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '书评失败',
              duration: 1000
            })
          } else { 
            this.$message.success({message: '书评成功'})
            this.formVisible = false
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]书评失败' + err})}) 
      },
      async ebookRead(row) {
        this.$router.push('/ebookView')
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "bookId": row.borrow.bookId,
          "startTime": row.borrow.startTime
        }
        await this.$http.post(this.baseUrl+'/eBookRead', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取电子书失败',
              duration: 1000
            })
          } else {
            this.ebook = res.data.result.file
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取电子书失败' + err})}) 
      },
      returnMyBook(row) {
        this.formTitle = "归还《" +row.bookName+ "》"
        console.log(row.borrow.bookId)
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "bookId": row.borrow.bookId,
          "userId": "no",
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
            this.createQrCode()
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取借阅码失败' + err})}) 

        this.form2Visible = true
      },
      async searchBorrowHistory() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "userId": "no",
          "bookId": "no",
          "type": "finish"
        }
        await this.$http.post(this.baseUrl+'/borrowSearch', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取历史借阅信息失败',
              duration: 1000
            })
          } else {
            this.historyList = res.data.result
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取历史借阅信息失败' + err})}) 

        this.form3Visible = true
      },
      createQrCode() {
        let qrcode = new QRCode(this.$refs.qrCodeUrl, {
            text: this.borrowCode, // 需要转换为二维码的内容
            width: 100,
            height: 100,
            colorDark: '#000000',
            colorLight: '#ffffff',
            correctLevel: QRCode.CorrectLevel.H
        })
      },
      closeCode() {
        this.$refs.qrCodeUrl.innerHTML = ''
      }
    },
    created() {
      this.getMyBooklist()
    },
    mounted() {
      //this.createQrCode()
    }
  }
</script>

<style scoped>

  .book-table {
    margin-top: 2rem;
    border-radius: 0.5rem;
  }

  .user-ctrl {
    display: flex;
    justify-content: space-around;
  }
</style>