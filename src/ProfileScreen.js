import React from 'react'
import {
  StyleSheet,
  View,
  Text,
} from 'react-native'

import BaseScreen from './BaseScreen.js'

export default class ProfileScreen extends BaseScreen {
  render() {
    return (
      <View>
        <View style={styles.container}>
          <Text>Profile</Text>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    marginTop: 0,
  }
})
