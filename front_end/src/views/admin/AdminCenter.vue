<template>
  <div>
    <h3 class="greet">亲爱的管理员
      <span style="color:gray;font-style: italic;">{{adminInfo.admin_name}}</span> , 欢迎使用在线图书管理系统！</h3>
    <el-descriptions class="margin-top" title="" :column="3" :size="size" border>
      <template slot="extra">
        <el-button type="primary" size="small" @click="showEditDialog()">编辑用户信息</el-button>
      </template>
      <el-descriptions-item span="10">
        <template slot="label">
          <i class="el-icon-user"></i>
          用户名
        </template>
        {{adminInfo.admin_id}}
      </el-descriptions-item>
      <el-descriptions-item span="10">
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          电话
        </template>
        {{adminInfo.admin_phone}}
      </el-descriptions-item>
      <el-descriptions-item span="10">
        <template slot="label">
          <i class="el-icon-office-building"></i>
          邮箱
        </template>
        {{adminInfo.admin_email}}
      </el-descriptions-item>
      <el-descriptions-item>
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
      </el-descriptions-item>
    </el-descriptions>

    <el-dialog title="编辑用户信息" :visible.sync="editDialogVisible" width="50%">
      <span>
        <el-form :model="adminInfo" ref="adminInfo" :rules="editFormRules" label-width="70px">
          <el-form-item label="用户名">
            <el-input v-model="adminInfo.admin_id" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="adminInfo.admin_email"></el-input>
          </el-form-item>
          <el-form-item label="手机">
            <el-input v-model="editInfo.admin_phone"></el-input>
          </el-form-item>
        </el-form>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="closeEditDialog()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data(){
    return{
      adminInfo: {
        admin_id: '100',
        admin_phone: '18500001111',
        admin_email: 'test@gmail.com',
        admin_name: 'ADMINISTRATOR'
      },
      editInfo: {
        admin_phone: ''
      },
      numOfBooks: 300,
      numOfFeedback: 6,
      editDialogVisible: false
    }
  },
  methods: {
    showEditDialog() {
      this.editInfo.admin_phone = this.adminInfo.admin_phone
      this.editDialogVisible = true
    },
    closeEditDialog() {
      this.editDialogVisible = false
      this.adminInfo.admin_phone = this.editInfo.admin_phone
    }
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
  }
}
</script>

<style scoped>
.gray-title {
  font-size: 2.2rem;
  color: gray;
  letter-spacing: 1rem;
  text-shadow: 5px 5px 5px gray;
}

</style>