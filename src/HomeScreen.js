import React from 'react'
import {
  StyleSheet,
  View,
  Text,
} from 'react-native'

/* import TwitterKit from 'react-native-fabric-twitterkit'
 * import LinearGradient from 'react-native-linear-gradient'*/

import BaseScreen from './BaseScreen.js'
import ToolBar from './ToolBar.js'

export default class HomeScreen extends BaseScreen {
  render() {
    return (
      <View style={{ backgroundColor: '#111' }}>
        <ToolBar />
        <View style={styles.container}>
          <Text>Home</Text>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    marginTop: 32,
    backgroundColor: '#111',
    height: '100%',
  }
})
