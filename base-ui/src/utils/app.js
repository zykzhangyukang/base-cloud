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