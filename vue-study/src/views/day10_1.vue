<script setup lang="ts">

import {reactive} from "vue";

const  newsList = reactive([
    {id:'dfafa01',title:'新闻一',content:'新闻一新闻一新闻一新闻一'},
    {id:'dfafa02',title:'新闻二',content:'新闻二新闻二新闻二新闻二'},
    {id:'dfafa03',title:'新闻三',content:'新闻三新闻三新闻三新闻三'},
    {id:'dfafa04',title:'新闻四',content:'新闻四新闻四新闻四新闻四'}
])
</script>

<template>
    <div>
        <ul class="news">
            <li v-for="news in newsList" :key="news.id">
                <RouterLink :to="{path:'/news/detail'}">{{news.title}}</RouterLink>
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
