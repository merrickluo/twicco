import React from 'react'
import {
  StyleSheet,
  View,
  Text,
} from 'react-native'

import BaseScreen from './BaseScreen.js'

export default class MessageScreen extends BaseScreen {
  render() {
    return (
      <View>
        <View style={styles.container}>
          <Text>Message</Text>
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
