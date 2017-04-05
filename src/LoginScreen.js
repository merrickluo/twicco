import React from 'react'
import Strings from './Strings.js'
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
    TwitterKit.login(async (err, result) => {
      if (err) {
        console.log(err)
        Alert.alert('login failed')
      } else {
        await AsyncStorage.setItem('@Account:login', 'true')
        console.log(result)
        Actions.home()
      }
    })
  }
  render() {
    return (
      <View style={styles.container}>
        <Button title={Strings.connect} onPress={this.login}/>
      </View>
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
  connect: {
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
