import {defineStore} from "pinia";

export let userCountStore = defineStore('count', {
    // action 中放置的是一个一个方法，用于响应组件中的 动作
    actions: {
        increment(value: number) {
            if (this.sum < 10) {
                // this 是当前的 countStore
                this.sum += value;
            }
        },
    },
    // 真正 存储数据的地方
    state() {
        return {
            sum: 6
        }
    },
    getters: {
        bigSum(state) {
            return state.sum * 10
        },
        smallSum: state => state.sum / 10,
        middleSum():number {
            return this.sum * 5
        }
    }
})
