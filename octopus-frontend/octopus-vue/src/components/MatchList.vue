<template>
	<table class="table table-striped table-striped">
		<thead class="thead-dark">
			<tr>
				<th>Id</th>
				<th>Number</th>
				<th>League</th>
				<th>Kickoff</th>
				<th>Home</th>
				<th>Away</th>
				<th>Score</th>
				<th>WDL</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="match in matches" v-bind:key="match.id">
				<td>{{match.id}}</td>
				<td>{{match.serialNumber}}</td>
				<td>{{match.league}}</td>
				<td>{{match.kickoffTime | toPrettyDateTime }}</td>
				<td>{{match.home}}</td>
				<td>{{match.away}}</td>
				<td>{{match | formatStatus}}</td>
				<td>{{match.actualWdl}}</td>
			</tr>
		</tbody>
	</table>
</template>
<script>
	import MatchService from '../services/MatchService';
	export default {
		name: "matches",
		data() {
			return {
				matches: [],
				latestDays: 1
			}
		},
		filters: {
			formatStatus: function(match) {
				if (match.halfHomeGoals == -1 ||
					match.halfAwayGoals == -1 ||
					match.finalHomeGoals == -1 ||
					match.finalAwayGoals == -1) {
					return '未开赛';
				} else {
					return ''.concat(match.finalHomeGoals)
						.concat("(")
						.concat(match.halfHomeGoals)
						.concat(")")
						.concat("(")
						.concat(match.halfAwayGoals)
						.concat(")")
						.concat(match.finalAwayGoals);
				}
			}
		},
		methods: {
			refreshMatches() {
				MatchService.retrieveLatestDaysMatches(this.latestDays)
					.then((result) => {
						result.statusText;
						this.matches = result.data;
					});
			},
		},
		created() {
			this.refreshMatches();
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
