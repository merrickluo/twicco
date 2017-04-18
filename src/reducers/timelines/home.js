const initialState = {
  tweets: [],
}

export const actions = {
  reload: 'timeline.home.reload',
}

export default (state, action) => {
  switch (action.type) {
  case actions.reload:
    return {
      ...state,
      tweets: action.tweets,
    }
  default:
    return state || initialState
  }
}
