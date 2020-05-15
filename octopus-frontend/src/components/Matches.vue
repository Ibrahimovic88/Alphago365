<template>
	<div class="container">
		<div v-if="message" class="alert alert-success">{{this.message}}</div>
		<div class="container">
			<table class="table">
				<thead>
					<tr>
						<th>Id</th>
						<th>KickoffTime</th>
						<th>Number</th>
						<th>Home</th>
						<th>Away</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="match in matchs" v-bind:key="match.id">
						<td>{{match.id}}</td>
						<td>{{match.kickoffTime}}</td>
						<td>{{match.serialNumber}}</td>
						<td>{{match.home}}</td>
						<td>{{match.away}}</td>
						<td><button class="btn btn-success" v-on:click="updatematch(match.id)">Update</button></td>
						<td><button class="btn btn-warning" v-on:click="deletematch(match.id)">Delete</button></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</template>
<script>
	import MatchService from '../services/MatchService';
	export default {
		name: "matches",
		data() {
			return {
				matches: [],
				message: "",
				latestDays: 1
			}
		},
		methods: {
			refreshmatches() {
				MatchService.retrieveLatestDaysMatches(this.latestDays)
					.then((result) => {
						this.matches = result.data;
					});
			},
		},
		created() {
			this.refreshmatches();
		}
	}
</script>
