<template>
    <div>
        <div style="text-align:center;user-select:none;"><span class="gray-title">书本管理</span></div>
        <!-- 管理员查询区 -->
        <el-row :gutter="20" style="margin:2rem 0 0 8rem">
            <el-col :span="18">
                <el-input @keydown.enter.native="findBook" placeholder="请输入查询的图书" prefix-icon="el-icon-search"
                    v-model="bookName">
                    <el-button type="primary" slot="append" icon="el-icon-search" @click="findBook"></el-button>
                </el-input>
            </el-col>
            <el-col :span="2">
                <el-button type="primary" plain @click="">添加书本</el-button>
            </el-col>
        </el-row>
        <!-- 列表显示区域 -->
        <el-table class="book-table" :data="bookData" style="width: 100%;">
            <el-table-column type="index" align="center" label="#"></el-table-column>
            <el-table-column label="缩略图" align="center" width="140">
                <!-- style="height:100px;width=30%" :fit="fit" -->
                <template v-slot:default="scope">
                    <!-- <img :src="scope.row.book_img" width="120" height="138" /> -->
                    <el-image style="width: 120px; height: 138px" :src="require('../../assets/book/01.jpeg')" :fit="fit" />
                </template>
            </el-table-column>
            <el-table-column label="书名" align="center" prop="book_name" width="200">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.book_name }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.book_name"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="作者" align="center" prop="book_author" width="150">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.book_author }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.book_author"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="ISBN" align="center" prop="isbn" width="200">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.isbn }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.isbn"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="类别" align="center" prop="book_types">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.book_types }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.book_types"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="定价" align="center" prop="book_price">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.book_price }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.book_price"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="电子书" align="center" prop="e_book" width="150">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.e_book }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.e_book"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="图片路径" align="center" prop="book_img" width="150">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.book_img }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.book_img"></el-input>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="简介" align="center" prop="book_intro" width="400">
                <template slot-scope="scope">
                    <div v-if="!scope.row.isEdit">{{ scope.row.book_intro }}</div>
                    <div v-else>
                        <el-input v-model="scope.row.book_intro"></el-input>
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
                </template>
            </el-table-column>
        </el-table>

        <el-dialog :title=dialogTitle :visible.sync="bookListVisible" width="70%">
            <el-row style="margin:0 0 1rem 0">
                <el-button type="primary" @click="addBookList()">添加{{dialogTitle}}</el-button>
            </el-row>
            <el-table :data="bookList" height="400" border style="width: 100%">
                <el-table-column type="index" label="#"></el-table-column>
                <el-table-column prop="book_id" label="书目ID" width="120">
                </el-table-column>
                <el-table-column prop="borrow_type" label="借阅状态" width="120">
                </el-table-column>
                <!-- <el-table-column prop="borrow_detail" label="借阅详情" width="120">
                </el-table-column> -->
                <el-table-column prop="broken_type" label="损坏情况" width="120">
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-link type="warning" @click="editBookList(scope.row)">编辑</el-link>
                        <h7>&nbsp &nbsp</h7>
                        <el-link type="danger" @click="delBookList(scope.row)">删除</el-link>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
        
        <el-dialog :title="添加图书" :visible.sync="addBookVisible" width="60%">
            <span>
                <el-form :model="newBook" ref="newBook" label-width="70px">
                    <el-form-item label="书名">
                        <el-input v-model="newBook.book_name" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="书目ID">
                        <el-input v-model="newBook.book_id"></el-input>
                    </el-form-item>
                    <el-form-item label="借阅状态">
                        <el-input v-model="newBook.borrow_type"></el-input>
                    </el-form-item>
                </el-form>
            </span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addBookVisible = false">取 消</el-button>
                <el-button type="primary" @click="closeAddBook()">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return{
            bookData: [{
                book_name: '高等数学',
                book_author: '同济大学',
                book_intro: '好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好好',
                e_book: '../AdvancedMath.pdf',
                book_types: '教育',
                book_img: 'D:/Spirited/G3/a.png',
                book_price: '66',
                isbn: '114514'
            }, {
                book_name: '高速退学',
                book_price: 'infinite',

            }],
            bookList: [{
                isbn: '114514',
                book_id: '12b101',
                borrow_type: '可借阅',
                broken_type: '正常'
            },{
                borrow_type: '已被user_id借出，还书日期2022-12-20'
            }],
            newBook: {},
            bookListVisible: false,
            addBookVisible: false,
            dialogTitle: ''
        }
    },
    methods: {
        showBookList(row) {
            this.dialogTitle = '馆藏的所有《' + row.book_name + '》图书列表'
            this.bookListVisible = true
        },
        editBookInfo(row) {
            if (row.isEdit) { // 已在编辑
                row.isEdit = false
            } else { // 想要编辑
                this.$set(row, 'isEdit', true)
            }
        },
        addBookList() {
            this.addBookVisible = true
        },
        delBookList(row) {
            
        },
        closeAddBook() {
            this.addBookVisible = false
            this.bookList.push(this.newBook)
        },
        handle(row, column, cell, event) {}
    }
}
</script>

<style scoped>
.book-table {
    margin-top: 2rem;
    border-radius: 0.5rem;
}
</style>