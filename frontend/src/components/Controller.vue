<script setup lang="ts">
import Dimension from './Dimension.vue';

defineProps<{
  height: number;
  setHeight: (height: number) => void;
  width: number;
  setWidth: (width: number) => void;
}>();
</script>

<template>
  <div class="controller">
    <Dimension
      :height="height"
      :setWidth="setWidth"
      :width="width"
      :setHeight="setHeight"
    />
  </div>
</template>
