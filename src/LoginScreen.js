import React from 'react'
import Strings from './res/Strings.js'
import {
  StyleSheet,
  View,
} from 'react-native'

import TwitterKit from 'react-native-fabric-twitterkit'
import Icon from 'react-native-vector-icons/MaterialCommunityIcons'

import { connect } from 'react-redux'

import BaseScreen from './BaseScreen.js'

class LoginScreen extends BaseScreen {
  handleLogin = () => {
    let { dispatch } = this.props
    TwitterKit.login((err, result) => {
      if (err) {
        dispatch({ type: 'LOGIN_FAILED', error: err })
      } else {
        dispatch({ type: 'LOGIN_SUCCESS', account: result })
        this.navigate('main', null, true)
      }
    })
  }
  render() {
    return (
      <View style={styles.container}>
        <Icon.Button name="twitter" onPress={this.handleLogin}>
          {Strings.connect}
        </Icon.Button>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
})

export default connect()(LoginScreen)
