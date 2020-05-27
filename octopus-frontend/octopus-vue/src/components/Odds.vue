<template>
	<table class="table table-striped table-striped">
		<thead class="thead-dark">
			<tr>
				<th>Home</th>
				<th>Draw</th>
				<th>Away</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="change in changeList" v-bind:key="change.id">
				<td>{{change.home}}</td>
				<td>{{change.draw}}</td>
				<td>{{change.away}}</td>
			</tr>
		</tbody>
	</table>
</template>
<script>
	import OddsService from "../services/OddsService";
	export default {
		name: "changeList",
		data() {
			return {
				changeList: []
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
			refreshOdds() {
				OddsService.retrieveOdds(this.matchId, this.providerId).then(result => {
					this.changeList = result.data.changeHistories;
				});
			},
		},
		created() {
			this.refreshOdds();
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