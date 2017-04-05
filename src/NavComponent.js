import React from 'react'
import Strings from './Strings.js'
import {
  StyleSheet,
  View,
  Text,
} from 'react-native'

import LinearGradient from 'react-native-linear-gradient';

export default class NavComponent extends React.Component {
  static renderNavigationBar() {
    return (
      <LinearGradient colors={['#383838', '#222222', '#222222']} style={styles.bar}>
        <Text style={styles.title}>{Strings.appName}</Text>
      </LinearGradient>
    )
  }
}

const styles = StyleSheet.create({
  bar: {
    top: 0,
    right: 0,
    left: 0,
    height: 32,
    paddingLeft: 8,
    position: 'absolute',
    justifyContent: 'center',
  },
  title: {
    fontSize: 14,
    color: '#ffffff',
  },
})
