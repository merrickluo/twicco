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
        colors={['#383838', '#222222', '#222222']}
        style={styles.bar}
      >
        <Text style={styles.title}>{this.props.title || Strings.appName}</Text>
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
    color: Colors.white,
  },
})
