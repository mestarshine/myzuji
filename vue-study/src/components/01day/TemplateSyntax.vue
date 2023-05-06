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
</script>

<template>
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

    <div v-bind="objectOfAttrs"></div>
</template>

<style>
.title {
    color: red;
}
#dynamicId{
    background-color: bisque;
}
</style>
