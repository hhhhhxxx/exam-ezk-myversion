/**
 * Tokens service
 */

import { Serialize } from "@/utils/index";

import { PostRequest, frontBase, apiBase } from "./common";

export const userLogin = async (data) => {
  // eslint-disable-next-line no-return-await
  return await PostRequest(
    `${process.env.VUE_APP_API_FAKE}/user/login?${Serialize(data)}`
  );
  // 改成之前的请求获取权限先
};

// 更新token
export const fetchUpdateToken = async (refreshToken) => {
  // await PostRequest(`${frontBase}/user/refresh_token?refreshtoken=${refreshToken}`)

  // sb 这里少了return 害我差了半天
  return await PostRequest(`/user/refresh_token?refreshtoken=${refreshToken}`);
};
