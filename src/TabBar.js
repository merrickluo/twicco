import React from 'react'

import {
  Animated,
  TouchableWithoutFeedback,
  StyleSheet,
} from 'react-native'

import LinearGradient from 'react-native-linear-gradient'

export default class TabBar extends React.Component {
  render() {
    const {
      navigationState,
      jumpToIndex,
      renderIcon,
    } = this.props

    console.log('shit')
    console.log(this.props)
    console.log('shit')
    return (
      <LinearGradient
        colors={['transparent', 'transparent', '#888888']}
        style={styles.tabBar}
      >
        {
          navigationState.routes.map((route, index) => {
            const focused = index === navigationState.index
            const scene = { route, index, focused }
            return (
              <TouchableWithoutFeedback key={route.key} onPress={() => jumpToIndex(index)}>
                <Animated.View style={styles.tab}>
                  { renderIcon(scene) }
                </Animated.View>
              </TouchableWithoutFeedback>
            )
          })
        }
      </LinearGradient>
    )
  }
}

const styles = StyleSheet.create({
  tabBar: {
    height: 48,
    flexDirection: 'row',
    backgroundColor: 'transparent',
  },
  tab: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  icon: {
    flexGrow: 1,
  },
  label: {
    textAlign: 'center',
    fontSize: 10,
    marginBottom: 1.5,
    backgroundColor: 'transparent',
  },
})
