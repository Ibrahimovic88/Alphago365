<template>
	<div>
		<div class="container">
			<span> {{this.match.home}} vs {{this.match.away}} {{this.match.kickoffTime}} </span>
		</div>
	</div>
</template>
<script>
	import MatchService from "../services/MatchService";
	export default {
		name: "Match",
		data() {
			return {
				match: {},
			};
		},
		computed: {
			matchId() {
				return this.$route.params.id;
			}
		},
		methods: {
			refreshMatch() {
				MatchService.retrieveMatch(this.matchId).then(result => {
					this.match = result.data;
				});
			},
		},
		created() {
			this.refreshMatch();
		}
	};
</script>
