<script setup lang="ts" name="day16_1">
import axios from "axios";
import {nanoid} from "nanoid";
import {userTalkStore} from "@/store/day16_1";

let talkStore = userTalkStore();

async function getLoveTalk() {
    let {data:{content:title}} = await axios.get('https://api.uomg.com/api/rand.qinghua?format=json');
    let obj = {id: nanoid(), title}
    talkStore.talkList.unshift(obj)
}
</script>

<template>
<div class="talk">
<button @click="getLoveTalk">获取一句土味情话</button>
    <ul>
        <li v-for="talk in talkStore.talkList" :key="talk.id">{{talk.title}}</li>
    </ul>
</div>
</template>

<style scoped>
.talk{
    background-color: lightgreen;
    padding: 10px;
    border-radius: 10px;
    box-shadow: 0 0 10px;
    margin-top: 30px;
}
</style>
