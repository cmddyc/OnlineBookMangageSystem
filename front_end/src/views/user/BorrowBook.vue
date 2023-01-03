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
          <el-image style="width: 120px; height: 138px" :src="scope.row.tBookInfo.bookImg" :fit="fit" />
        </template>
      </el-table-column>
      <el-table-column label="书名" prop="tBookInfo.bookName" align="center"></el-table-column>
      <el-table-column label="作者" prop="tBookInfo.bookAuthor" align="center"></el-table-column>
      <el-table-column label="ISBN" prop="tBookInfo.isbn" align="center"></el-table-column>
      <el-table-column label="类别" prop="tBookInfo.bookTypes" align="center"></el-table-column>
      <el-table-column label="借阅状态" prop="tBookInfo.borrowType" align="center"></el-table-column>
      <!-- <el-table-column label="借阅状态" align="center">
      <el-tag type="success">可借阅</el-tag></el-table-column> -->
      
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <div class="user-ctrl">
            <el-link type="primary" @click="showBookInfo(scope.row)">查看详情</el-link>
            <div v-if="scope.row.tBookInfo.borrowType === '已借出'">
              <el-link type="primary" disabled @click="borrow(scope.row)">借阅</el-link>
            </div>
            <div v-else>
              <el-link type="primary" @click="borrow(scope.row, 'normal')">借阅</el-link>
            </div>
            <div>
              <el-link type="primary" @click="borrow(scope.row, 'ebook')">电子书</el-link>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title=formTitle :visible.sync="formVisible" width="60%">
      <el-descriptions title="图书详情" :column="1">
        <el-descriptions-item label="定价">{{bookInfo.price}}</el-descriptions-item>
        <el-descriptions-item label="电子书">
            <div v-if="bookInfo.ebook === 'no'">
              <el-tag type="info">无</el-tag>
            </div>
            <div v-else>
              <el-tag type="success">有</el-tag>
            </div>
        </el-descriptions-item>
        <el-descriptions-item label="描述">{{bookInfo.intro}}</el-descriptions-item>        
      </el-descriptions>
    </el-dialog>

    <!-- 借阅对话框 -->
    <!-- <el-dialog :title=formTitle :visible.sync="form2Visible" width="60%">
      <el-table :data="borrowList" height="400" border style="width: 100%">
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="bookId" label="书目编号" width="120">
        </el-table-column>
        <el-table-column prop="borrowType" label="借阅状态" width="120">
        </el-table-column>
        <el-table-column prop="brokenType" label="损坏情况" width="120">
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
    </el-dialog> -->
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
        bookName: '',
        bookInfo: {},
        bookList: [{
          bookName: ''
        }],
        borrowList: [{
          bookPrice: '6'
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
          "bookId": "no",
          "author": "no",
          "type": "no",
          "brokenType": "no",
          "borrowType": "no"
        }
        await this.$http.post(this.baseUrl+'/bookSearch', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
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

            var count = 0
            for (var j = 0; j < this.bookList.length; j++) {
              for (var i = 0; i < this.bookList[j].tBook.length; i++) {
                if (this.bookList[j].tBook[i].borrowType === 'finish') {
                  count++
                }
              }
              if (count) {
                this.bookList[j].tBookInfo.borrowType = '可借阅'
              }
              else {
                this.bookList[j].tBookInfo.borrowType = '已借出'
              }
            }

            console.log(this.bookList[0].tBookInfo.bookImg)
            console.log(this.bookList[0].tBook[0].bookId)
            this.loading = false
          }
        }).catch(err => {this.$message.error({message: '[CATCH]获取所有信息失败 ' + err})})        
      },
      showBookInfo(row) {
        this.formTitle = "《" + row.tBookInfo.bookName + "》"
        this.bookInfo.price = row.tBookInfo.bookPrice //test
        this.bookInfo.ebook = row.tBookInfo.ebook
        this.bookInfo.intro = row.tBookInfo.bookIntro

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
        this.formTitle = "《" + row.tBookInfo.bookName + "》的馆藏图书条目"
        this.borrowList = row.tBook
        this.form2Visible = true
      },
      async borrow(row,types) {
        this.$confirm('确定借阅', '提示').then(async () => {
          let submit = {
            "id": window.localStorage.getItem('id'),
            "token": window.localStorage.getItem('token'),
            "isbn": row.tBookInfo.isbn,
            "startTime": "",
            "endTime": "90",
            "note": "",
            "type": types,
          }
          await this.$http.post(this.baseUrl + '/borrow', qs.stringify(submit),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
              if (res.data.state === 'fail') {
                this.$message.error({ message: '借阅失败' })
              } else {
                this.$message.success({ message: '借阅成功' })
              }
            })
        }).catch((err) => {
          this.$message.info({message: '[CATCH]取消借阅 ' + err})
        })
      },
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