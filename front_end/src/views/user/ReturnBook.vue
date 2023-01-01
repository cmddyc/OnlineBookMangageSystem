<template>
  <div>
    <div style="text-align:center;"><span class="mytitle">在线图书系统</span></div>
    <!-- 空状态显示区 -->
    <el-empty description="暂时还没有图书" v-if="!isFind" :image-size="200" style="margin-top: 6.5rem;user-select:none;"></el-empty>
    <!-- 还书列表显示区 -->
    <el-table class="book-table" :data="returnBookList" v-if="isFind" v-loading="loading" style="width: 100%;">
      <el-table-column type="index" align="center" label="#"></el-table-column>
      <el-table-column label="书名" align="center" prop="book_name"></el-table-column>
      <el-table-column label="图书编号" align="center" prop="book_id"></el-table-column>
      <el-table-column label="作者" align="center" prop="book_author"></el-table-column>
      <el-table-column label="还书时间" align="center" prop="end_time"></el-table-column>
      <el-table-column label="借阅状态" align="center" prop="borrow_type"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <div class="user-ctrl">
            <el-link type="success" @click="comment(scope.row)">添加书评</el-link>
            <el-link type="primary" @click="ebookRead(scope.row)">电子书阅览</el-link>
            <el-link type="danger" @click="returnMyBook(scope.row)">还书</el-link>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div style="space">
      <el-link type="primary" @click="searchBorrowHistory">历史借阅记录查询</el-link>
    </div> 
    
    <!-- 添加、查看书评 -->
    <el-dialog :title=formTitle :visible.sync="formVisible" width="60%">
      <span>
        <el-form :model="bookComment" ref="bookComment" :rules="editFormRules" label-width="70px">
          <el-form-item label="书评内容">
            <el-input v-model="bookComment"></el-input>
          </el-form-item>
        </el-form>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">返 回</el-button>
        <el-button type="primary" @click="postComment()">提 交</el-button>
      </span>
    </el-dialog>

    <!-- 还书 -->
    <el-dialog :title=formTitle :visible.sync="form2Visible" width="50%">
      <el-descriptions title="请向管理员出示此借阅码！" :column="1">
        <el-descriptions-item label="图书借阅码">{{borrowCode}}</el-descriptions-item>     
      </el-descriptions>
    </el-dialog>

    <!-- 历史借阅记录查询 -->
    <el-dialog title="历史借阅记录查询" :visible.sync="form3Visible" width="60%">
      <span>
        <el-table :data="historyList" height="400" border style="width: 100%">
          <el-table-column type="index" align="center" label="#"></el-table-column>
          <el-table-column label="书名" align="center" prop="book_name"></el-table-column>
          <el-table-column label="图书编号" align="center" prop="book_id"></el-table-column>
          <el-table-column label="作者" align="center" prop="book_author"></el-table-column>
          <el-table-column label="借书时间" align="center" prop="start_time"></el-table-column>
          <el-table-column label="还书时间" align="center" prop="end_time"></el-table-column>
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
        returnBookList: [],
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
          "user_id": "no",
          "book_id": "no",
          "type": "normal" + "outtime"
        }
        await this.$http.post(this.baseUrl+'/borrowSearch', submit).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取用户图书列表失败',
              duration: 1000
            })
          } else {
            this.returnBookList = res.data.result
            if (this.returnBookList.length !== 0) {
              this.isFind = true
              this.loading = false
            }
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取用户图书列表失败'})})        
      },
      async comment(row, willEdit = true) {
        if (willEdit)
          this.formTitle = "添加书评：《" + row.book_name + "》"
        else
          this.formTitle = "您的书评：《" + row.book_name + "》"
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "user_id": "no",
          "isbn": row.isbn,
          "start_time": "no",
          "end_time": "no"
        }
        await this.$http.post(this.baseUrl+'/bookCommentSearch', submit).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取书评失败',
              duration: 1000
            })
          } else {
            this.bookComment = res.data.result.TBookComment[0].comment
            this.isbn = row.isbn
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取书评失败'})})

        this.formVisible = true
      },
      async postComment() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "isbn": this.isbn,
          "comment": this.bookComment
        }
        await this.$http.post(this.baseUrl+'/user/bookComment', submit).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '书评失败',
              duration: 1000
            })
          } else {
            this.ebook = res.data.result.file
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]书评失败'})}) 
      },
      async ebookRead(row) {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "book_id": row.book_id
        }
        await this.$http.post(this.baseUrl+'/eBookRead', submit).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取电子书失败',
              duration: 1000
            })
          } else {
            this.ebook = res.data.result.file
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取电子书失败'})}) 
      },
      async returnMyBook(row) {
        this.formTitle = "归还《" +row.book_name+ "》"
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "book_id": row.book_id,
          "start_time": "no"
        }
        await this.$http.post(this.baseUrl+'/borrowCode', submit).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取借阅码失败',
              duration: 1000
            })
          } else {
            this.borrowCode = res.data.result.code
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取借阅码失败'})}) 

        form2Visible = true
      },
      async getReturnBookList() {
        const res = await this.$http.get(this.baseUrl+`/getHasBooks?id=${this.id}`)
        this.returnBookList = res.data.bookList
        if(this.returnBookList.length !== 0 ) {
          this.isFind = true
          this.loading = false
        }
      },
      async searchBorrowHistory() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "user_id": "no",
          "book_id": "no",
          "type": "finish"
        }
        await this.$http.post(this.baseUrl+'/borrowSearch', submit).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
              message: '获取历史借阅信息失败',
              duration: 1000
            })
          } else {
            this.historyList = res.data.result
          }
        })
        .catch(err => {this.$message.error({message: '[CATCH]获取历史借阅信息失败'})}) 

        form3Visible = true
      }
    },
    created() {
      this.getMyBooklist()
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