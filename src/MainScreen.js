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
import ActionButton from 'react-native-action-button'
import { NavigationActions } from 'react-navigation'

import HomeScreen from './HomeScreen.js'
import MentionScreen from './MentionScreen.js'
import MessageScreen from './MessageScreen.js'
import ProfileScreen from './ProfileScreen.js'

import { connect } from 'react-redux'

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

const mapStateToProps = (state) => {
  return {
    selectedTab: 0,
    account: state.account,
  }
}

const mapDispatchToProps = (dispatch) => ({
  dispatch,
  toCompose: () => {
    const action = NavigationActions.navigate({ routeName: 'compose' })
    dispatch(action)
  },
})

@connect(mapStateToProps, mapDispatchToProps)
export default class MainScreen extends BaseScreen {

  handleTabPress = (i) => {
    console.log(i)
  }

  handleComposeClick = () => {
    this.props.toCompose()
  }

  render() {
    return (
      <View style={styles.container}>
        <ToolBar style={styles.toolBar} />
        <View style={styles.content}>
          { screens[this.props.selectedTab] }
          <ActionButton
            buttonColor="rgba(255,255,255,0.8)"
            onPress={this.handleComposeClick}
            useNativeFeedback={false}
            offsetY={56}
            icon={<Icon name="comment-processing-outline" size={30} color="black" />}
          />
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
