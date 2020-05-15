import axios from 'axios'

const MATCH_API_URL = 'http://127.0.0.1:8888/api/v2/matches'

class MatchService {
	retrieveLatestDaysMatches(latestDays) {
		return axios.get(`${MATCH_API_URL}/current-date?latest-days=${latestDays}`);
	}

	retrieveMatch(id) {
		return axios.get(`${MATCH_API_URL}/${id}`);
	}
}

export default new MatchService()
