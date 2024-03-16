<script lang="ts" setup name="App">

</script>

<template>
    <div class="container-fluid wrapper">
        <h1 class="title">Vue3 组件通信</h1>
        <hr>
        <div class="row">
            <div class="col-xs-3 col-md-3 col-lg-3 col-xl-3">
                <router-link active-class="active" class="list-group-item" to="/props" >1. props</router-link>
            </div>
            <div class="col-xs-9 col-md-9 col-lg-9 col-xl-9">
                <div class="panel-body">
                    <RouterView/>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* 样式 */
.title {
    text-align: center;
}
</style>
