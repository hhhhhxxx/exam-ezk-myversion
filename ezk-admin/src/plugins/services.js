import { TokenService, CommentsService, UserService } from '../services'

export default Vue => {
  // alias
  const services = {
    token: TokenService,
    user: UserService,
    comments: CommentsService
  }

  // mount the services to Vue
  Object.defineProperties(Vue, {
    services: { get: () => services }
  })

  // mount the services to Vue component instance
  Object.defineProperties(Vue.prototype, {
    $services: { get: () => services }
  })
}
