<template>
	<div class="container">
		<span> {{ this.match.home }} vs {{ this.match.away }}</span>
		<div id="nav">
			<router-link :to="{ path: '/matches/' + this.matchId + '/handicap' }">让球</router-link>
			|
			<router-link :to="{ path: '/matches/' + this.matchId + '/odds' }">赔率</router-link>
			|
			<router-link :to="{ path: '/matches/' + this.matchId + '/overunder' }">大小球</router-link>
			|
		</div>
		<table class="table table-striped table-striped">
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
					<th>Over</th>
					<th>Boundary</th>
					<th>Under</th>
					<th>Over</th>
					<th>Boundary</th>
					<th>Under</th>
					<th>Over</th>
					<th>Under</th>
					<th>Over</th>
					<th>Under</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="overunder in overunderList" v-bind:key="overunder.id">
					<td>
						<router-link :to="{path: '/matches/'+ overunder.match.id +'/overunder/' + overunder.provider.id}">{{overunder.provider.name}}</router-link>
					</td>
					<td>{{overunder.first.over|toFix}}</td>
					<td>{{overunder.first.boundary|toFix}}</td>
					<td>{{overunder.first.under|toFix}}</td>
					<td>{{overunder.current.over|toFix}}</td>
					<td>{{overunder.current.boundary|toFix}}</td>
					<td>{{overunder.current.under|toFix}}</td>
					<td>{{overunder.ratioOver|toFix}}%</td>
					<td>{{overunder.ratioUnder|toFix}}%</td>
					<td>{{overunder.kellyOver|toFix}}</td>
					<td>{{overunder.kellyUnder|toFix}}</td>
					<td>{{overunder.payoff|toFix}}</td>
				</tr>
			</tbody>
		</table>
	</div>
</template>
<script>
	import MatchService from "../services/MatchService";
	import OverunderService from '../services/OverunderService';
	export default {
		name: "overunderList",
		data() {
			return {
				match: {},
				overunderList: []
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
			refreshOverunderList() {
				MatchService.retrieveMatch(this.matchId).then(result => {
				  this.match = result.data;
				});
				OverunderService.retrieveOverunderList(this.matchId)
					.then((result) => {
						result.statusText;
						this.overunderList = result.data;
					});
			},
		},
		created() {
			this.refreshOverunderList();
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
