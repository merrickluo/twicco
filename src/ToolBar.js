import React from 'react'
import Strings from './res/Strings.js'
import {
  StyleSheet,
  Text,
} from 'react-native'

import LinearGradient from 'react-native-linear-gradient'
import Colors from './res/Colors.js'

export default class ToolBar extends React.Component {
  render() {
    return (
      <LinearGradient
        colors={['#383838', '#222222', '#232323']}
        style={styles.bar}
      >
        <Text style={styles.title}>{this.props.title || Strings.appName}</Text>
      </LinearGradient>
    )
  }
}

const styles = StyleSheet.create({
  bar: {
    height: 32,
    paddingLeft: 8,
    justifyContent: 'center',
  },
  title: {
    fontSize: 14,
    color: Colors.white,
  },
})
