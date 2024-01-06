import {createRouter, createWebHistory} from "vue-router";
// 引入要呈现的组件
import day10 from "@/components/day10.vue"
import day10_1 from "@/components/day10_1.vue"
import day10_2 from "@/components/day10_2.vue"

// 创建路由器
let router = createRouter({
    history: createWebHistory(), //路由器的工作模式
    routes: [{   //一个一个的路由规则
        path: '/',
        component: day10
    },{   //一个一个的路由规则
        path: '/home',
        component: day10
    },{   //一个一个的路由规则
        path: '/news',
        component: day10_1
    },{   //一个一个的路由规则
        path: '/about',
        component: day10_2
    }]
});

export default router
