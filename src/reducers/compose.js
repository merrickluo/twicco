const initialState = {
  text: '',
  count: 140,
  valid: false,
}

export const actions = {
  changeText: 'compose.changeText',
  clear: 'compose.clear',
}

export default (state, action) => {
  if (!state) return initialState
  switch (action.type) {
  case actions.changeText: {
    const counter = 140 - action.text.length
    const valid = counter > 0 && counter < 140
    return Object.assign({}, state, {
      text: action.text,
      count: counter,
      valid: valid,
    })
  }
  case actions.clear:
    return initialState
  default:
    return state
  }
}
