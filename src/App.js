/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React from 'react'

import LoginScreen from './LoginScreen.js'
import SplashScreen from './SplashScreen.js'

import { StackNavigator } from 'react-navigation'

import MainScreen from './MainScreen.js'

const RootNavigator = StackNavigator({
  splash: {
    screen: SplashScreen,
  },
  login: {
    screen: LoginScreen,
  },
  main: {
    screen: MainScreen,
  }
},{
  initialRouteName: 'splash',
  headerMode: 'none',
})

export default class App extends React.Component {
  render() {
    return (
      <RootNavigator />
    )
  }
}
