import axios from "axios";

const BASE_API_URL = "https://www.alphago365.com/api/v2";

class OverunderService {
  retrieveOverunderList(matchId) {
    return axios.get(`${BASE_API_URL}/matches/${matchId}/overunder`);
  }

  retrieveOverunder(matchId, providerId) {
    return axios.get(
      `${BASE_API_URL}/matches/${matchId}/overunder/${providerId}`
    );
  }
}
export default new OverunderService();
