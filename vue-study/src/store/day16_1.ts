import {defineStore} from "pinia";
import axios from "axios";
import {nanoid} from "nanoid";
import {reactive} from "vue";

// 选项式写法
// export let userTalkStore = defineStore('talk', {
//     actions:{
//       async getATalk(){
//           let {data:{content:title}} = await axios.get('https://api.uomg.com/api/rand.qinghua?format=json');
//           let obj = {id: nanoid(), title}
//           this.talkList.unshift(obj)
//       }
//     },
//     // 真正 存储数据的地方
//     state() {
//         return {
//             talkList:[
//                 {id:'gafaf01',title:'今天有点怪，哪里怪？怪好看的！'},
//                 {id:'gafaf02',title:'我能陪你熬夜也会劝你早睡.'},
//             ]
//         }
//     },
// })
// 组合式写法
export const userTalkStore = defineStore("talk", () => {
    let talkList = reactive([
        {id: 'gafaf01', title: '今天有点怪，哪里怪？怪好看的！'},
        {id: 'gafaf02', title: '我能陪你熬夜也会劝你早睡.'},
    ])

    async function getATalk() {
        let {data: {content: title}} = await axios.get('https://api.uomg.com/api/rand.qinghua?format=json');
        let obj = {id: nanoid(), title}
        talkList.unshift(obj)
    }
    return {talkList,getATalk}
})
