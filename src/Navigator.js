import React from 'react'

import { StackNavigator, addNavigationHelpers } from 'react-navigation'
import { connect } from 'react-redux'

import LoginScreen from './LoginScreen.js'
import MainScreen from './MainScreen.js'

const routes = {
  login: {
    screen: LoginScreen,
  },
  main: {
    screen: MainScreen,
  }
}

const AppNavigator = StackNavigator(routes, {
  headerMode: 'none',
})

export const navReducer = (state, action) => {
  const newState = AppNavigator.router.getStateForAction(action, state)
  return (newState ? newState : state)
}

@connect(state => {
  // FIXME this is kindof hack
  if (state.nav.index == 0 && state.nav.routes[0].key === 'Init') {
    if (state.app.account) {
      return { nav: { index: 0, routes: [ { routeName: 'main', key: 'Init' } ] } }
    } else {
      return { nav: { index: 0, routes: [ { routeName: 'login', key: 'Init' } ] } }
    }
  }
  return {
    nav: state.nav,
  }
})

export default class Navigator extends React.Component {
  render() {
    return (
      <AppNavigator navigation={
        addNavigationHelpers({
          dispatch: this.props.dispatch,
          state: this.props.nav,
        })
      }
      />
    )
  }
}
