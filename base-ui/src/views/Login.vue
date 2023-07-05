<template>
  <a-layout class='login animated fadeIn'>
    <div class="model">
      <div class="login-form">
        <h3>后台管理系统</h3>
        <a-divider />
        <a-form
          :model='formModel'
          @finish='handleSubmitFinish'
          @finishFailed='handleSubmitFinishFailed'
        >
          <a-form-item
            name='username'
            :rules="[{ required: true, message: '请输入用户名' }]"
          >
            <a-input v-model:value="formModel.username" placeholder='用户名' autocomplete="off">
              <template v-slot:prefix>
                <user-outlined />
              </template> 
            </a-input>
          </a-form-item>
          <a-form-item
            name='password'
            :rules="[{ required: true, message: '请输入密码' }]"
          >
            <a-input-password v-model:value="formModel.password" placeholder="密码" autocomplete="off">
              <template v-slot:prefix>
                <lock-outlined />
              </template> 
            </a-input-password>
          </a-form-item>
          <a-form-item>
            <a-button 
              type='primary' 
              htmlType='submit' 
              class='login-form-button' 
              :loading='loading'>
                登录
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
  </a-layout>
</template>
<style lang="less">
@import '../style/views/login.less';
</style>
<script>
  import store from '../store';
  import {LockOutlined, UserOutlined} from '@ant-design/icons-vue'
  import { authUserLogin } from '@/api/auth';

  export default {
  name: 'Login',
  components: {
    UserOutlined,
    LockOutlined
  },
  data() {
    return {
      loading: false,
      formModel: {
        username: 'admin',
        password: '123456'
      }
    }
  },
  methods: {
    setUserToken(token) {
      store.setUserToken(token);
    },
    handleSubmitFinish() {
      this.loading = true;
      authUserLogin({
                    username: this.formModel.username,
                    password: this.formModel.password,
                  }).then(res=>{

        localStorage.setItem('token', res.result.token);
        this.setUserToken(res.result.token);

        this.$router.push('/').then(()=>{

          this.$message.success("用户登录成功！");
        })

      }).finally(e=>{
        this.loading = false;
      })
    },
    handleSubmitFinishFailed(errorInfo){
      console.log('Failed:', errorInfo);
    }
  }
}
</script>