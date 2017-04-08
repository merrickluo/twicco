import React from 'react'
import Strings from './res/Strings.js'
import {
  StyleSheet,
  View,
  Button,
  Alert,
  AsyncStorage,
} from 'react-native'

import TwitterKit from 'react-native-fabric-twitterkit'

import BaseScreen from './BaseScreen.js'

export default class LoginScreen extends BaseScreen {
  handleLogin = () => {
    TwitterKit.login((err, result) => {
      if (err) {
        console.log(err)
        Alert.alert('login failed')
      } else {
        console.log(result)
        AsyncStorage.setItem('@Account:login', 'true')
               .then(() => {
                 this.navigate('main', null, true)
               })
      }
    })
  }
  render() {
    return (
      <View style={styles.container}>
        <Button
          title={Strings.connect}
          onPress={this.handleLogin}
        />
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
