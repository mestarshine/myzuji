<script setup>
const message = 'Hello World!'
const rawHtml = '<span style="color: red">This should be red.</span>'
</script>

<template>
    <h1>{{ message }}</h1>
    <p>Using text interpolation: {{ rawHtml }}</p>
    <p>Using v-html directive: <span v-html="rawHtml"></span></p>
</template>
