<template>
  <a-modal
    :title="type === 'add' ? '新增' : type === 'edit'? '编辑' : '详情'"
    :visible="visible"
    width="30%"
    @cancel="cancel()"
  >
    <template slot="footer">
      <a-button key="back" @click="cancel()">取消</a-button>
      <a-button
        key="submit"
        type="primary"
        :hidden="isDetail"
        @click="ok()"
      >确定
      </a-button>
    </template>

    <a-spin :spinning="loading">
      <a-form :form="form">
        <a-form-item
          label="账号"
          :label-col="formItemLayout.labelCol"
          :wrapper-col="formItemLayout.wrapperCol"
        >
          <a-input
            placeholder="Account Code"
            :disabled="isDetail || isEdit"
            v-decorator="[
              'accountCode',
              { rules: [{ required: true, message: '账号不能为空!' }] }]"
          />
        </a-form-item>

        <a-form-item
          label="用户名"
          :label-col="formItemLayout.labelCol"
          :wrapper-col="formItemLayout.wrapperCol"
        >
          <a-input
            placeholder="UserName"
            :disabled="isDetail"
            v-decorator="[
              'userName',
              { rules: [{ required: true, message: '用户名不能为空!' }] }]"
          />
        </a-form-item>

        <a-form-item
          label="密码"
          :label-col="formItemLayout.labelCol"
          :wrapper-col="formItemLayout.wrapperCol"
        >
          <a-input
            placeholder="Password"
            :disabled="isDetail"
            v-decorator="[
              'password',
              { rules: [{ required: true, message: '密码不能为空!' }] }]"
          />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import {getSysUser, addSysUser, updateSysUser} from '@/api/sysUser.js';

export default {
  name: "UserModal",
  data() {
    return {
      type: '',
      visible: false,
      loading: false,
      isDetail: false,
      isEdit: false,
      formItemLayout: {
        labelCol: {span: 7},
        wrapperCol: {span: 14}
      },
      form: this.$form.createForm(this)
    }
  },
  methods: {
    paramReceive(type, data) {
      this.type = type
      this.visible = true
      this.loading = false
      if (this.type === 'add') {
      }
      if (this.type === 'detail') {
        this.isDetail = true
        this.setFormValue(data)
      }
      if (this.type === 'edit') {
        this.isEdit = true
        this.setFormValue(data)
      }
    },
    cancel() {
      this.visible = false
      this.isDetail = false
      this.isEdit = false
      this.form.resetFields()
    },
    ok() {
      this.form.validateFields((errors, values) => {
        if (!errors) {
          const {type} = this
          switch (type) {
            case 'add':
              addSysUser(values).then(res => {
                switch (res.data) {
                  case 1 :
                    this.$message.success('新增成功')
                    break
                  case 2 :
                    this.$message.error('账号重复')
                    break
                  case 0 :
                    this.$message.error('新增失败')
                    break
                }
              })
              break
            case 'edit':
              updateSysUser(values).then(res => {
                if (res.data === 1) {
                  this.$message.success('更新成功')
                } else {
                  this.$message.error('更新失败')
                }
              })
              break
          }
          this.cancel()
        }
      })
    },
    setFormValue(data) {
      const code = data
      if (code !== '') {
        getSysUser(code).then(res => {
          this.form.setFieldsValue({
            accountCode: res.data.accountCode,
            userName: res.data.userName,
            password: res.data.password
          })
        })
      }
    }
  }
}
</script>

<style scoped>
</style>
