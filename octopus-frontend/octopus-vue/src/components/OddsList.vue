<template>
	<table class="table table-striped table-striped">
		<thead class="thead-dark">
			<tr>
				<th>Provider</th>
				<th>Home(First)</th>
				<th>Draw(First)</th>
				<th>Away(First)</th>
				<th>Home(Current)</th>
				<th>Draw(Current)</th>
				<th>Away(Current)</th>
				<th>Home(Ratio)</th>
				<th>Away(Ratio)</th>
				<th>Home(Kelly)</th>
				<th>Away(Kelly)</th>
				<th>Payoff</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="odds in oddsList" v-bind:key="odds.id">
				<td><router-link :to="{path: '/matches/'+ odds.match.id +'/odds/' + odds.provider.id}">{{odds.provider.name}}</router-link></td>
				<td>{{odds.first.home}}</td>
				<td>{{odds.first.draw}}</td>
				<td>{{odds.first.away}}</td>
				<td>{{odds.current.home}}</td>
				<td>{{odds.current.draw}}</td>
				<td>{{odds.current.away}}</td>
				<td>{{odds.ratioHome}}</td>
				<td>{{odds.ratioAway}}</td>
				<td>{{odds.kellyHome}}</td>
				<td>{{odds.kellyAway}}</td>
				<td>{{odds.payoff}}</td>
			</tr>
		</tbody>
	</table>
</template>
<script>
	import OddsService from '../services/OddsService';
	export default {
		name: "oddsList",
		data() {
			return {
				oddsList: []
			}
		},
		computed: {
			matchId() {
				return this.$route.params.id;
			}
		},
		methods: {
			refreshOddsList() {
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
		border-color: #008080;
	}
</style>
