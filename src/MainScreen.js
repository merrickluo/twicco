import React from 'react'

import {
  View,
  StyleSheet,
  TouchableWithoutFeedback,
} from 'react-native'

import BaseScreen from './BaseScreen.js'
import ToolBar from './ToolBar.js'

import Icon from 'react-native-vector-icons/MaterialCommunityIcons'
import LinearGradient from 'react-native-linear-gradient'

import HomeScreen from './HomeScreen.js'
import MentionScreen from './MentionScreen.js'
import MessageScreen from './MessageScreen.js'
import ProfileScreen from './ProfileScreen.js'

class TabIcon extends React.Component {
  render() {
    const color = this.props.focused ? 'gray' : 'white'
    return (
      <View style={styles.tabIcon}>
        <TouchableWithoutFeedback onPress={this.props.onPress}>
          <Icon name={this.props.img} size={24} color={color} />
        </TouchableWithoutFeedback>
      </View>
    )
  }
}

const screens = [
  <HomeScreen key="home" />,
  <MentionScreen key="mention" />,
  <MessageScreen key="message" />,
  <ProfileScreen key="profile" />,
]

export default class MainScreen extends BaseScreen {
  handleTabPress = (i) => {
    console.log(i)
  }

  render() {
    return (
      <View style={styles.container}>
        <ToolBar style={styles.toolBar} />
        <View style={styles.content}>
          { screens[this.props.selectedTab] }
          <LinearGradient
            colors={['transparent', 'transparent', '#000000']}
            style={styles.tabBar}
          >
            <TabIcon
              img="home"
              focused={this.props.selectedTab === 0}
              onPress={() => this.handleTabPress(0)}
            />
            <TabIcon
              img="at"
              focused={this.props.selectedTab === 1}
              onPress={() => this.handleTabPress(1)}
            />
            <TabIcon
              img="email"
              focused={this.props.selectedTab === 2}
              onPress={() => this.handleTabPress(2)}
            />
            <TabIcon
              img="account"
              focused={this.props.selectedTab === 3}
              onPress={() => this.handleTabPress(3)}
            />
          </LinearGradient>
        </View>
      </View>
    )
  }
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
  },
  toolbar: {
    flex: 1,
    zIndex: 999,
  },
  content: {
    zIndex: 998,
    flex: 1,
    backgroundColor: '#222222'
  },
  tabBar: {
    bottom: 0,
    left: 0,
    right: 0,
    position: 'absolute',
    height: 36,
    width: '100%',
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  tabIcon: {
    flex: 1,
    alignItems: 'center',
  },
})
