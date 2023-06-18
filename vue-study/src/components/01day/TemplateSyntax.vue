<script setup>
import {reactive, ref} from 'vue'
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

// 给每个 todo 对象一个唯一的 id
let id = 0

const newTodo = ref('')
const todos = ref([
    { id: id++, text: 'Learn HTML' },
    { id: id++, text: 'Learn JavaScript' },
    { id: id++, text: 'Learn Vue' }
])

function addTodo() {
    todos.value.push({ id: id++, text: newTodo.value })
    newTodo.value = ''
}

function removeTodo(todo) {
    todos.value = todos.value.filter((t) => t !== todo)
    console.log(todos.value)
}

const type = "C";

const parentMessage = ref('Parent')
const items = ref([{ message: 'Foo',id: 1 }, { message: 'Bar',id: 2 }])
const myObject = reactive({
    title: 'How to do lists in Vue',
    author: 'Jane Doe',
    publishedAt: '2016-04-10'
})
const sets = ref([
    [1, 2, 3, 4, 5],
    [6, 7, 8, 9, 10]
])

function even(numbers) {
    return numbers.filter((number) => number % 2 === 0)
}
</script>
<template>
    <!-- 插值 在文本插值中 (双大括号)， 在任何 Vue 指令 (以 v- 开头的特殊 attribute) attribute 的值中 -->
    <div>
        <h1>{{ message }}</h1>
        <p>Using text interpolation: {{ rawHtml }}</p>
        <!-- 将此元素的 innerHTML 与 rawHtml 属性保持同步-->
        <p>Using v-html directive: <span v-html="rawHtml"></span></p>
    </div>

    <!-- v-bind 指令指示 Vue 将元素的 id attribute 与组件的 dynamicId 属性保持一致。如果绑定的值是 null 或者 undefined，那么该 attribute 将会从渲染的元素上移除。-->
    <div>
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
    </div>
    <!--表单绑定-->
    <div>
        <input v-model="text" placeholder="请输入">
        <p>{{ text }}</p>
    </div>
    <!--条件渲染if-else-->
    <div>
        <div>
            <button @click="toggle">查看</button>
            <h1 v-if="awesome">Vue is awesome!</h1>
            <h1 v-else>Oh no 😢</h1>
        </div>

        <!-- else-if -->
        <div v-if="type === 'A'">
            A
        </div>
        <div v-else-if="type === 'B'">
            B
        </div>
        <div v-else-if="type === 'C'">
            C
        </div>
        <div v-else>
            Not A/B/C
        </div>

        <!-- template 只是一个不可见的包装器元素，最后渲染的结果并不会包含这个 <template> 元素 -->
        <template v-if="false">
            <p>1</p>
            <p>2</p>
            <p>3</p>
        </template>
        <template v-else>
            <p>一</p>
            <p>二</p>
            <p>三</p>
        </template>
    </div>

    <!-- v-show v-show 会在 DOM 渲染中保留该元素；v-show 仅切换了该元素上名为 display 的 CSS 属性。-->
    <!-- v-show 不支持在 <template> 元素上使用，也不能和 v-else 搭配使用 -->
    <!-- v-if 有更高的切换开销，而 v-show 有更高的初始渲染开销。因此，如果需要频繁切换，则使用 v-show 较好；如果在运行时绑定条件很少改变，则 v-if 会更合适 -->
    <div>
        <h1 v-show="type === 'A'">Hello!</h1>
        <div>
            <form @submit.prevent="addTodo">
                <input v-model="newTodo">
                <button>Add Todo</button>
            </form>
            <ul>
                <!-- 列表渲染 -->
                <li v-for="todo in todos" :key="todo.id">
                    {{ todo.text }}
                    <button @click="removeTodo(todo)">X</button>
                </li>
            </ul>
        </div>
    </div>

    <!-- 列表 -->
    <!-- items 是源数据的数组, item 是迭代项的别名-->
    <!-- 块中可以完整地访问父作用域内的属性和变量 -->
    <div>
    <!-- 循环数组 -->
        <ul>
            <li>循环数组</li>
            <li v-for="(item, index) in items">
                {{ parentMessage }} - {{ index }} - {{ item.message }}
            </li>

            <li v-for="{ message } in items">
                {{ message }}
            </li>

            <!-- 有 index 索引时 -->
            <li>有 index 索引时</li>
            <li v-for="({ message }, index) of items">
                {{ message }} {{ index }}
            </li>
        </ul>
        <!-- 循环对像value：属性值，key：属性， index：索引 -->
        <h3>循环对像</h3>
        <ul>
            <li v-for="(value, key, index) in myObject">
                {{ index }}. {{ key }}: {{ value }}
            </li>
        </ul>
        <!-- 通过Key管理状态,key是每个元素对应的唯一的变量 -->
        <!-- key的期望值是一个基础类型，不要有对像作为key -->
        <!-- 如果传了Key，则将根据key的变化顺序来重新排列元素，并且将始移除/销毁已经不存在的元素 -->
        <!-- 同一个父元素下的子元素必须具有唯一的key，重复的key将会导致渲染异常 -->
        <h3>通过Key管理状态</h3>
        <ul>
            <li v-for="(item, index) in items" :key="item.id">
                {{ parentMessage }} - {{ index }} - {{ item.message }} - {{item.id}}
            </li>
        </ul>

        <h3>展示过虑的结果</h3>
        <ul v-for="numbers in sets">
            <li v-for="n in even(numbers)">{{ n }}</li>
        </ul>
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
