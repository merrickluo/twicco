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
  let nav = state.nav
  if (state.nav.index == 0 && state.nav.routes[0].key === 'Init') {
    const firstRoute = state.app.account ? 'main' : 'login'
    nav = { index: 0, routes: [ { routeName: firstRoute, key: 'Init' } ] }
  }
  return {
    nav: nav,
    account: state.app.account,
  }
})
export default class Navigator extends React.Component {
  componentWillMount() {
    this.navigation = addNavigationHelpers({
      dispatch: this.props.dispatch,
      state: this.props.nav,
    })
  }

  render() {
    return (
      <AppNavigator
        navigation={this.navigation}
      />
    )
  }
}
