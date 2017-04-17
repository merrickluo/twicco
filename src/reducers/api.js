export default (state, action) => {
  if (!state) return {}
  switch (action.type) {
  case 'api.twitter.init':
    return Object.assign({}, state, {
      twitter: action.client
    })
  default:
    return state
  }
}
