<template>
	<table class="table table-striped table-striped">
		<thead class="thead-dark">
			<tr>
				<th>Over</th>
				<th>Boundary</th>
				<th>Under</th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="change in changeList" v-bind:key="change.id">
				<td>{{change.over}}</td>
				<td>{{change.boundary}}</td>
				<td>{{change.under}}</td>
			</tr>
		</tbody>
	</table>
</template>
<script>
	import OverunderService from "../services/OverunderService";
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
			refreshOverunder() {
				OverunderService.retrieveOverunder(this.matchId, this.providerId).then(result => {
					this.changeList = result.data.changeHistories;
				});
			},
		},
		created() {
			this.refreshOverunder();
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