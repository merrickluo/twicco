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
import SplashScreen from './SplashScreen.js'
import TabIcon from './TabIcon.js'
import HomeScreen from './HomeScreen.js'
import MentionScreen from './MentionScreen.js'
import MessageScreen from './MessageScreen.js'
import ProfileScreen from './ProfileScreen.js'

export default class App extends React.Component {
  render() {
    return (
      <Router>
        <Scene key="root">
          <Scene hideNavBar key="splash" component={SplashScreen} />
          <Scene hideNavBar key="login" component={LoginScreen} />
          <Scene key="main" tabs type={ActionConst.REPLACE}>
            <Scene key="home" title="home" icon={TabIcon} component={HomeScreen} />
            <Scene key="mention" title="mention" icon={TabIcon} component={MentionScreen} />
            <Scene key="message" title="message" icon={TabIcon} component={MessageScreen} />
            <Scene key="profile" title="profile" icon={TabIcon} component={ProfileScreen} />
          </Scene>
        </Scene>
      </Router>
    )
  }
}
