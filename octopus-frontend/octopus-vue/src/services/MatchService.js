import axios from "axios";

const BASE_API_URL = "https://www.alphago365.com/api/v2";

class MatchService {
  retrieveLatestDaysMatches(latestDays) {
    return axios.get(
      `${BASE_API_URL}/matches/current-date?latest-days=${latestDays}`
    );
  }

  retrieveMatch(id) {
    return axios.get(`${BASE_API_URL}/matches/${id}`);
  }
}

export default new MatchService();
