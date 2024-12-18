<script setup lang="ts">
defineProps<{
  rowConstraint: number[];
  setRowConstraint: (constraint: string) => void;
  isHighlighted?: boolean;
  editable: boolean;
}>();
</script>

<template>
  <div class="row-constraint" @click="(event) => event.stopPropagation()">
    <input
      :class="{ highlighted: isHighlighted }"
      :disabled="!editable"
      type="text"
      value="1"
      @input="
        (event) => setRowConstraint((event.target as HTMLInputElement).value)
      "
    />
  </div>
</template>

<style scoped>
.row-constraint {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translate(-120%, -50%);
}
.highlighted {
  background-color: lightgreen;
}
input {
  width: 80px;
  height: 30px;
  font-size: 16px;
  padding: 5px;
  border-radius: 5px;
  border: 1px solid black;
  text-align: center;
}
</style>
