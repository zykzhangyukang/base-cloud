export function debounce(func, wait, immediate) {
    let timeout //计时器
    return function (...args) {//args就是传的参数
        let context = this
        if (timeout) clearTimeout(timeout)//如果有计时器就清除计时器
        if (immediate) { //如果立即执行设置为true
            let callNow = !timeout //定时器为空就是true
            timeout = setTimeout(function() {//开启定时器
                timeout = null
            }, wait)//wait：定时器的时间
            if (callNow) func.apply(context, args)//定时器为空的话就执行把传进来的函数指向改变为这个防抖函数，然后把参数传给这个防抖函数。通过return把参数返回。
        } else {
            //立即执行为false就开个定时器在执行这个函数
            timeout = setTimeout(function() {
                func.apply(context, args)
            }, wait)
        }
    }
}