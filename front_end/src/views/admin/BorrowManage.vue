<template>
    <div>
      <div style="text-align:center;user-select:none;"><span class="mytitle">借阅管理</span></div>
      <!-- 管理员查询区 -->
      <el-row :gutter="20" style="margin:2rem 0 0 8rem">
        <el-col :span="18">
          <el-input @keydown.enter.native="findBook" placeholder="请输入查询的图书" prefix-icon="el-icon-search"
            v-model="bookName">
            <el-button type="primary" slot="append" icon="el-icon-search" @click="findBook"></el-button>
          </el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="showAddBook">添加书本</el-button>
        </el-col>
      </el-row>
      <!-- 列表显示区域 -->
      <el-table class="book-table" :data="bookList" style="width: 100%;">
        <el-table-column type="index" align="center" label="#"></el-table-column>
        <el-table-column label="书名" align="center" prop="book_name"></el-table-column>
        <el-table-column label="图书编号" align="center" prop="book_id"></el-table-column>
        <el-table-column label="作者" align="center" prop="author"></el-table-column>
        <el-table-column label="状态" align="center" prop="state"></el-table-column>
      </el-table>
      <!-- 书本添加区 -->
      <div class="addBook-box" v-if="isAddBook">
        <el-form label-position="left" label-width="80px" :model="formLabelAlign">
          <el-form-item label="书名">
            <el-input v-model="formLabelAlign.addBookName"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="formLabelAlign.author"></el-input>
          </el-form-item>
          <el-form-item label="书本描述">
            <el-input v-model="formLabelAlign.statement"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button plain @click="addBook">确认添加</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </template>
  
  <script>
    export default {
      data() {
        return {
          bookName: '',
          bookList: [],
          formLabelAlign: {
            addBookName: '',
            author: '',
            statement: ''
          },
          isAddBook: false
        }
      },
      methods: {
        async findBook() {
          const res = await this.$http.get(this.baseUrl+`/adminGetBooks?book_name=${this.bookName}`)
          this.bookList = res.data.bookList
        },
        async showAddBook() {
          this.isAddBook = true
        },
        async addBook() {
          let submit = {
            book_name: this.formLabelAlign.addBookName,
            author: this.formLabelAlign.author,
            statement: this.formLabelAlign.statement
          }
          const res = await this.$http.post(this.baseUrl+'/adminAddBook', submit)
          if (res.status === 200) {
            this.$message.success({
                message: '添加成功！',
                duration: 500
              }),
            this.formLabelAlign.addBookName = ''
            this.formLabelAlign.author = ''
            this.formLabelAlign.statement = ''
            this.hideAddBook()
          } else {
            this.$message.error({
              message: '添加失败，请稍后再试',
              duration: 500
            })
          }
        },
        async hideAddBook() {
          this.isAddBook = false
        }
      },
      created() {
  
      }
    }
</script>