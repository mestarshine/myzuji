<script setup>
import { ref } from 'vue'
const message = ref('Hello World!')
const rawHtml = '<span style="color: red">This should be red.</span>'
const dynamicId = ref('dynamicId')
const titleClass = ref('title')
const isButtonDisabled = ref('true')
const objectOfAttrs = {
    id: 'container',
    class: 'wrapper'
}
const count = ref(0)

function increment() {
    count.value++
}
const date = new Date()
function toTitleDate(date) {
    let options = { year: 'numeric', month: 'long', day: 'numeric' };
    let formatter = new Intl.DateTimeFormat('zh-cn', options);
    return formatter.format(date)
}

function formatDate(date) {
    let options = { year: 'numeric', month: 'long', day: 'numeric' };
    let formatter = new Intl.DateTimeFormat('zh-cn', options);
    return formatter.format(date)
}

const text = ref('')

const awesome = ref(true)

function toggle() {
    awesome.value = !awesome.value
}
</script>

<template>

    <!-- 插值 在文本插值中 (双大括号)， 在任何 Vue 指令 (以 v- 开头的特殊 attribute) attribute 的值中 -->
    <h1>{{ message }}</h1>
    <p>Using text interpolation: {{ rawHtml }}</p>
    <!-- 将此元素的 innerHTML 与 rawHtml 属性保持同步-->
    <p>Using v-html directive: <span v-html="rawHtml"></span></p>

    <!-- v-bind 指令指示 Vue 将元素的 id attribute 与组件的 dynamicId 属性保持一致。如果绑定的值是 null 或者 undefined，那么该 attribute 将会从渲染的元素上移除。-->
    <div v-bind:id="dynamicId">{{dynamicId}}</div>
    <div v-bind:class="titleClass">{{titleClass}}</div>
    <!--  v-bind 指令简写  -->
    <div :id="dynamicId"></div>
    <div :class="titleClass"></div>
    <button :disabled="isButtonDisabled">Button</button>

    <!--动态绑定多个值-->
    <div v-bind="objectOfAttrs"></div>

    <button v-on:click="increment">{{ count }}</button>
    <!--  简写  -->
    <button @click="increment">{{ count }}</button>
    <div/>
    <time :title="toTitleDate(date)" :datetime="date">
        {{ formatDate(date) }}
    </time>
    <!--表单绑定-->
    <div>
        <input v-model="text" placeholder="请输入">
        <p>{{ text }}</p>
    </div>
    <!--条件渲染-->
    <div>
        <button @click="toggle">查看</button>
        <h1 v-if="awesome">Vue is awesome!</h1>
        <h1 v-else>Oh no 😢</h1>
    </div>
</template>

<style>
.title {
    color: red;
}
#dynamicId{
    background-color: bisque;
}
</style>
