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
    const: [],
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
  }
}