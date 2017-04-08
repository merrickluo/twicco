import React from 'react'

import { View, AsyncStorage } from 'react-native'

import BaseScreen from './BaseScreen.js'

export default class Splash extends BaseScreen {
  componentDidMount() {
    AsyncStorage
      .getItem('@Account:login')
      .then((value) => {
        if (value) {
          this.navigate('main', null, true)
        } else {
          this.navigate('login', null, true)
        }
      })
      .catch(() => {
        this.navigate('login', null, true)
      })
  }

  render() {
    return (<View />)
  }
}
