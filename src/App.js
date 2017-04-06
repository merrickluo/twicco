/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React from 'react'

import {
  Router,
  Scene,
  ActionConst,
} from 'react-native-router-flux'

import LoginScreen from './LoginScreen.js'
import HomeScreen from './HomeScreen.js'
import SplashScreen from './SplashScreen.js'

export default class App extends React.Component {
  render() {
    return (
      <Router>
        <Scene key="root">
          <Scene
            hideNavBar
            key="splash"
            component={SplashScreen}
          />
          <Scene
            hideNavBar
            key="login"
            component={LoginScreen}
          />
          <Scene
            key="home"
            component={HomeScreen}
            type={ActionConst.REPLACE}
          />
        </Scene>
      </Router>
    )
  }
}
