import React from 'react'
import {
  StyleSheet,
  View,
  Text,
} from 'react-native'

import BaseScreen from './BaseScreen.js'

export default class MentionScreen extends BaseScreen {
  render() {
    return (
      <View>
        <View style={styles.container}>
          <Text>Mention</Text>
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
