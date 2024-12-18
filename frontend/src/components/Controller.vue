<script setup lang="ts">
import Buttons from './Buttons.vue';
import Dimension from './Dimension.vue';

defineProps<{
  height: number;
  setHeight: (height: number) => void;
  width: number;
  setWidth: (width: number) => void;
  getSolution: () => void;
  eraseSolution: () => void;
  editable: boolean;
}>();
</script>

<template>
  <div class="controller">
    <Buttons :getSolution :eraseSolution :editable />
    <Dimension :height :setWidth :width :setHeight :editable />
  </div>
</template>

<style scoped>
.controller {
  display: flex;
  flex-direction: row;
  gap: 30px;
}
</style>
