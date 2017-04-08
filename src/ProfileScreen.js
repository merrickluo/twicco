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

export default class ProfileScreen extends BaseScreen {
  render() {
    return (
      <View>
        <ToolBar />
        <View style={styles.container}>
          <Text>Profile</Text>
        </View>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    marginTop: 32,
  }
})
