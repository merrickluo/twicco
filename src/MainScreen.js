import React from 'react'

import {
  View,
  StyleSheet,
} from 'react-native'

import BaseScreen from './BaseScreen.js'
import ToolBar from './ToolBar.js'

import Icon from 'react-native-vector-icons/MaterialIcons'

export default class MainScreen extends BaseScreen {
  render() {
    return (
      <View style={styles.container}>
        <ToolBar style={styles.toolbar} />
        <View style={styles.content}>
          <View style={styles.tabBar}>
            <Icon name="home" size={30} />
          </View>
        </View>
      </View>
    )
  }
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    flexDirection: 'column',
  },
  toolbar: {
    height: 32,
  },
  content: {
    backgroundColor: '#222'
  },
  tabBar: {
    height: 48,
    alignSelf: 'flex-end',
  },
})
