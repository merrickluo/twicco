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
    app: root,
    nav: navReducer,
  }),
  undefined,
  compose(
    autoRehydrate({ log: true })
  )
)

const persistConfig = {
  blacklist: ['nav'],
  storage: AsyncStorage,
}
persistStore(store, persistConfig, (storedState) => {
  console.log(storedState)
  // eslint-disable-next-line react/no-set-state
})

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
