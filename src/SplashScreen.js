import React from 'react'

import { View, AsyncStorage } from 'react-native'
import { Actions, ActionConst } from 'react-native-router-flux'

export default class Splash extends React.Component {
  componentDidMount() {
    AsyncStorage
      .getItem('@Account:login')
      .then((value) => {
        if (value) {
          Actions.home({ type: ActionConst.RESET })
        } else {
          Actions.login({ type: ActionConst.RESET })
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
