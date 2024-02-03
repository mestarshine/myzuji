import {createRouter, createWebHistory} from "vue-router";
// 引入要呈现的组件
import day12 from "@/components/day12.vue"
import day10 from "@/views/day10.vue"
import day10_1 from "@/views/day10_1.vue"
import day10_2 from "@/views/day10_2.vue"
import day14 from "@/components/day14.vue";

// 创建路由器
let router = createRouter({
    history: createWebHistory(), //路由器的工作模式
    routes: [{   //一个一个的路由规则
        path: '/',
        component: day12
    },{   //一个一个的路由规则
        path: '/home',
        component: day10
    },{   //一个一个的路由规则
        name: 'xinwen',
        path: '/news',
        component: day10_1,
        children:[
            {
                name: 'xiangqing',
                path: 'detail/:id/:title/:content?',
                component: day14,
                // 第一种写法：将路由收到的所有 params 参数作为 props 传给路由组件
                props:true
            }
        ]
    },{   //一个一个的路由规则
        path: '/about',
        component: day10_2
    }]
});

export default router
