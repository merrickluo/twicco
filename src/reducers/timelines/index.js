import { combineReducers } from 'redux'

import home from './home.js'

const timelines = {
  home,
}
export default combineReducers(timelines)
