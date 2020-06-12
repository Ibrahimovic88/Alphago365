<template>
	<div class="container">
		<table class="table table-striped table-striped">
			<thead class="thead-dark">
				<tr>
					<th>Id</th>
					<th>Number</th>
					<th>League</th>
					<th>Kickoff</th>
					<th>Home</th>
					<th>Away</th>
					<th>Score(Half)</th>
					<th>WDL</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="match in matches" v-bind:key="match.id">
					<td>
						<router-link :to="{path: '/matches/' + match.id + '/handicap'}">{{match.id}}</router-link>
					</td>
					<td>{{match.serialNumber}}</td>
					<td>{{match.league}}</td>
					<td>{{match.kickoffTime | moment('MM/DD HH:mm') }}</td>
					<td>{{match.home}}</td>
					<td>{{match.away}}</td>
					<td>{{match | formatScore}}</td>
					<td v-if="match.actualWdl==='UNKNOWN'">---</td>
					<td v-else>{{match.actualWdl|formatWdl}}</td>
				</tr>
			</tbody>
		</table>
	</div>
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
			formatScore: function(match) {
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
						.concat(" : ")
						.concat("(")
						.concat(match.halfAwayGoals)
						.concat(")")
						.concat(match.finalAwayGoals);
				}
			},
			formatWdl: function(wdl) {
				switch (wdl) {
					case 'WIN':
						return '主胜';
					case 'DRAW':
						return '平局';
					case 'LOSE':
						return '主负';
					default:
						return '---'
				}
			}
		},
		methods: {
			refreshMatches() {
				MatchService.retrieveLatestDaysMatches(this.latestDays)
					.then((result) => {
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
