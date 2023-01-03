<template>
  <div>
    <h3 class="greet">亲爱的管理员
      <span style="color:gray;font-style: italic;">{{adminInfo.adminId}}</span> , 欢迎使用在线图书管理系统！</h3>
    <el-descriptions class="margin-top" title="" :column="3" :size="size" border>
      <template slot="extra">
        <el-button type="primary" size="small" @click="showEditDialog()">编辑用户信息</el-button>
      </template>
      <el-descriptions-item span="10">
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        {{adminInfo.adminName}}
      </el-descriptions-item>
      <el-descriptions-item span="10">
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          电话
        </template>
        {{adminInfo.adminPhone}}
      </el-descriptions-item>
      <el-descriptions-item span="10">
        <template slot="label">
          <i class="el-icon-office-building"></i>
          邮箱
        </template>
        {{adminInfo.adminEmail}}
      </el-descriptions-item>
      <!-- <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-reading"></i>
          当前图书馆藏数
        </template>
        {{numOfBooks}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-paperclip"></i>
          当前待处理的反馈数
        </template>
        {{numOfFeedback}}
      </el-descriptions-item> -->
    </el-descriptions>

    <el-dialog title="编辑用户信息" :visible.sync="editDialogVisible" width="50%">
      <span>
        <el-form :model="editInfo" ref="editInfo" :rules="editFormRules" label-width="70px">
          <el-form-item label="用户名">
            <el-input v-model="editInfo.adminName"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="editInfo.adminEmail"></el-input>
          </el-form-item>
          <el-form-item label="手机">
            <el-input v-model="editInfo.adminPhone"></el-input>
          </el-form-item>
        </el-form>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="closeEditDialog()">确 定</el-button>
      </span>
    </el-dialog>
    
    <vue-qrcode></vue-qrcode>

  </div>
</template>

<script>
import qs from 'qs'
import qrcode from './scan.vue'
export default {
  data(){
    return{
      adminInfo: {
        adminId: '100',
        adminPhone: '18500001111',
        adminEmail: 'test@gmail.com',
        adminName: 'ADMINISTRATOR'
      },
      editInfo: {
        adminPhone: ''
      },
      numOfBooks: 300,
      numOfFeedback: 6,
      editDialogVisible: false
    }
  },
  components:{
    'vue-qrcode': qrcode,
  },
   
  
  methods: {
    async getAdminInfo() {
        let submit = {
          "id": window.localStorage.getItem('id'),
          "token": window.localStorage.getItem('token')
        }        
        await this.$http.post(this.baseUrl+'/admin/adminInfoSearch', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {
          if (res.data.state === 'fail') {
            this.$message.error({
            message: '管理员信息获取失败',
            duration: 1500
          })
          } else {
            this.adminInfo.adminName = res.data.result[0].adminName
            this.adminInfo.adminPhone = res.data.result[0].adminPhone
            this.adminInfo.adminEmail = res.data.result[0].adminEmail
          }
        }).catch(err => {
          if (err) {
            this.$message.error({
              message: '[CATCH]管理员信息获取失败' + err,
              duration: 1500
            })
          }
        })        
      },
    showEditDialog() {
      this.editInfo = this.adminInfo
      this.editDialogVisible = true
    },
    async closeEditDialog() {
      this.editDialogVisible = false
      let submit = {
            "id": window.localStorage.getItem('id'),
            "token": window.localStorage.getItem('token'),
            "newId": "no",
            "name": this.editInfo.adminName,
            "password": "no",
            "telephone": this.editInfo.adminPhone,
            "email": this.editInfo.adminEmail,
          }
      await this.$http.post(this.baseUrl+'/admin/adminInfoChange', qs.stringify(submit), 
          {headers: {'Content-Type':'application/x-www-form-urlencoded'}}).then(res => {    
        if (res.data.state == 'fail') {
          this.$message.error({
            message: "管理员个人信息修改失败"
          })
        } else {
          this.$message.success({
            message: "管理员个人信息修改成功"
          })
          this.getAdminInfo()
        }
      }).catch(err => {this.$message.error({message: '[CATCH]管理员个人信息修改失败'})})
    },
    // editFormRules: {
    //   email: [
    //       { required: true, message: '请输入用户邮箱', trigger: 'blur' },
    //       // { validator: checkEmail, trigger: 'blur' }
    //   ],
    //   mobile: [
    //       { required: true, message: '请输入用户手机', trigger: 'blur' },
    //       // { validator: checkMobile, trigger: 'blur' }
    //   ]
// }
  },
  created() {
    this.adminInfo.adminId = window.localStorage.getItem('id')
    this.getAdminInfo()
  }
}
</script>
<style scoped>

</style>