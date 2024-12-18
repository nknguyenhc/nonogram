<script setup lang="ts">
defineProps<{
  colConstraint: number[];
  setColConstraint: (constraint: string) => void;
  isHighlighted?: boolean;
}>();
</script>

<template>
  <div class="col-constraint" @click="(event) => event.stopPropagation()">
    <input
      :class="{ highlighted: isHighlighted }"
      type="text"
      value="1"
      @input="
        (event) => setColConstraint((event.target as HTMLInputElement).value)
      "
    />
  </div>
</template>

<style scoped>
.col-constraint {
  position: absolute;
  right: 0;
  top: 0;
  transform: translate(-40px, -100%) rotate(45deg);
  transform-origin: right bottom;
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
