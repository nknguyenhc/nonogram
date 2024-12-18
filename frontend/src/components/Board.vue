<script setup lang="ts">
import type { Step } from '@/App.vue';
import Cell from './Cell.vue';

const props = defineProps<{
  board: ('X' | boolean)[][];
  toggleCell: (row: number, col: number) => void;
  rowConstraints: number[][];
  colConstraints: number[][];
  setRowConstraint: (index: number, constraint: string) => void;
  setColConstraint: (index: number, constraint: string) => void;
  currentStep?: Step;
  editable: boolean;
}>();
</script>

<template>
  <div class="board">
    <div class="row" v-for="(row, rowIndex) in props.board" :key="rowIndex">
      <Cell
        v-for="(cell, cellIndex) in row"
        :key="cellIndex"
        :data="cell"
        :toggleCell="() => toggleCell(rowIndex, cellIndex)"
        :rowConstraint="cellIndex === 0 ? rowConstraints[rowIndex] : undefined"
        :colConstraint="rowIndex === 0 ? colConstraints[cellIndex] : undefined"
        :setRowConstraint="
          cellIndex === 0
            ? (constraint: string) =>
                props.setRowConstraint(rowIndex, constraint)
            : undefined
        "
        :setColConstraint="
          rowIndex === 0
            ? (constraint: string) =>
                props.setColConstraint(cellIndex, constraint)
            : undefined
        "
        :isRowConstraintHighlighted="
          currentStep?.isRow && currentStep?.index === rowIndex
        "
        :isColConstraintHighlighted="
          currentStep?.isRow === false && currentStep?.index === cellIndex
        "
        :editable
      />
    </div>
  </div>
</template>

<style scoped>
.board {
  display: flex;
  flex-direction: column;
  padding: 60px 0px 0px 100px;
}
.row {
  display: flex;
  flex-direction: row;
}
</style>
