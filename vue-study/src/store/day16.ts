import {defineStore} from "pinia";

export let userCountStore = defineStore('count', {
    // 真正 存储数据的地方
    state() {
        return {
            sum: 6
        }
    },
})
