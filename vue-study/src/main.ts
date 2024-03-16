import './assets/main.css'
//引入 createApp 用于创建应用
import {createApp} from 'vue';
//引入 App 组件
import App from './App.vue';
//引入路由器
import router from "@/router";

//创建一个应用
const app = createApp(App);
//使用路由器
app.use(router);
//挂载整个应用到 app 容器中
app.mount('#app');
