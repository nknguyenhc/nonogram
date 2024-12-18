<script setup lang="ts">
defineProps<{
  width: string;
}>();
</script>

<template>
  <div class="cross"></div>
</template>

<style scoped>
.cross {
  width: v-bind(width);
  height: v-bind(width);
  position: relative;
}

.cross::before,
.cross::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 5px;
  background-color: black;
}

.cross::before {
  transform: translate(-50%, -50%) rotate(45deg);
}

.cross::after {
  transform: translate(-50%, -50%) rotate(-45deg);
}
</style>
