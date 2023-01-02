<template>
    <div>
        <div style="text-align:center;user-select:none;"><span class="mytitle">书本管理</span></div>
        <!-- 管理员查询区 -->
        <el-row :gutter="20" style="margin:2rem 0 0 8rem">
            <el-col :span="18">
                <el-input @keydown.enter.native="findBook" placeholder="请输入查询的图书" prefix-icon="el-icon-search"
                    v-model="bookName">
                    <el-button type="primary" slot="append" icon="el-icon-search" @click="findBook"></el-button>
                </el-input>
            </el-col>
            <el-col :span="2">
                <el-button type="primary" plain @click="addBookInfo()">添加书本</el-button>
            </el-col>
        </el-row>
        <!-- 列表显示区域 -->
        <el-table class="book-table" :data="bookList" style="width: 100%;">
            <el-table-column type="index" align="center" label="#"></el-table-column>
            <el-table-column label="缩略图" align="center" width="140">
                <!-- style="height:100px;width=30%" :fit="fit" -->
                <template v-slot:default="scope">
                    <!-- <img :src="scope.row.book_img" width="120" height="138" /> -->
                    <el-image style="width: 120px; height: 138px" :src="scope.row.tBookInfo.bookImg" :fit="fit" />
                </template>
            </el-table-column>
            <el-table-column label="书名" align="center" prop="tBookInfo.bookName" width="200">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.tBookInfo.bookName }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.tBookInfo.bookName"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="作者" align="center" prop="tBookInfo.bookAuthor" width="150">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.tBookInfo.bookAuthor }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.tBookInfo.bookAuthor"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="ISBN" align="center" prop="tBookInfo.isbn" width="200">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.tBookInfo.isbn }}</div>
                    <div v-else>
                        <el-input v-model="newIsbn"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="类别" align="center" prop="bookTypes">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.tBookInfo.bookTypes }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.tBookInfo.bookTypes"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="定价" align="center" prop="bookPrice">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.tBookInfo.bookPrice }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.tBookInfo.bookPrice"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="电子书" align="center" prop="ebook" width="150">
                <template slot-scope="scope">
                      <div v-if="scope.row.tBookInfo.ebook === 'no'">
                        <el-tag type="info">无</el-tag>
                      </div>
                      <div v-else>
                        <el-tag type="success">有</el-tag>
                      </div>
                </template>
            </el-table-column>
            <!-- <el-table-column label="图片路径" align="center" prop="bookImg" width="150">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.tBookInfo.bookImg }}</div>
                    <div v-else>
                        <el-input type="textarea" autosize v-model="scope.row.tBookInfo.bookImg"></el-input>
                    </div>
                </template>
            </el-table-column> -->
            <el-table-column label="简介" align="center" prop="bookIntro" width="400">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.tBookInfo.bookIntro }}</div>
                    <div v-else>
                        <el-input type="textarea" autosize v-model="scope.row.tBookInfo.bookIntro"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="120">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">
                        <el-link type="warning" @click="editBookInfo(scope.row)">编辑</el-link>
                    </div>
                    <div v-else>
                        <el-link type="success" @click="editBookInfo(scope.row)">完成</el-link>
                    </div>
                    <!-- <el-link type="danger" @click="editBookInfo(scope.row)">{{ scope.row.isEdit ? '完成' : '编辑' }}</el-link> -->
                    <!-- <h7>&nbsp &nbsp</h7> -->
                    <el-link type="primary" @click="showBookList(scope.row)">管理</el-link>
                    <div>
                    <el-link type="danger" @click="delBookInfo(scope.row.tBookInfo)">删除</el-link></div>
                </template>
            </el-table-column>
        </el-table>

        <!-- 查看本书馆藏条目 -->
        <el-dialog :title=dialogTitle :visible.sync="bookListVisible" width="70%">
            <el-row style="margin:0 0 1rem 0">
                <el-button type="primary" @click="addBookList(bookList2[0].isbn)">新增本书馆藏条目</el-button>
            </el-row>
            <el-table :data="bookList2" height="400" border style="width: 100%">
                <el-table-column type="index" label="#"></el-table-column>
                <el-table-column prop="isbn" label="ISBN" width="120">
                  <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit2">{{ scope.row.isbn }}</div>
                    <div v-else>
                        <el-input type="textarea" autosize v-model="scope.row.isbn"></el-input>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="bookId" label="书目ID" width="120">
                </el-table-column>
                <el-table-column prop="borrowType" label="借阅状态" width="120">
                  <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit2">{{ scope.row.borrowType }}</div>
                    <div v-else>
                        <el-input type="textarea" autosize v-model="scope.row.borrowType"></el-input>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="brokenType" label="损坏情况" width="120">
                  <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit2">{{ scope.row.brokenType }}</div>
                    <div v-else>
                        <el-input type="textarea" autosize v-model="scope.row.brokenType"></el-input>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <div v-if="!scope.row.isEdit2">
                            <el-link type="warning" @click="editBookList(scope.row)">编辑</el-link>
                        </div>
                        <div v-else>
                            <el-link type="success" @click="editBookList(scope.row)">完成</el-link>
                        </div>
                        <el-link type="danger" @click="delBookList(scope.row)">删除</el-link>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
        
        <!-- Add Book List 新增馆藏图书条目 -->
        <el-dialog :title="添加图书" :visible.sync="addBookVisible" width="60%">
            <span>
                <el-form :model="newBook" ref="newBook" label-width="70px">
                    <el-form-item label="书名">
                        <el-input v-model="bookName" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="书目ID">
                        <el-input v-model="newBookInfo.bookId"></el-input>
                    </el-form-item>
                </el-form>
            </span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addBookVisible = false">取 消</el-button>
                <el-button type="primary" @click="closeAddBook()">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 新增新书 -->
        <el-dialog title="添加图书" :visible.sync="addBookInfoVisible" width="60%">
            <span>
                <el-form :model="newBookInfo" ref="newBook" label-width="70px">
                    <el-row :gutter="20">
                     <el-col :span="5"><el-upload
                        class = "left"
                        ref="upload"
                        span="2"
                        :auto-upload="false"
                        :on-change="onUploadChange"
                        :action="url"
                        multiple
                        :limit="3"
                        :file-list="files"
                        >
                        <el-button size="small" type="primary">上传缩略图</el-button>
                     </el-upload>
                     </el-col>
                    </el-row>                    
                    
                    <el-form-item label="书名">
                        <el-input v-model="newBookInfo.bookName"></el-input>
                    </el-form-item>
                    <el-form-item label="ISBN">
                        <el-input v-model="newBookInfo.isbn"></el-input>
                    </el-form-item>
                    <el-form-item label="书目编号">
                        <el-input v-model="newBookInfo.bookId"></el-input>
                    </el-form-item>
                    <el-form-item label="作者">
                        <el-input v-model="newBookInfo.bookAuthor"></el-input>
                    </el-form-item>
                    <el-form-item label="简介">
                        <el-input type="textarea" autosize v-model="newBookInfo.bookIntro"></el-input>
                    </el-form-item>                    
                    <el-form-item label="价格">
                        <el-input v-model="newBookInfo.bookPrice"></el-input>
                    </el-form-item>
                    <el-form-item label="类别">
                        <el-input v-model="newBookInfo.bookTypes"></el-input>
                    </el-form-item>
                    <el-row :gutter="20">
                     <el-col :span="5"><el-upload
                        ref="upload"
                        span="2"
                        :auto-upload="false"
                        :on-change="onUploadChange2"
                        :action="url"
                        multiple
                        :limit="3"
                        :file-list="files"
                        >
                        <el-button size="small" type="primary" plain>上传电子书</el-button>
                     </el-upload>
                     </el-col>
                    </el-row>
                </el-form>
            </span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addBookInfoVisible = false">取 消</el-button>
                <el-button type="primary" @click="closeAddBookInfo()">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import qs from 'qs'
