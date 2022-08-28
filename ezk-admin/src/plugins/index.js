/**
 * My plugins
 */

import axios from "./axios";
import authorize from "./authorize";
import services from "./services";


export default {
  install(Vue) {
    axios(Vue);
    services(Vue);
    authorize(Vue);
  },
};
