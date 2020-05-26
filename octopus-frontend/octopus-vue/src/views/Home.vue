<template>
  <div class="home">
    <MatchList />
  </div>
</template>

<script>
// @ is an alias to /src
import MatchList from "@/components/MatchList.vue";

export default {
  name: "Home",
  components: {
    MatchList
  }
};
</script>
