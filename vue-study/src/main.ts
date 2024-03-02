import './assets/main.css'
//引入 createApp 用于创建应用
import {createApp} from 'vue';
//引入 App 组件
import App from './App.vue';

//创建一个应用
const app = createApp(App);
//挂载整个应用到 app 容器中
app.mount('#app');
