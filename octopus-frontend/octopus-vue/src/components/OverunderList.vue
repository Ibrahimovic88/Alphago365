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
			<tr v-for="overunder in overunderList" v-bind:key="overunder.id">
				<td><router-link :to="{path: '/matches/'+ overunder.match.id +'/overunder/' + overunder.provider.id}">{{overunder.provider.name}}</router-link></td>
				<td>{{overunder.first.home}}</td>
				<td>{{overunder.first.boundary}}</td>
				<td>{{overunder.first.away}}</td>
				<td>{{overunder.current.home}}</td>
				<td>{{overunder.current.boundary}}</td>
				<td>{{overunder.current.away}}</td>
				<td>{{overunder.ratioHome}}</td>
				<td>{{overunder.ratioAway}}</td>
				<td>{{overunder.kellyHome}}</td>
				<td>{{overunder.kellyAway}}</td>
				<td>{{overunder.payoff}}</td>
			</tr>
		</tbody>
	</table>
</template>
<script>
	import OverunderService from '../services/OverunderService';
	export default {
		name: "overunderList",
		data() {
			return {
				overunderList: []
			}
		},
		computed: {
			matchId() {
				return this.$route.params.id;
			}
		},
		methods: {
			refreshOverunderList() {
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
		border-color: #008080;
	}
</style>
