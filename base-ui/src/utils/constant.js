import store from '../store/index'

export const authDomain = "auth";
export const syncDomain = "sync";

export function getConst(group,domain) {

    if (!group) {
        return [];
    }
    const arr = store.state.const[domain].filter(e => e.group === group);
    if (arr.length) {
        return arr[0].itemList;
    }
    return [];
}


export function formatConst(list) {

    const obj = {};
    list.forEach((ele) => {

        obj[ele.code] = ele.name;
    })
    return obj;
}


export default {
    getConst,
    formatConst
}