<template>
	<table class="table table-striped table-striped">
		<thead class="thead-dark">
			<tr>
				<th>Provider</th>
				<th>Home(First)</th>
				<th>Boundary(First)</th>
				<th>Away(First)</th>
				<th>Home(Current)</th>
				<th>Boundary(Current)</th>
				<th>Away(Current)</th>
				<th>Home(Ratio)</th>
				<th>Away(Ratio)</th>
				<th>Home(Kelly)</th>
				<th>Away(Kelly)</th>
				<th>Payoff</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="handicap in handicapList" v-bind:key="handicap.id">
				<td><router-link :to="{path: '/matches/'+ handicap.match.id +'/handicap/' + handicap.provider.id}">{{handicap.provider.name}}</router-link></td>
				<td>{{handicap.first.home}}</td>
				<td>{{handicap.first.boundary}}</td>
				<td>{{handicap.first.away}}</td>
				<td>{{handicap.current.home}}</td>
				<td>{{handicap.current.boundary}}</td>
				<td>{{handicap.current.away}}</td>
				<td>{{handicap.ratioHome}}</td>
				<td>{{handicap.ratioAway}}</td>
				<td>{{handicap.kellyHome}}</td>
				<td>{{handicap.kellyAway}}</td>
				<td>{{handicap.payoff}}</td>
			</tr>
		</tbody>
	</table>
</template>
<script>
	import HandicapService from '../services/HandicapService';
	export default {
		name: "handicapList",
		data() {
			return {
				handicapList: []
			}
		},
		computed: {
			matchId() {
				return this.$route.params.id;
			}
		},
		methods: {
			refreshHandicapList() {
				HandicapService.retrieveHandicapList(this.matchId)
					.then((result) => {
						console.log(result.statusText);
						this.handicapList = result.data;
					});
			},
		},
		created() {
			this.refreshHandicapList();
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
