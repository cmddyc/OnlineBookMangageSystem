<template>
  <div>
    <div style="text-align:center;"><span class="mytitle">在线图书系统</span></div>
    <!-- 用户查询区 -->
    <el-row :gutter="15" style="margin:2rem 2rem 0 2rem">
      <el-col :span="21">
        <el-input @keydown.enter.native="findBook" placeholder="请输入查询的图书" prefix-icon="el-icon-search"
          v-model="bookName">
        </el-input>
      </el-col>
      <el-col :span="3">
        <el-button type='primary' @click="findBook">查询图书</el-button>
      </el-col>
    </el-row>
    <!-- 空状态显示区 -->
    <el-empty v-if="!isFind" :image-size="200" style="margin-top: 2rem;user-select:none;"></el-empty>
    <!-- 结果显示区 -->
    <el-table class="book-table" :data="bookList" v-if="isFind" border stripe v-loading="loading" style="width: 100%;">
      <el-table-column type="index" label="#" align="center"></el-table-column>
      <el-table-column label="缩略图" align="center" width="150">
        <!-- style="height:100px;width=30%" :fit="fit" -->
        <template v-slot:default="scope">
          <!-- <img :src="scope.row.book_img" width="120" height="138" /> -->
          <el-image style="width: 120px; height: 138px" :src="require('../../assets/book/02.jpg')" :fit="fit" />
        </template>
      </el-table-column>
      <el-table-column label="书名" prop="book_name" align="center"></el-table-column>
      <el-table-column label="作者" prop="book_author" align="center"></el-table-column>
      <el-table-column label="ISBN" prop="isbn" align="center"></el-table-column>
      <el-table-column label="类别" prop="TBookInfo.book_types" align="center"></el-table-column> //test
      <!-- <el-table-column label="借阅状态" align="center">
      <el-tag type="success">可借阅</el-tag></el-table-column> -->
      
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <div class="user-ctrl">
            <el-link type="primary" @click="showBookInfo(scope.row)">查看详情</el-link>
            <el-link type="primary" @click="showBorrow(scope.row)">借阅</el-link>
            <!-- <el-link type="primary" @click="borrowBook(scope.row)">预订</el-link> -->
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title=formTitle :visible.sync="formVisible" width="60%">
      <el-descriptions title="图书详情" :column="1">
        <el-descriptions-item label="定价">{{bookInfo.price}}</el-descriptions-item>
        <el-descriptions-item label="电子书">{{bookInfo.ebook}}</el-descriptions-item>
        <el-descriptions-item label="描述">{{bookInfo.intro}}</el-descriptions-item>        
      </el-descriptions>
    </el-dialog>

    <!-- 借阅对话框 -->
    <el-dialog :title=formTitle :visible.sync="form2Visible" width="60%">
      <el-table :data="borrowList" height="400" border style="width: 100%">
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="book_id" label="书目编号" width="120">
        </el-table-column>
        <el-table-column prop="borrow_type" label="借阅状态" width="120">
        </el-table-column>
        <el-table-column prop="broken_type" label="损坏情况" width="120">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <div v-if="scope.row.borrow_type === '已借出'">
              <el-link type="primary" @click="book(scope.row)">预定</el-link>
            </div>
            <div v-else>
              <el-link type="primary" @click="borrow(scope.row)">借阅</el-link>
            </div>
            <h7>&nbsp &nbsp</h7>
          </template>
        </el-table-column>
      </el-table>
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
        bookName: '',
        bookInfo: {},
        bookList: [{
          book_name: ''
        }],
        borrowList: [{
          book_price: '6'
        }],
        isFind: false,
        loading: true,
        formTitle: 'form',
        formVisible: false,
        form2Visible: false,
      }
    },
    computed:{
      ...mapState('userAbout', ['id'])
    },
    methods: {
      async findBook() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token'),
          "name": this.bookName,
          "isbn": "no",
          "book_id": "no",
          "author": "no",
          "type": "no",
          "broken_type": "no",
          "borrow_type": "no"
        }
        this.$http.post(this.baseUrl + '/bookSearch', submit).then(res => {
          if (res.data.state === "fail") {
            this.$message.error({
              message: '图书信息获取失败',
              duration: 1500
            })
          } else {
            if (res.data.result.length === 0) {
              return this.isFind = false
            }
            this.isFind = true
            this.bookList = res.data.result
            this.loading = false
          }
        })
      },
      showBookInfo(row) {
        this.formTitle = "《" + row.book_name + "》"
        this.bookInfo.price = row.tBookInfo.book_price //test
        this.bookInfo.ebook = row.e_book
        this.bookInfo.intro = row.book_intro

        this.formVisible = true       
      },
      // async showBookInfo(row) {
      //   const res = await this.$http.get(this.baseUrl+`/getBookInfo?book_id=${row.book_id}`)
      //   if (res.data.book_info === null || res.data.book_info.trim().length === 0) {
      //     res.data.book_info = '暂无详情信息'
      //   }
      //   this.$alert(`${res.data.book_info}`, '书本详情（包含图片、定价、是否电子书）')
      // },
      showBorrow(row) {
        this.formTitle = "《" + row.tBookInfo.book_name + "》的馆藏图书条目"
        this.borrowList = row.tBook
        this.form2Visible = true
      },
      async borrow(row) {
        this.$confirm('确定借阅', '提示').then(async () => {
          let submit = {
            "id": window.localStorage.getItem('id'),
            "token": window.localStorage.getItem('token'),
            "book_id": row.book_id 
          }
          const res = await this.$http.post(this.baseUrl+'/user/borrow', submit)
          if (res.data.state === 'fail') {
            this.$message.error({message: '借阅失败'})
          } else {
            this.$message.success({message: '借阅成功'})
          }
        }).catch(() => {
          this.$message.info({message: '[CATCH]取消借阅'})
        })
      },
      async borrowBook(row) {
        this.$confirm(`确定要借阅这本 ${row.author} 的 ${row.book_name} 吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(async () => {
          let submit = {
            book_id: row.book_id,
            id:this.id
          }
          const res = await this.$http.post(this.baseUrl+`/submitBorrowReq`, submit)
          if (res.status === 200) {
            this.$message.success({
              message: '请求提交成功',
              duration: 1000
            })
            setTimeout(() => {
            this.reload()
          }, 500)
            return 
          } else {
            return this.$message.error({
              message: '请求失败,请稍后再试',
              duration: 1000
            })
          }
        }).catch(() => {
          this.$message.info({
            message: '已取消',
            duration: 1000
          })
        })

      }
    },
    created() {
      this.findBook()
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