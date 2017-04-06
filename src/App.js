/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  AsyncStorage,
} from 'react-native';

import {
  Router,
  Scene,
  Actions,
  ActionConst,
} from 'react-native-router-flux'

import LoginScreen from './LoginScreen.js'
import HomeScreen from './HomeScreen.js'

class Splash extends React.Component {
  componentDidMount() {
    AsyncStorage
      .getItem('@Account:login')
      .then((value) => {
        if (value) {
          Actions.home()
        } else {
          Actions.login()
        }
      })
      .catch(() => {
        Actions.login()
      })
  }

  render() {
    return (<View />)
  }
}

export default class App extends Component {
  render() {
    return (
      <Router>
        <Scene key="root">
          <Scene hideNavBar
                 key="splash"
                 component={Splash} />
          <Scene hideNavBar
                 key="login"
                 component={LoginScreen} />
          <Scene key="home"
                 component={HomeScreen}
                 type={ActionConst.REPLACE} />
        </Scene>
      </Router>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
