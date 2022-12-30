<template>
  <div>
    <div style="text-align:center;"><span class="gray-title">在线图书系统</span></div>
    <!-- 用户查询区 -->
    <el-row :gutter="15" style="margin:2rem 2rem 0 2rem">
      <el-col :span="21">
        <el-input @keydown.enter.native="findBook" placeholder="请输入查询的图书" prefix-icon="el-icon-search"
          v-model="bookName">
        </el-input>
      </el-col>
      <el-col :span="3">
        <el-button plain @click="findBook">点我查询</el-button>
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
      <el-table-column label="作者" prop="author" align="center"></el-table-column>
      <el-table-column label="ISBN" align="center">9787532139491</el-table-column>
      <el-table-column label="类别" align="center">文学</el-table-column>
      <el-table-column label="借阅状态" align="center">
      <el-tag type="success">可借阅</el-tag></el-table-column>
      
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <div class="user-ctrl">
            <el-link type="primary" @click="showBookInfo(scope.row)">查看详情</el-link>
            <el-link type="primary" @click="showw(scope.row)">借阅</el-link>
            <!-- <el-link type="primary" @click="borrowBook(scope.row)">预订</el-link> -->
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title=formTitle :visible.sync="formVisible" width="60%">
      <el-descriptions title="XX图书" :column="1">
        <el-descriptions-item label="定价">36</el-descriptions-item>
        <el-descriptions-item label="电子书">是</el-descriptions-item>
        <el-descriptions-item label="描述">苏州市江苏省苏州市吴中区吴中大道江苏省苏州市吴中区吴中大道江苏省苏州市吴中区吴中大道江苏省苏州市吴中区吴中大道江苏省苏州市吴中区吴中大道</el-descriptions-item>
        
      </el-descriptions>
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
        bookList: [],
        isFind: false,
        loading: true,
        formTitle: 'TEST',
        formVisible: false
      }
    },
    computed:{
      ...mapState('userAbout', ['id'])
    },
    methods: {
      async findBook() {
        // if (this.bookName.trim().length === 0) {
        //   return this.$message.info({
        //     message: '请输入你要查询的图书',
        //     duration: 1500
        //   })
        // }
        let submit = this.bookName
        const res = await this.$http.get(this.baseUrl+`/findBook?name=${submit}`)
        if (res.data.length === 0) {
          return this.isFind = false
        }
        this.isFind = true
        this.bookList = res.data
        this.loading = false
      },
      showw(row) {
        this.formTitle = row.book_name
        this.formVisible = true       
      },
      async showBookInfo(row) {
        const res = await this.$http.get(this.baseUrl+`/getBookInfo?book_id=${row.book_id}`)
        if (res.data.book_info === null || res.data.book_info.trim().length === 0) {
          res.data.book_info = '暂无详情信息'
        }
        this.$alert(`${res.data.book_info}`, '书本详情（包含图片、定价、是否电子书）')
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
    }
  }
</script>

<style scoped>
  .gray-title {
    position: relative;
    user-select: none;
    color: darkgray;
    text-shadow: 5px 5px 5px gray;
    font-size: 2rem;
    padding: 1rem 0 1rem 0;
    line-height: 4rem;
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