import {defineStore} from "pinia";

export let userTalkStore = defineStore('talk', {
    // 真正 存储数据的地方
    state() {
        return {
            talkList:[
                {id:'gafaf01',title:'今天有点怪，哪里怪？怪好看的！'},
                {id:'gafaf02',title:'我能陪你熬夜也会劝你早睡.'},
            ]
        }
    },
})
