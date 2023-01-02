<template>
    <div class="pdf" v-show="fileType === 'pdf'">
      <pdf
        :src="src"
        v-for="i in pageCount"
        :key="i"
        :page="i"
      >
      </pdf>
    </div>
  </template>


<script>
    import pdf from 'vue-pdf'
    export default {
        components: {pdf},
        data () {
            return {
                currentPage: 1, // pdf文件页码
                pageCount: 4, // pdf文件总页数
                fileType: 'pdf', // 文件类型
                src:'/1.pdf',// pdf文件地址
            }
        },
        created() {
            // 有时PDF文件地址会出现跨域的情况,这里最好处理一下
            this.src= pdf.createLoadingTask({url:this.src})
        },
        method: {
        }
    }

</script>