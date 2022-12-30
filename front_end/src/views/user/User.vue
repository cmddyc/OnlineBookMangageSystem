<template>
  <div>
    <!-- <div style="text-align:center;"><span class="gray-title">在线图书系统</span></div>
    <el-empty description="快去借本书吧~" v-if="!isFind" :image-size="200" style="margin-top: 6.5rem;user-select:none;"></el-empty>
    等待审核列表 -->
    <el-button type="primary">添加反馈</el-button>
    <el-table class="book-table" :data="waitBookList" v-if="isFind" v-loading="loading" style="width: 100%;">
      <el-table-column type="index" align="center" label="#"></el-table-column>
      <el-table-column label="反馈时间" width="120" align="center" prop="book_name"></el-table-column>
      <el-table-column label="反馈内容" align="center" prop="book_id"></el-table-column>
      <el-table-column label="状态" width="80" align="center"><el-tag type="info">审核中</el-tag></el-table-column>
      <el-table-column label="操作" width="80" align="center">
        <el-link type="danger" >删除</el-link>
      </el-table-column>
    </el-table>
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
    data() {
      return {
        waitBookList: [],
        loading:true,
        isFind:false
      }
    },
    computed:{
      ...mapState('userAbout', ['id'])
    },
    methods: {
      async getWaitBooks() {
        const res = await this.$http.get(this.baseUrl+`/getOnCheckBooks?id=${this.id}`)
        this.waitBookList = res.data.bookList
        const res2 = await this.$http.get(this.baseUrl+`/getHasBooks?id=${this.id}`)
        this.waitBookList.push(...res2.data.bookList)
        for(let i=0;i<this.waitBookList.length;i++) {
          if(this.waitBookList[i].due === '') {
            this.waitBookList[i].due = '审核中'
          }
        }
        if(this.waitBookList.length !== 0) {
          this.loading = false,
          this.isFind = true
        }
      }
    },
    created() {
      this.getWaitBooks()
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