import { post, postNoAuthHeader,get } from "@/utils/request";

export default {
  // login: query => postWithLoadTip(`/api/user/login`, query),
  // login: query => postWithLoadTip(`/front/user/login`, query),
  login: (query) => postNoAuthHeader(`/user/login`, query),
  getCaptcha:() => get(`/user/captcha`),
  logout: (query) => post(`/api/user/logout`, query),
};
