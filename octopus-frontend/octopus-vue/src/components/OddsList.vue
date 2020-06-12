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
	<table class="table table-striped table-striped">
		<thead class="thead-dark">
			<tr>
				<th rowspan="2">Provider</th>
				<th colspan="3">First</th>
				<th colspan="3">Current</th>
				<th colspan="3">Ratio</th>
				<th colspan="3">Kelly</th>
				<th rowspan="2">Payoff</th>
			</tr>
			<tr>
				<th>Home</th>
				<th>Draw</th>
				<th>Away</th>
				
				<th>Home</th>
				<th>Draw</th>
				<th>Away</th>
				
				<th>Home</th>
				<th>Draw</th>
				<th>Away</th>
				
				<th>Home</th>
				<th>Draw</th>
				<th>Away</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="odds in oddsList" v-bind:key="odds.id">
				<td><router-link :to="{path: '/matches/'+ odds.match.id +'/odds/' + odds.provider.id}">{{odds.provider.name}}</router-link></td>
				<td>{{odds.first.home|toFix}}</td>
				<td>{{odds.first.draw|toFix}}</td>
				<td>{{odds.first.away|toFix}}</td>
				<td>{{odds.current.home|toFix}}</td>
				<td>{{odds.current.draw|toFix}}</td>
				<td>{{odds.current.away|toFix}}</td>
				<td>{{odds.ratioHome|toFix}}%</td>
				<td>{{odds.ratioDraw|toFix}}%</td>
				<td>{{odds.ratioAway|toFix}}%</td>
				<td>{{odds.kellyHome|toFix}}</td>
				<td>{{odds.kellyDraw|toFix}}</td>
				<td>{{odds.kellyAway|toFix}}</td>
				<td>{{odds.payoff|toFix}}</td>
			</tr>
		</tbody>
	</table>
	</div>
</template>
<script>
	import MatchService from "../services/MatchService";
	import OddsService from '../services/OddsService';
	export default {
		name: "oddsList",
		data() {
			return {
				match: {},
				oddsList: []
			}
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
			refreshOddsList() {
				MatchService.retrieveMatch(this.matchId).then(result => {
				  this.match = result.data;
				});
				OddsService.retrieveOddsList(this.matchId)
					.then((result) => {
						this.oddsList = result.data;
					});
			},
		},
		created() {
			this.refreshOddsList();
		}
	}
</script>

<style>
	.table.table-striped.table-hover tbody tr:hover {
		background: #ff7f7f;
	}

	.table .thead-dark th {
		color: $table-dark-color;
		background-color: #008080;
		border: 1px solid #FF7F7F;
		vertical-align: middle;
	}
</style>
