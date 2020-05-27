<template>
	<table class="table table-striped table-striped">
		<thead class="thead-dark">
			<tr>
				<th>Home</th>
				<th>Boundary</th>
				<th>Away</th>
				<th>Boundary Change</th>
				<th>Water Change</th>
				<th>First</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="changeAnalysis in changeAnalysisList" v-bind:key="changeAnalysis.id">
				<td>{{changeAnalysis.home}}</td>
				<td>{{changeAnalysis.boundary}}</td>
				<td>{{changeAnalysis.away}}</td>
				<td>{{changeAnalysis.boundaryChange}}</td>
				<td>{{changeAnalysis.waterChange}}</td>
				<td>{{changeAnalysis.first}}</td>
			</tr>
		</tbody>
	</table>
</template>
<script>
	import HandicapService from "../services/HandicapService";
	export default {
		name: "changeAnalysisList",
		data() {
			return {
				changeAnalysisList: []
			};
		},
		computed: {
			matchId() {
				return this.$route.params.id;
			},
			providerId() {
				return this.$route.params.providerId;
			}
		},
		methods: {
			refreshHandicap() {
				HandicapService.retrieveHandicap(this.matchId, this.providerId).then(result => {
					this.changeAnalysisList = result.data.changeAnalyses;
				});
			},
		},
		created() {
			this.refreshHandicap();
		}
	};
</script>

<style scoped>
	#nav {
		padding: 30px;
	}
	
	#nav a {
		font-weight: bold;
		color: #2c3e50;
	}
	
	#nav a.router-link-exact-active {
		color: #42b983;
	}
</style>