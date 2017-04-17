export default (state, action) => {
  if (!state) {
    return {}
  }
  switch(action.type) {
  case 'LOGIN_SUCCESS':
    return Object.assign({}, state, action.account)
  default:
    return state
  }
}
