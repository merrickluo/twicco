/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React from 'react'

import LoginScreen from './LoginScreen.js'
import SplashScreen from './SplashScreen.js'
//import TabIcon from './TabIcon.js'
import HomeScreen from './HomeScreen.js'
import MentionScreen from './MentionScreen.js'
import MessageScreen from './MessageScreen.js'
import ProfileScreen from './ProfileScreen.js'

import { StackNavigator, TabNavigator } from 'react-navigation'

const MainScreen = TabNavigator({
  home: {
    title: 'Home',
    screen: HomeScreen,
  },
  mention: {
    title: 'Mention',
    screen: MentionScreen,
  },
  message: {
    title: 'Message',
    screen: MessageScreen,
  },
  profile: {
    title: 'Profile',
    screen: ProfileScreen,
  },
}, {
  tabBarPosition: 'bottom',
  animationEnabled: false,
  swipeEnabled: false,
  tabBarOptions: {
    showIcon: true,
    showLabel: true,
    activeTintColor: '#05BF7B',
    inactiveTintColor: '#A1A1A1',
    style: {
      backgroundColor: 'transparent',
    },
  }
})

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
