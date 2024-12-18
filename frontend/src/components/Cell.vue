<script setup lang="ts">
import { ref } from 'vue';
import Cross from './Cross.vue';
import RowConstraint from './RowConstraint.vue';
import ColConstraint from './ColConstraint.vue';

const props = defineProps<{
  data: 'X' | boolean;
  toggleCell: () => void;
  rowConstraint?: number[];
  colConstraint?: number[];
  setRowConstraint?: (constraint: string) => void;
  setColConstraint?: (constraint: string) => void;
  isRowConstraintHighlighted?: boolean;
  isColConstraintHighlighted?: boolean;
}>();

const cellWidth = ref('50px');
</script>

<template>
  <div
    :class="{ chosen: props.data === true, cell: true }"
    @click="props.toggleCell"
  >
    <Cross v-if="props.data === 'X'" :width="cellWidth" />
    <RowConstraint
      v-if="rowConstraint && setRowConstraint"
      :rowConstraint
      :setRowConstraint
      :isHighlighted="isRowConstraintHighlighted"
    />
    <ColConstraint
      v-if="colConstraint && setColConstraint"
      :colConstraint
      :setColConstraint
      :isHighlighted="isColConstraintHighlighted"
    />
  </div>
</template>

<style scoped>
.cell {
  display: inline-block;
  width: v-bind(cellWidth);
  height: v-bind(cellWidth);
  border: 1px solid black;
  text-align: center;
  position: relative;
}
.chosen {
  background-color: lightblue;
}
</style>
