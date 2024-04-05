<script setup lang="ts">

import {reactive} from "vue";
import {useRouter} from "vue-router";

const newsList = reactive([
    {id: 'dfafa01', title: '新闻一', content: '新闻一新闻一新闻一新闻一'},
    {id: 'dfafa02', title: '新闻二', content: '新闻二新闻二新闻二新闻二'},
    {id: 'dfafa03', title: '新闻三', content: '新闻三新闻三新闻三新闻三'},
    {id: 'dfafa04', title: '新闻四', content: '新闻四新闻四新闻四新闻四'}
])
let router = useRouter();

interface NewsInter {
    id: string,
    title: string,
    content: string
}

function showNewsDetail(news: NewsInter) {
    router.push({
        name: 'xiangqing',
        params: {
            id: news.id,
            title: news.title,
            content: news.content
        }
    })
}
</script>

<template>
    <div>
        <ul class="news">
            <li v-for="news in newsList" :key="news.id">
                <!-- 第一种写法 -->
                <!--                <RouterLink :to="`/news/detail?id=${news.id}&title=${news.title}&content=${news.content}`">-->
                <!--                    {{ news.title }}-->
                <!--                </RouterLink>-->
                <!-- 第二种写法 -->
                <!--                <RouterLink :to="{-->
                <!--                    path: '/news/detail',-->
                <!--                    query:{-->
                <!--                        id: news.id,-->
                <!--                        title: news.title,-->
                <!--                        content:news.content-->
                <!--                    }-->
                <!--                }">-->
                <!--                    {{ news.title }}-->
                <!--                </RouterLink>-->
                <!-- 第三种写法 -->
                <!--                <RouterLink :to="{-->
                <!--                    name: 'xiangqing',-->
                <!--                    query:{-->
                <!--                        id: news.id,-->
                <!--                        title: news.title,-->
                <!--                        content:news.content-->
                <!--                    }-->
                <!--                }">-->
                <!--                    {{ news.title }}-->
                <!--                </RouterLink>-->
                <!-- params 第一种写法 -->
                <!--                <RouterLink :to="`/news/detail/${news.id}/${news.title}/${news.content}`">-->
                <!--                    {{ news.title }}-->
                <!--                </RouterLink>-->
                <!-- params 第二种写法
                注：传递 params 参数时，若使用 to 的对象写法，必须使用 name 配置项，不能用 path
                需要提前在规则中占位
                -->
                <button @click="showNewsDetail(news)">查看详情</button>
                <RouterLink :to="{
                                    name: 'xiangqing',
                                    params:{
                                        id: news.id,
                                        title: news.title,
                                        content:news.content
                                    }
                                }">
                    {{ news.title }}
                </RouterLink>
            </li>
        </ul>
        <div class="news-content">
            <RouterView></RouterView>
        </div>
    </div>
</template>

<style scoped>
.news {
    float: left;
    width: 10%;
    list-style-type: none;
    padding: 0;
}

.news-content {
    float: left;
    width: 89%;
    height: 240px;
    margin: 10px 0;
    border: solid 1px skyblue;
}
</style>
