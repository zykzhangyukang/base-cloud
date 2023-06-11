import {message} from 'ant-design-vue';
let __router;
export const setRouter = (router,route)=>{
  __router = {
    router: router,
    route: route
  };
}
export const getRouter = ()=>{
  return __router;
}
export const $iscode = (res, isShowMessage)=>{
  if(res.code === 200){
    return true;
  } else {
    isShowMessage && message.error(res.msg);
    return false;
  }
}