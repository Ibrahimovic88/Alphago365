import axios from "axios";

const BASE_API_URL = "https://www.alphago365.com/api/v2";

class HandicapService {
  retrieveHandicapList(matchId) {
    return axios.get(`${BASE_API_URL}/matches/${matchId}/handicap`);
  }

  retrieveHandicap(matchId, providerId) {
    return axios.get(
      `${BASE_API_URL}/matches/${matchId}/handicap/${providerId}`
    );
  }
}
export default new HandicapService();
