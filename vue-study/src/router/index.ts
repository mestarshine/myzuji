import {createRouter, createWebHistory} from "vue-router";
// 引入要呈现的组件
import propsv from "@/views/day19_props_father.vue";
import customEvent from "@/views/day19_custom-event_father.vue";

// 创建路由器
let router = createRouter({
    history: createWebHistory(), //路由器的工作模式
    routes: [{   //一个一个的路由规则
        path: '/props',
        component: propsv
    }, {   //一个一个的路由规则
        path: '/custom-event',
        component: customEvent
    }, {
        path: '/',
        redirect: '/props'
    }]
});

export default router
