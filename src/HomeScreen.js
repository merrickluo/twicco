import React from 'react'
import Strings from './Strings.js'
import {
  StyleSheet,
  View,
  Button,
  Alert,
  Text,
} from 'react-native'

import TwitterKit from 'react-native-fabric-twitterkit'
import LinearGradient from 'react-native-linear-gradient'

import { Actions } from 'react-native-router-flux'

import NavComponent from './NavComponent.js'

export default class HomeScreen extends NavComponent {
  render() {
    return (
      <View style={styles.container}>
        <Text>HomeScreen</Text>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    marginTop: 32,
  }
})
