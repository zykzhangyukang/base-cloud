import {reactive} from 'vue';
export default {
  state: reactive({
    // 应用信息
    app: {
      menuToggle: false,
    },
    // 用户信息
    user: {
      token: localStorage.getItem("token") || '',
      info: {}
    },
    // 项目常量
    const: [],
    // 消息通知
    message:{
      messageList: [],
      noRead: 0,
    }
  }),
  setMenuItem(item, key, value) {
    item[key] = value;
  },
  setAppMenuToggle(toggle) {
    this.state.app.menuToggle = toggle;
  },
  setUserToken(token){
    if(token){
      this.state.user.token = token;
    }else{
      this.state.user.token = ''
    }
  },
  setUserInfo(info) {
    if(info){
      this.state.user.info = info;
    }else{
      this.state.user.info = {}
    }
  },
  setConstList(list){
    const _store = this.state;
    Object.keys(list).forEach(key => {
      _store.const[key] = list[key]
    });
  },
  addUserMsg(msg) {
    this.state.message.messageList.unshift(msg);
    this.state.message.noRead += 1
    if (this.state.message.messageList.length > 10) {
      this.state.message.messageList = this.state.message.messageList.slice(0, 10)
    }
  }
}