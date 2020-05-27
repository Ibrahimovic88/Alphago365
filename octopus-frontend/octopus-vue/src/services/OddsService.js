import axios from "axios";

const BASE_API_URL = "https://www.alphago365.com/api/v2";

class OddsService {
  retrieveOddsList(matchId) {
    return axios.get(`${BASE_API_URL}/matches/${matchId}/odds`);
  }

  retrieveOdds(matchId, providerId) {
    return axios.get(`${BASE_API_URL}/matches/${matchId}/odds/${providerId}`);
  }
}
export default new OddsService();
