import React from 'react'
import Strings from './res/Strings.js'
import {
  StyleSheet,
  View,
  Button,
  Alert,
  AsyncStorage,
} from 'react-native'

import { Actions } from 'react-native-router-flux'
import TwitterKit from 'react-native-fabric-twitterkit'

export default class LoginScreen extends React.Component {
  login() {
    TwitterKit.login((err, result) => {
      if (err) {
        console.log(err)
        Alert.alert('login failed')
      } else {
        console.log(result)
        AsyncStorage.setItem('@Account:login', 'true')
               .then(() => {
                 Actions.main()
               })
      }
    })
  }
  render() {
    return (
      <View style={styles.container}>
        <Button title={Strings.connect} onPress={this.login} />
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
