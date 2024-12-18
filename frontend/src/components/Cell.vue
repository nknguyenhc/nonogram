<script setup lang="ts">
import Cross from './Cross.vue';

const props = defineProps<{
  data: string | boolean;
  toggleCell: () => void;
}>();
</script>

<template>
  <div
    :class="{ chosen: props.data === true, cell: true }"
    @click="props.toggleCell"
  >
    <Cross v-if="props.data === 'X'" />
  </div>
</template>

<style scoped>
.cell {
  display: inline-block;
  width: 100px;
  height: 100px;
  border: 1px solid black;
  text-align: center;
  line-height: 100px;
  font-size: 24px;
}
.chosen {
  background-color: lightblue;
}
</style>