export default {
    data() {
        return{
            bookName: '',
            bookData: [{
                bookName: '高等数学',
                bookAuthor: '同济大学',
                bookIntro: '好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好',
                ebook: '../AdvancedMath.pdf',
                isbn: '114514'
            }, {
                bookName: '高速退学',
                bookPrice: 'infinite',

            }],
            bookList: [], //书本品类信息
            bookList2: [], //馆藏条目
            newBook: {},
            newIsbn: '',
            newBookInfo: {
                bookImg: ''
            },
            bookListVisible: false,
            addBookVisible: false,
            addBookInfoVisible: false,
            dialogTitle: ''
        }
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
            await this.$http.post(this.baseUrl + '/bookSearch', qs.stringify(submit),
                { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
                    if (res.data.state === "fail") {
                        this.$message.error({
                            message: '图书信息获取失败',
                            duration: 1500
                        })
                    } else {
                        //this.$message.success({message: '图书查询成功'})
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
                }).catch(err => { this.$message.error({ message: '[CATCH]获取所有信息失败 ' + err }) })
        },
        showBookList(row) {
            this.dialogTitle = '馆藏的所有《' + row.tBookInfo.bookName + '》图书列表'
            this.bookName = row.tBookInfo.bookName
            this.bookListVisible = true
            this.bookList2 = row.tBook

        },
        async editBookInfo(row) {
            if (row.isEdit) { // 已在编辑
                row.isEdit = false
                let submit = {
                    "id": window.localStorage.getItem('id'),
                    "token": window.localStorage.getItem('token'),
                    "name": row.tBookInfo.bookName,
                    "isbn": row.tBookInfo.isbn,
                    "newIsbn": this.newIsbn,
                    "author": row.tBookInfo.bookAuthor,
                    "intro": row.tBookInfo.bookIntro,
                    "img": row.tBookInfo.bookImg,
                    "price": row.tBookInfo.bookPrice,
                    "type": row.tBookInfo.bookTypes,
                    "ebook": "no",
                }
                await this.$http.post(this.baseUrl + '/admin/changeBookInfo', qs.stringify(submit),
                    { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
                        if (res.data.state == 'fail') {
                            this.$message.error({
                                message: "图书信息修改失败"
                            })
                        } else {
                            this.$message.success({
                                message: "图书信息修改成功"
                            })
                            this.findBook()
                        }
                    }).catch(err => { this.$message.error({ message: '[CATCH]图书个人信息修改失败' + err }) })
            } else { // 想要编辑
                this.$set(row, 'isEdit', true)
                this.newIsbn = row.tBookInfo.isbn
            }
        },
        async editBookList(row) {
            if (row.isEdit2) { //编辑完成
                row.isEdit = false
                let submit = {
                    "id": window.localStorage.getItem('id'),
                    "token": window.localStorage.getItem('token'),
                    "isbn": row.isbn,
                    "bookId": row.bookId,
                    "newBookId": "no",
                    "brokenType": row.brokenType,
                    "borrowType": row.borrowType
                }
                await this.$http.post(this.baseUrl + '/admin/changeBook', qs.stringify(submit),
                    { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
                        if (res.data.state == 'fail') {
                            this.$message.error({
                                message: "图书信息修改失败"
                            })
                        } else {
                            this.$message.success({
                                message: "图书信息修改成功"
                            })
                            this.bookListVisible = false
                            this.bookName = ''
                            this.findBook()
                        }
                    }).catch(err => { this.$message.error({ message: '[CATCH]图书个人信息修改失败' + err }) })
            } else { // 想要编辑
                this.$set(row, 'isEdit2', true)
            }            
        },
        addBookInfo() {
            this.addBookInfoVisible = true
        },
        addBookList(isbn) {
            this.addBookVisible = true
            this.newBookInfo.isbn = isbn
        },
        delBookInfo(row) {
            this.$confirm('确定删除书目《' + row.bookName + '》吗？', '警告').then(async () => {
                let submit = {
                    "id": window.localStorage.getItem('id'),
                    "token": window.localStorage.getItem('token'),
                    "isbn": row.isbn
                }
                await this.$http.post(this.baseUrl + '/admin/deleteBookInfo', qs.stringify(submit),
                    { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
                        if (res.data.state === 'fail') {
                            this.$message.error({ message: '删除失败' })
                        } else {
                            this.$message.success({ message: '删除成功' })
                            location.reload()
                        }
                    })
            }).catch((err) => {
                this.$message.info({ message: '[CATCH]取消删除 ' + err })
            })
        },
        delBookList(row) {
            this.$confirm('确定删除编号为' + row.bookId + '的图书吗？', '警告').then(async () => {
                let submit = {
                    "id": window.localStorage.getItem('id'),
                    "token": window.localStorage.getItem('token'),
                    "bookId": row.bookId
                }
                await this.$http.post(this.baseUrl + '/admin/deleteBook', qs.stringify(submit),
                    { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
                        if (res.data.state === 'fail') {
                            this.$message.error({ message: '删除失败' })
                        } else {
                            this.$message.success({ message: '删除成功' })
                            location.reload()
                        }
                    })
            }).catch((err) => {
                this.$message.info({ message: '[CATCH]取消删除 ' + err })
            })
        },
        async closeAddBookInfo() {
            this.addBookInfoVisible = false
            console.log('222221' + this.newBookInfo.ebook)
            if (this.newBookInfo.ebook === undefined) {
                this.newBookInfo.ebook = 'no'
            }
            let submit = {
                "id": window.localStorage.getItem('id'),
                "token": window.localStorage.getItem('token'),
                "name": this.newBookInfo.bookName,
                "isbn": this.newBookInfo.isbn,
                "author": this.newBookInfo.bookAuthor,
                "type": this.newBookInfo.bookTypes,
                "ebook": this.newBookInfo.ebook,
                "price": this.newBookInfo.bookPrice,
                "intro": this.newBookInfo.bookIntro,
                "img": this.newBookInfo.bookImg
            }
            await this.$http.post(this.baseUrl + '/admin/addBookInfo', qs.stringify(submit),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
              if (res.data.state === 'fail') {
                this.$message.error({ message: '添加失败' })
              } else {
                //this.$message.success({ message: '添加成功' })
                location.reload()
              }
            }).catch((err) => {
                this.$message.info({ message: '[CATCH]添加失败 ' + err })
            })
            let submit2 = {
                "id": window.localStorage.getItem('id'),
                "token": window.localStorage.getItem('token'),
                "isbn": this.newBookInfo.isbn,
                "bookId": this.newBookInfo.bookId
            }
            this.$http.post(this.baseUrl + '/admin/addBook', qs.stringify(submit2),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
              if (res.data.state === 'fail') {
                this.$message.error({ message: '添加失败2' })
              } else {
                this.$message.success({ message: '添加成功!' })
              }
            }).catch((err) => {
                this.$message.info({ message: '[CATCH]添加失败2 ' + err })
            })

        },
        closeAddBook() {
            this.addBookVisible = false
            this.bookListVisible = false
            let submit2 = {
                "id": window.localStorage.getItem('id'),
                "token": window.localStorage.getItem('token'),
                "isbn": this.newBookInfo.isbn,
                "bookId": this.newBookInfo.bookId
            }
            this.$http.post(this.baseUrl + '/admin/addBook', qs.stringify(submit2),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }).then(res => {
              if (res.data.state === 'fail') {
                this.$message.error({ message: '添加失败2' })
              } else {
                this.$message.success({ message: '添加成功!' })
                location.reload()
              }
            }).catch((err) => {
                this.$message.info({ message: '[CATCH]添加失败2 ' + err })
            })
            this.newBookInfo = {}
        },
        onUploadChange(file) { // upload bookImg
            var th = this
            this.files = file
            this.resuleFile = file

            console.log("---file----->" + file)//
            var reader = new FileReader();
            reader.readAsDataURL(file.raw);
            reader.onload = function () {
                th.newBookInfo.bookImg = this.result//
                console.log(this.result)
                console.log(th.newBookInfo.bookImg)
            }

        },
        onUploadChange2(file) { // upload ebook
            var th = this
            this.files = file
            this.resuleFile = file

            console.log("---file----->" + file)//
            var reader = new FileReader();
            reader.readAsDataURL(file.raw);
            reader.onload = function () {
                th.newBookInfo.ebook = this.result//
            }

        },
        handle(row, column, cell, event) {}
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

.left {
    margin-bottom: 2rem;
}
</style>