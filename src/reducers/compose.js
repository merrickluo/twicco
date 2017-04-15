const initialState = {
  text: '',
  count: 140,
  valid: false,
  previewImageUri: null,
  images: [],
}

export const actions = {
  changeText: 'compose.changeText',
  clear: 'compose.clear',
  pickImage: 'compose.pickImage',
  previewImage: 'compose.previewImage',
  previewClear: 'compose.preivewClear',
}

export default (state, action) => {
  if (!state) return initialState
  switch (action.type) {
  case actions.changeText: {
    const counter = 140 - action.text.length
    const valid = counter > 0 && counter < 140
    return {
      ...state,
      text: action.text,
      count: counter,
      valid: valid,
    }
  }
  case actions.pickImage:
    return {
      ...state,
      images: [
        ...state.images,
        action.imageUri
      ]
    }
  case actions.previewImage:
    return {
      ...state,
      previewImageUri: action.imageUri,
    }
  case actions.previewClear:
    return {
      ...state,
      previewImageUri: null,
    }
  case actions.clear:
    return initialState
  default:
    return state
  }
}
