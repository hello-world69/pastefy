<template>
    <div style="min-height: 100%;">
        <side-bar />
        <router-view :class="{'mobile': $store.state.mobileVersion}" id="page"></router-view>
    </div>
</template>
<script>
import Sidebar from "./components/Sidebar.vue";
export default {
    components: {
        "side-bar":Sidebar
    }
}
</script>
<style lang="scss" scoped>
    #page {
        min-height: 100%;
        padding: 20px;
        color: var(--text-color);
        padding-left: 410px;
        &.mobile {
            padding: 20px;
            padding-top: 80px;
        }
    }
</style>