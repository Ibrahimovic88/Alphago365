<template>
  <div class="container">
    <span> {{ this.match.home }} vs {{ this.match.away }}</span>
    <div id="nav">
      <router-link :to="{ path: '/matches/' + this.matchId + '/handicap' }"
        >让球</router-link
      >
      |
      <router-link :to="{ path: '/matches/' + this.matchId + '/odds' }"
        >赔率</router-link
      >
      |
      <router-link :to="{ path: '/matches/' + this.matchId + '/overunder' }"
        >大小球</router-link
      >
      |
    </div>
    <table class="table table-striped">
      <thead class="thead-dark">
        <tr>
          <th rowspan="2">Provider</th>
          <th colspan="3">First</th>
          <th colspan="3">Current</th>
          <th colspan="2">Ratio</th>
          <th colspan="2">Kelly</th>
          <th rowspan="2">Payoff</th>
        </tr>
        <tr>
          <th>Home</th>
          <th>Boundary</th>
          <th>Away</th>
          <th>Home</th>
          <th>Boundary</th>
          <th>Away</th>
          <th>Home</th>
          <th>Away</th>
          <th>Home</th>
          <th>Away</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="handicap in handicapList" v-bind:key="handicap.id">
          <td>
            <router-link
              :to="{
                path:
                  '/matches/' +
                  handicap.match.id +
                  '/handicap/' +
                  handicap.provider.id
              }"
              >{{ handicap.provider.name }}</router-link
            >
          </td>
          <td>{{ handicap.first.home | toFix }}</td>
          <td>{{ handicap.first.boundary | toFix }}</td>
          <td>{{ handicap.first.away | toFix }}</td>
          <td>{{ handicap.current.home | toFix }}</td>
          <td>{{ handicap.current.boundary | toFix }}</td>
          <td>{{ handicap.current.away | toFix }}</td>
          <td>{{ handicap.ratioHome | toFix }}%</td>
          <td>{{ handicap.ratioAway | toFix }}%</td>
          <td>{{ handicap.kellyHome | toFix }}</td>
          <td>{{ handicap.kellyAway | toFix }}</td>
          <td>{{ handicap.payoff | toFix }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
import MatchService from "../services/MatchService";
import HandicapService from "../services/HandicapService";
export default {
  name: "handicapList",
  data() {
    return {
      match: {},
      handicapList: []
    };
  },
  filters: {
    toFix: function(value) {
      return new Number(value).toFixed(2);
    }
  },
  computed: {
    matchId() {
      return this.$route.params.id;
    }
  },
  methods: {
    refreshHandicapList() {
      MatchService.retrieveMatch(this.matchId).then(result => {
        this.match = result.data;
      });
      HandicapService.retrieveHandicapList(this.matchId).then(result => {
        console.log(result.statusText);
        this.handicapList = result.data;
      });
    }
  },
  created() {
    this.refreshHandicapList();
  }
};
</script>

<style>
.table.table-striped.table-hover tbody tr:hover {
  background: #ff7f7f;
}

.table .thead-dark th {
  color: $table-dark-color;
  background-color: #008080;
  border: 1px solid #ff7f7f;
  vertical-align: middle;
}
</style>
