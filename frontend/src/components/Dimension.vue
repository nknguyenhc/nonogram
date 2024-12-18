<script setup lang="ts">
defineProps<{
  height: number;
  setHeight: (height: number) => void;
  width: number;
  setWidth: (width: number) => void;
  editable: boolean;
}>();
</script>

<template>
  <div class="dimension">
    <label>
      Height:
      <input
        type="number"
        :disabled="!editable"
        :value="height"
        @input="
          (event) => setHeight(Number((event.target as HTMLInputElement).value))
        "
      />
    </label>
    <label>
      Width:
      <input
        type="number"
        :disabled="!editable"
        :value="width"
        @input="
          (event) => setWidth(Number((event.target as HTMLInputElement).value))
        "
      />
    </label>
  </div>
</template>

<style scoped>
.dimension {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: flex-end;
}
label {
  display: flex;
  flex-direction: row;
  gap: 15px;
  align-items: center;
}
input {
  width: 100px;
  height: 30px;
  font-size: 16px;
  padding: 5px;
  border-radius: 5px;
  border: 1px solid black;
  text-align: center;
}
</style>
