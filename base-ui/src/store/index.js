import {reactive} from 'vue';
export default {
  state: reactive({
    app: {
      menuToggle: false,
    },
    user: {
      token:'',
      info: {}
    },
    constList: [],
  }),
  setMenuItem(item, key, value) {
    item[key] = value;
  },
  setAppLanguage(language) {
    this.state.app.language = language;
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
    if(list){
      this.state.constList = list;
    }else {
      this.state.constList = [];
    }
  }
}