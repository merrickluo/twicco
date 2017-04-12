import React from 'react'
import Strings from './res/Strings.js'
import {
  StyleSheet,
  View,
} from 'react-native'

import Icon from 'react-native-vector-icons/MaterialCommunityIcons'
import { connect } from 'react-redux'
import { auth } from 'react-native-twitter'
import Config from 'react-native-config'

import BaseScreen from './BaseScreen.js'

class LoginScreen extends BaseScreen {
  handleLogin = () => {
    let { dispatch } = this.props
    const tokens = {
      consumerKey: Config.TWITTER_KEY,
      consumerSecret: Config.TWITTER_SECRET,
    }
    auth(tokens, 'https://luois.ninja/twicco')
      .then(r => {
        dispatch({ type: 'LOGIN_SUCCESS', account: r })
        this.navigate('main', null, true)
      })
      .catch(e => {
        dispatch({ type: 'LOGIN_FAILED', error: e })
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
