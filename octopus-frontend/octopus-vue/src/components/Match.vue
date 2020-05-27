<template>
	<div>
		<div class="container">
			<span> {{this.match.home}} vs {{this.match.away}}</span>
			<div id="nav">
				<router-link :to="{path: '/matches/' + this.matchId + '/handicap'}">让球</router-link> |
				<router-link :to="{path: '/matches/' + this.matchId + '/odds'}">赔率</router-link> |
				<router-link :to="{path: '/matches/' + this.matchId + '/overunder'}">大小球</router-link> |
			</div>
		</div>
	</div>
</template>
<script>
	import MatchService from "../services/MatchService";
	export default {
		name: "Match",
		data() {
			return {
				match: {}
			};
		},
		computed: {
			matchId() {
				return this.$route.params.id;
			}
		},
		methods: {
			refreshMatch() {
				MatchService.retrieveMatch(this.matchId)
					.then(result => {
						this.match = result.data;
					});
			},
		},
		created() {
			this.refreshMatch();
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
