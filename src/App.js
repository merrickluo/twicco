/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React from 'react'

import { AsyncStorage } from 'react-native'

import { compose, createStore, combineReducers } from 'redux'
import { Provider } from 'react-redux'

import { persistStore, autoRehydrate } from 'redux-persist'

import SplashScreen from './SplashScreen.js'
import Navigator, { navReducer } from './Navigator.js'

const api = (state, action) => {
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

const root = (state, action) => {
  if (!state) {
    return {}
  }
  console.log(action)
  switch(action.type) {
  case 'LOGIN_SUCCESS':
    return Object.assign({}, state, {
      account: action.account,
    })
  default:
    return state
  }
}

const store = createStore(
  combineReducers({
    api,
    app: root,
    nav: navReducer,
  }),
  undefined,
  compose(
    autoRehydrate({ log: true })
  )
)

const persistConfig = {
  blacklist: ['nav', 'api'],
  storage: AsyncStorage,
}

class App extends React.Component {

  constructor() {
    super()
    this.state = {
      rehydrated: false,
    }
  }

  componentWillMount() {
    persistStore(store, persistConfig, (storedState) => {
      console.log(storedState)
      // eslint-disable-next-line react/no-set-state
      this.setState({
        rehydrated: true,
      })
    })
  }

  render() {
    if (!this.state.rehydrated) {
      return <SplashScreen />
    }
    return (
      <Provider store={store} >
        <Navigator />
      </Provider>
    )
  }
}

export default App
