const initialState = {
  text: '',
  count: 140,
  valid: true,
}

export const actions = {
  changeText: 'compose.changeText',
}

export default (state, action) => {
  if (!state) return initialState
  console.log(action)
  switch (action.type) {
  case actions.changeText:
    return Object.assign({}, state, {
      text: action.text,
      count: 140 - action.text.length,
    })
  default:
    return state
  }
}
